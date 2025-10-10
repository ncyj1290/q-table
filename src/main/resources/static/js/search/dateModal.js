// ===================================
// 변수 선언
// ===================================
// 서버에서 가져올 데이터 (TODO: 백엔드 연동)
const closedDays = [0, 1]; // 휴무 요일 (0=일요일, 1=월요일)

// 상수
const MONTHS = [
	'1월', '2월', '3월', '4월', '5월', '6월',
	'7월', '8월', '9월', '10월', '11월', '12월'
];
const daysOfWeek = ['일', '월', '화', '수', '목', '금', '토'];

// 일반 변수
let currentDate = new Date();
let currentMonth = currentDate.getMonth();
let currentYear = currentDate.getFullYear();
let selectedDate = null;  // 선택된 날짜 (YYYY-MM-DD 형식)


$(function() {
	const $calendarTitle = $('.calendar-title');
	const $calendarDays = $('.calendar-days');
	const $calendarNavBtns = $('.calendar-nav-btn');

	// ===================================
	// 이벤트 리스너
	// ===================================
	
	// 적용하기 버튼 
	$('#apply-btn-date').on('click', function() {
		if (!selectedDate) {
		    alert('날짜를 선택해주세요.');
			return;
	    }
		// 2. 선택된 날짜(YYYY-MM-DD)로 Date 객체 생성
	    const dateObj = new Date(selectedDate);
	    // 3. getDay()로 요일 인덱스(0~6) 가져오기
	    const dayIndex = dateObj.getDay();
	    const dayName = daysOfWeek[dayIndex]; // 요일 이름 (예: '수')
	    // 4. 원하는 형식으로 텍스트 조합 (예: 10.09 (수))
	    const displayText = `${selectedDate.substring(5).replace('-', '.')} (${dayName})`;
	    // 5. 화면에 표시
	    $('.rsv-date').text(displayText);
		
		//인원수 
		const personCnt = $('#person-count').val();
		$('.rsv-person-count').text(personCnt + '명');
		//시간 
		const time = $('#time').val();
		$('.rsv-time').text(time);    
		hideDateModal();
	});
	
	// 월 이동 기능
	$calendarNavBtns.on('click', function() {
		const isNext = $(this).text() === '▶';

		if (isNext) {
			currentMonth++;
			if (currentMonth > 11) {
				currentMonth = 0;
				currentYear++;
			}
		} else {
			currentMonth--;
			if (currentMonth < 0) {
				currentMonth = 11;
				currentYear--;
			}
		}
		generateCalendar(currentYear, currentMonth);
	});

	// 날짜 클릭 (이벤트 위임)
	$calendarDays.on('click', '.calendar-day.available', function() {
		const dateNumber = $(this).find('.date-number').text();

		// 휴무일은 클릭 불가
		if ($(this).find('.availability-mark.unavailable').length > 0) {
			return;
		}

		// 이전/다음 달 날짜는 클릭 불가
		if ($(this).hasClass('prev-month') || $(this).hasClass('next-month')) {
			return;
		}

		// 선택된 날짜 저장 (YYYY-MM-DD 형식)
		const year = currentYear;
		const month = String(currentMonth + 1).padStart(2, '0');
		const day = String(dateNumber).padStart(2, '0');
		selectedDate = `${year}-${month}-${day}`;

		// 모든 날짜에서 selected 클래스 제거
		$calendarDays.find('.calendar-day').removeClass('selected');

		// 클릭한 날짜에 selected 클래스 추가
		$(this).addClass('selected');

		console.log('선택된 날짜:', selectedDate);
		// TODO: 백엔드 연동 시 예약 가능 시간 불러오기
	});

	// 초기 달력 생성
	generateCalendar(currentYear, currentMonth);

	// ===================================
	// 함수 정의
	// ===================================

	// 달력 생성
	function generateCalendar(year, month) {
		// 달력 제목 업데이트
		$calendarTitle.text(`${year}년 ${MONTHS[month]}`);

		// 해당 월의 첫째 날과 마지막 날
		const firstDay = new Date(year, month, 1);
		const lastDay = new Date(year, month + 1, 0);
		const daysInMonth = lastDay.getDate();
		const startingDayOfWeek = firstDay.getDay();

		// 이전 달의 마지막 날
		const prevMonthLastDay = new Date(year, month, 0).getDate();

		let calendarHTML = '';
		let dayCount = 1;

		// 필요한 주 수 계산
		const totalCells = startingDayOfWeek + daysInMonth;
		const weeksNeeded = Math.ceil(totalCells / 7);
		let nextMonthDay = 1;

		for (let week = 0; week < weeksNeeded; week++) {
			for (let day = 0; day < 7; day++) {
				const cellIndex = week * 7 + day;

				if (cellIndex < startingDayOfWeek) {
					// 이전 달 날짜
					const prevDate = prevMonthLastDay - (startingDayOfWeek - cellIndex - 1);
					calendarHTML += `<div class="calendar-day prev-month">${prevDate}</div>`;

				} else if (dayCount <= daysInMonth) {
					// 현재 달 날짜
					const currentDayOfWeek = day;
					const isClosedDay = closedDays.includes(currentDayOfWeek);
					const isSunday = day === 0;
					const isSaturday = day === 6;
					const isToday = year === currentDate.getFullYear() &&
								   month === currentDate.getMonth() &&
								   dayCount === currentDate.getDate();

					// 선택된 날짜 확인
					const dateString = `${year}-${String(month + 1).padStart(2, '0')}-${String(dayCount).padStart(2, '0')}`;
					const isSelected = selectedDate === dateString;

					let dayClasses = 'calendar-day available';
					if (isSunday) dayClasses += ' sunday';
					if (isSaturday) dayClasses += ' saturday';
					if (isSelected) dayClasses += ' selected';

					calendarHTML += `
						<div class="${dayClasses}">
							<span class="date-number">${dayCount}</span>
						</div>
					`;
					dayCount++;

				} else {
					// 다음 달 날짜
					let dayClasses = 'calendar-day next-month';
					if (day === 0) dayClasses += ' sunday';
					if (day === 6) dayClasses += ' saturday';

					calendarHTML += `<div class="${dayClasses}">${nextMonthDay}</div>`;
					nextMonthDay++;
				}
			}
		}

		$calendarDays.html(calendarHTML);
	}
	
	$('.search-box').on('click', '.date-area', showDateModal);
	// 모달닫기
	$(".close-btn").on("click", function() {
		hideDateModal();
	});
	//모달 외에 부분 눌렀을때
	$(window).on("click", function(e) {
	    if (e.target == $("#dateModal")[0]) {
			hideDateModal();
		}
	});	
	
});


function showDateModal() {
	$("#dateModal").fadeIn(200); 
}

function hideDateModal () {
	$("#dateModal").fadeOut(200);
}