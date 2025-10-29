// ===================================
// 변수 선언
// ===================================
// 요일 이름을 숫자로 변환에 사용할 맵
const DAY_NAME_TO_NUMBER = {'일': 0, '월': 1, '화': 2, '수': 3,'목': 4,'금': 5,'토': 6};

// 상수
const MONTHS = [
	'1월', '2월', '3월', '4월', '5월', '6월',
	'7월', '8월', '9월', '10월', '11월', '12월'
];

// ===================================
// 재사용 가능한 달력 초기화 함수
// ===================================
function initCalendar(containerSelector) {
	
	const $calendarContainer = $(containerSelector + ' .calendar-container');
	const $calendarTitle = $(containerSelector + ' .calendar-title');
	const $calendarDays = $(containerSelector + ' .calendar-days');
	const $calendarNavBtns = $(containerSelector + ' .calendar-nav-btn');
	const noShowCount = $("#noShowCount").val();

	// 휴무 요일
	let closedDays = [];

	// 일반 변수
	let currentDate = new Date();
	let currentMonth = currentDate.getMonth();
	let currentYear = currentDate.getFullYear();
	let selectedDate = null;  // 선택된 날짜 (YYYY-MM-DD 형식)
	let currentReserveIdx = null;  // 예약 번호 (마이페이지 예약 변경용)

	// 예약 가능 기간 계산
	const today = new Date();
	today.setHours(0, 0, 0, 0);
	const maxReservationDate = new Date();
	maxReservationDate.setDate(maxReservationDate.getDate() + 60); // 오늘부터 60일 후
	maxReservationDate.setHours(23, 59, 59, 999);

	// ===================================
	// 휴일 데이터 로드 
	// ===================================
	const holidaysData = $calendarContainer.attr('data-holidays')|| '';
	// 문자열을 배열로 변환 + 공백제거  
	const holidayNames = holidaysData.split(',').map(name => name.trim()).filter(name => name);
	// 한글로 된 요일을 숫자로 변환 
	closedDays = holidayNames.map(name => DAY_NAME_TO_NUMBER[name]).filter(num => num !== undefined);

	// 예약 번호 설정 (마이페이지용)
	currentReserveIdx = $calendarContainer.closest('[data-reserve-idx]').data('reserve-idx');

	// ===================================
	// 내부 함수 정의
	// ===================================

	// 시간 옵션 업데이트 (오늘 날짜면 지난 시간 비활성화)
	function updateTimeOptions(selectedDateStr) {
		const $timeSelect = $(containerSelector + ' #reservation-time');
		const now = new Date();
		const selectedDateObj = new Date(selectedDateStr);

		// 오늘 날짜인지 확인
		const isToday = selectedDateObj.getFullYear() === now.getFullYear() &&
						selectedDateObj.getMonth() === now.getMonth() &&
						selectedDateObj.getDate() === now.getDate();

		if (isToday) {
			// 현재 시간
			const currentHour = now.getHours();
			const currentMinute = now.getMinutes();
			const currentTimeStr = `${String(currentHour).padStart(2, '0')}:${String(currentMinute).padStart(2, '0')}`;
			console.log(currentTimeStr);
			// 모든 option 순회
			$timeSelect.find('option').each(function() {
				const optionTime = $(this).val();

				// 지난 시간이면 비활성화
				if (optionTime <= currentTimeStr) {
					$(this).prop('disabled', true);
				} else {
					$(this).prop('disabled', false);
				}
			});

			// 첫 번째 활성화된 옵션 선택
			const firstEnabled = $timeSelect.find('option:not(:disabled)').first();
			console.log(firstEnabled);
			if (firstEnabled.length > 0) {
				$timeSelect.val(firstEnabled.val());
			}
		} else {
			// 오늘이 아니면 모든 시간 활성화
			$timeSelect.find('option').prop('disabled', false);
		}
	}

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

					// 해당 날짜 객체 생성
					const thisDate = new Date(year, month, dayCount);
					thisDate.setHours(0, 0, 0, 0);

					// 지난 날짜 체크
					const isPastDate = thisDate < today;

					// 60일 이후 날짜 체크
					const isOutOfRange = thisDate > maxReservationDate;

					// 선택된 날짜 확인
					const dateString = `${year}-${String(month + 1).padStart(2, '0')}-${String(dayCount).padStart(2, '0')}`;
					const isSelected = selectedDate === dateString;

					let dayClasses = 'calendar-day';
					if (!isPastDate && !isOutOfRange) dayClasses += ' available';
					if (isSunday) dayClasses += ' sunday';
					if (isSaturday) dayClasses += ' saturday';
					if (isSelected) dayClasses += ' selected';
					if (isPastDate) dayClasses += ' past-date';
					if (isOutOfRange) dayClasses += ' out-of-range';

					const availabilityMark = (isClosedDay || isPastDate || isOutOfRange) ?
						'<span class="availability-mark unavailable">X</span>' :
						'<span class="availability-mark">O</span>';

					calendarHTML += `
						<div class="${dayClasses}">
							<span class="date-number">${dayCount}</span>
							${availabilityMark}
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

	// ===================================
	// 이벤트 리스너
	// ===================================

	// 월 이동 기능
	$calendarNavBtns.off('click').on('click', function() {
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
	$calendarDays.off('click').on('click', '.calendar-day.available', function() {
		const dateNumber = $(this).find('.date-number').text();

		// 휴무일은 클릭 불가
		if ($(this).find('.availability-mark.unavailable').length > 0) {
			return;
		}

		// 이전/다음 달 날짜는 클릭 불가
		if ($(this).hasClass('prev-month') || $(this).hasClass('next-month')) {
			return;
		}

		// 지난 날짜 및 60일 이후 날짜는 클릭 불가
		if ($(this).hasClass('past-date') || $(this).hasClass('out-of-range')) {
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

		// 시간 옵션 업데이트 (오늘 날짜면 지난 시간 비활성화)
		updateTimeOptions(selectedDate);
	});

	// 예약하기 버튼 클릭 (마이페이지 재사용 가능하게 수정)
	$(containerSelector + ' #reservation-submit-btn').off('click').on('click', function() {
		// 날짜 선택 확인
		if (!selectedDate) {
			alert('날짜를 선택해주세요.');
			return;
		}

		// 인원수 입력 확인
		const personCount = $(containerSelector + ' #person-count').val().trim();
		
		if (!personCount || personCount === '0') {
			alert('인원수를 입력해주세요.');
			return;
		}

		// 시간 선택 확인
		const time = $(containerSelector + ' #reservation-time').val();
		if (!time) {
			alert('시간을 선택해주세요.');
			return;
		}
		
		// 노쇼 카운트 2회 이상 고객 예약 불가능
		if (noShowCount > 2){
			alert("2번 이상 노쇼를 한 고객은 예약이 불가능합니다.");
			return;
		}

		// store_idx 가져오기: 마이페이지는 .calendar-container.data('store-idx'), 식당상세는 URL 파라미터
		let storeIdx = $calendarContainer.data('store-idx');

		if (!storeIdx) {
			const urlParams = new URLSearchParams(location.search);
			storeIdx = urlParams.get('store_idx');
		}

		if (!storeIdx) {
			alert('잘못된 접근입니다.');
			return;
		}

		// 마이페이지에서만 예약번호 존재
		console.log("storeIdx : " + storeIdx);
		console.log("resIdx : " + currentReserveIdx);
		if (currentReserveIdx) {
			// 마이페이지: 기존 예약 변경
			location.href = `/reservation?store_idx=${storeIdx}&reserve_idx=${currentReserveIdx}&reserve_date=${selectedDate}&reserve_time=${time}&person_count=${personCount}`;
		} else {
			// 식당상세: 새로운 예약 생성
			location.href = `/reservation?store_idx=${storeIdx}&reserve_date=${selectedDate}&reserve_time=${time}&person_count=${personCount}`;
		}
	});

	// 초기 달력 생성
	generateCalendar(currentYear, currentMonth);
}

// ===================================
// 페이지 로드 시 자동 초기화 (식당 상세 페이지용)
// ===================================
$(function() {
	// body 요소에 달력이 있으면 자동 초기화
	if ($('body .calendar-container').length > 0) {
		initCalendar('body');
	}
	
	// 인원수 입력 필드 포커스/블러 이벤트
	$('#person-count').on('focus', function() {
		if ($(this).val() === '0') {
			$(this).val('');
		}
	}).on('blur', function() {
		if ($(this).val() === '') {
			$(this).val('0');
		}
	});
});
