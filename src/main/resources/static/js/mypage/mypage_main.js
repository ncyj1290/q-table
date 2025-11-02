let currentStat = "rsrt_05";  // 기본 예약상태 초기값 할당
function formatAmountWithComma(amount) {
    return String(amount).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function showTab(tabId, element, reserveResult) {

	// 모든 탭 내용 숨기기
	document.querySelectorAll(".tab-content").forEach(c => c.style.display = "none");

	// 선택한 탭만 보이기
	document.getElementById(tabId).style.display = "block";

	// 모든 탭 라벨에서 active 제거
	document.querySelectorAll(".reserve-label").forEach(l => l.classList.remove("active"));

	// 클릭한 라벨 활성화
	element.classList.add("active");

	currentStat = reserveResult;  // 현재 상태 갱신

	//예약&취소 조회 
	$.ajax({
		url: "/reservation_list",
		type: "GET",
		data: { reserveResult: currentStat },
		headers: { "X-Requested-With": "XMLHttpRequest" },
		success: function(html) {
			reserveResult: currentStat
			document.getElementById("reservationContent").innerHTML = html;

			// 예약현황 o -> 취소 select
			const tabContentElements = document.querySelectorAll(".tab-content");
			tabContentElements.forEach(c => c.style.display = "none");

			//예약현황 o -> 예약 & 취소 select
			const currentTabContent = document.getElementById(tabId);
			(currentTabContent ? currentTabContent : document.getElementById("reservationContent")).style.display = "block";

			// 메인에서 취소 누를 때 메세지 삭제 & 방문예정 클릭 시 메세지 띄우기
			const emptyMsg = document.querySelector('.reservation-empty-content');
			if (!emptyMsg) return;  // 요소가 없으면 종료
			emptyMsg.style.display = (tabId === "cancel" && html.trim() !== "") ? "none" : "block";

		},
		error: function() {
			alert("[translate:데이터 조회가 실패하였습니다.]");
		}
	});
}

//예약취소
function formatWithComma(amount) {
    return Number(amount).toLocaleString('ko-KR');
}
$(document).on('click', 'button[data-type="cancelBtn"]', function() {
    console.log('예약 취소 버튼 클릭됨');
    const reserveIdx = $(this).data('reserveIdx');
    if (!reserveIdx) {
        alert('예약 ID가 없습니다.');
        return;
    }
    $.ajax({
        url: '/reservation_cancel',
        type: 'POST',
        data: { reserveIdx },
		dataType: 'json',
        success: function(resp) {
            console.log(reserveIdx, resp);
            if (resp.success) {
                // 큐머니 프로필 즉시 반영
                if (typeof resp.qmoney !== "undefined") {
					const formatted = formatWithComma(resp.qmoney);
                    $("#qmoneyBalance").text(formatted + "원"); 
                }
                const cancelTabLabel = document.querySelector('.reserve-label[data-tab="cancel"]');
                showTab('cancel', cancelTabLabel, 'rsrt_03');
                alert('예약이 취소되었습니다.');
            } else {
                alert('예약 취소에 실패했습니다.');
            }
        },
        error: function() {
            alert('네트워크 오류가 발생했습니다.');
        }
    });
});


// 예약변경 모달을 연 후, 프래그먼트 html이 동적으로 삽입된 이후 실행!
document.body.addEventListener('click', async (e) => {
	const btn = e.target.closest('.positive-button');
	if (!btn) return;

	const type = btn.getAttribute('data-type');
	const storeIdx = btn.getAttribute('data-store-idx');
	const reserveIdx = btn.getAttribute('data-reserve-idx');
	const people = btn.getAttribute('data-people');
	const reserveTime = btn.getAttribute('data-reserve-time');

	if (type === 'changeBtn' || type === 'rebookBtn') {
		// 서버에서 달력 프래그먼트 가져오기
		const response = await fetch(`/reserv_change?store_idx=${storeIdx}&reserve_idx=${reserveIdx}&people=${people}&reserve_time=${reserveTime}`);

		if (response.ok) {
			const html = await response.text();
			document.getElementById('mypage-calendar-wrapper').innerHTML = html;

			// 달력 컨테이너에 data 속성 설정
			const calendarContainer = document.querySelector('#mypage-calendarModal .calendar-container');

			if (calendarContainer) {
				// 달력 컨테이너에 예약 정보 설정
				calendarContainer.setAttribute('data-store-idx', storeIdx);

				// 예약번호는 모달 또는 부모 요소에 설정
				const modal = document.getElementById('mypage-calendarModal');
				modal.setAttribute('data-reserve-idx', reserveIdx);

				console.log('✅ 달력에 설정됨 - storeIdx:', storeIdx, 'reserveIdx:', reserveIdx);
			}

			// 달력 초기화
			initCalendar('#mypage-calendarModal');

			// 모달 표시
			document.getElementById('mypage-calendarModal').style.display = 'block';
		}
	}
});


//모달 열고 닫기
const closeModalBtn = document.getElementById('mypage-closeCalendarModal');
if (closeModalBtn) {
	closeModalBtn.addEventListener('click', () => {
		document.getElementById('mypage-calendarModal').style.display = 'none';
	});
}


// 클릭 시 해당 가게로 이동
document.body.addEventListener('click', function(e) {

	// 가게 이름 클릭 체크 (먼저 처리)
	if (e.target.classList.contains('store-name')) {
		const storeIdx = e.target.getAttribute('data-store-idx');
		if (storeIdx) {
			window.location.href = `/store_detail_main?store_idx=${storeIdx}`;
		}
		return;
	}
	// 재예약 버튼 클릭 시 상세페이지 이동
	const rebookBtn = e.target.closest('[data-type="rebookBtn"]');
	if (rebookBtn) {
		const storeIdx = rebookBtn.getAttribute('data-store-idx');
		if (storeIdx) {
			window.location.href = `/store_detail_main?store_idx=${storeIdx}`;
		}
		return;
	}

	// . 예약 카드 클릭 시 상세페이지 이동 (버튼 제외)
	const card = e.target.closest('.reservation-item');
	const isActionBtn = e.target.classList.contains('positive-button') || e.target.classList.contains('negative-button');

	if (card && !isActionBtn) {
		const storeIdx = card.getAttribute('data-store-idx');
		if (storeIdx) {
			window.location.href = `/store_detail_main?store_idx=${storeIdx}`;
		}
	}
});

//회원 탈퇴
$('#memberDeleteBtn').on('click', function() {
	const password = $('#passwordInput').val().trim();
	const agree = $('#agreeCheckbox').is(':checked');

	if (!password) {
		alert('비밀번호를 입력하세요');
		return;
	}

	if (!agree) {
		alert('약관에 동의해주세요');
		return;
	}

	if (!confirm('정말 탈퇴하시겠습니까?')) {
		return;
	}

	$.ajax({
		type: 'POST',
		url: '/member_delete',
		data: {
			password: password,
			agree: agree
		},
		success: function(data) {
			if (data.success) {
				alert('회원탈퇴가 완료되었습니다.');
				$.post('/logout', function() {
					window.location.href = '/';
				});
			} else {
				alert('오류: ' + data.message);
			}
		},
		error: function(xhr, status, error) {
			alert('요청 중 오류가 발생했습니다.\n' + xhr.responseText);
		}
	});
});









