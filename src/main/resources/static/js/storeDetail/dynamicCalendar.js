$(function() {
    let currentDate = new Date();
    let currentMonth = currentDate.getMonth();
    let currentYear = currentDate.getFullYear();

    // 임시 휴무 요일 데이터 (나중에 서버에서 받아올 예정)
    // 0=일요일, 1=월요일, 2=화요일, 3=수요일, 4=목요일, 5=금요일, 6=토요일
    const closedDays = [0, 1]; // 예시: 일요일(0), 월요일(1) 휴무

    const months = [
        '1월', '2월', '3월', '4월', '5월', '6월',
        '7월', '8월', '9월', '10월', '11월', '12월'
    ];

    function generateCalendar(year, month) {
        // 달력 제목 업데이트
        $('.calendar-title').text(`${year}년 ${months[month]}`);

        // 해당 월의 첫째 날과 마지막 날
        const firstDay = new Date(year, month, 1);
        const lastDay = new Date(year, month + 1, 0);
        const daysInMonth = lastDay.getDate();
        const startingDayOfWeek = firstDay.getDay(); // 0=일요일, 6=토요일

        // 이전 달의 마지막 날들
        const prevMonthLastDay = new Date(year, month, 0).getDate();

        let calendarHTML = '';
        let dayCount = 1;

        // 필요한 주 수만 계산해서 깔끔하게 표시
        const totalCells = startingDayOfWeek + daysInMonth;
        const weeksNeeded = Math.ceil(totalCells / 7);
        let nextMonthDay = 1;

        for (let week = 0; week < weeksNeeded; week++) {
            for (let day = 0; day < 7; day++) {
                const cellIndex = week * 7 + day;

                if (cellIndex < startingDayOfWeek) {
                    // 이전 달 날짜들
                    const prevDate = prevMonthLastDay - (startingDayOfWeek - cellIndex - 1);
                    calendarHTML += `<div class="calendar-day prev-month">${prevDate}</div>`;

                } else if (dayCount <= daysInMonth) {
                    // 현재 달 날짜들
                    const currentDayOfWeek = day; // 0=일요일, 1=월요일...
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
                    // 다음 달 날짜들 (마지막 주의 남은 빈 칸만 채움)
                    let dayClasses = 'calendar-day next-month';
                    if (day === 0) dayClasses += ' sunday';
                    if (day === 6) dayClasses += ' saturday';

                    calendarHTML += `<div class="${dayClasses}">${nextMonthDay}</div>`;
                    nextMonthDay++;
                }
            }
        }

        $('.calendar-days').html(calendarHTML);
    }

    // 월 이동 기능
    $('.calendar-nav-btn').on('click', function() {
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