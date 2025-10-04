// ===================================
// 이벤트 리스너 & 실행 코드
// ===================================
$(function() {
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

	// 초기 달력 생성
	generateCalendar(currentYear, currentMonth);
});

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

// DOM 캐싱
const $calendarTitle = $('.calendar-title');
const $calendarDays = $('.calendar-days');
const $calendarNavBtns = $('.calendar-nav-btn');

// 일반 변수
let currentDate = new Date();
let currentMonth = currentDate.getMonth();
let currentYear = currentDate.getFullYear();

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

				let dayClasses = 'calendar-day available';
				if (isSunday) dayClasses += ' sunday';
				if (isSaturday) dayClasses += ' saturday';
				if (isToday) dayClasses += ' selected';

				const availabilityMark = isClosedDay ?
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