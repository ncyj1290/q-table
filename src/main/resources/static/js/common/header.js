$(document).ready(function() {
    const $icon = $('.profile-icon');
    const $dropdown = $('.dropdown-menu');

    // 아이콘 클릭 시 토글
    $icon.on('click', function(e) {
        e.stopPropagation();
        $dropdown.toggle();
    });


    $dropdown.on('click', function(e) {
        e.stopPropagation();
    });

    // 외부 클릭 시 닫기
    $(document).on('click', function() {
        $dropdown.hide();
    });

    // 헤더 채팅 배지 업데이트 (페이지 로드 시)
    updateHeaderChatBadge();
});

// 헤더 채팅 배지 업데이트 함수
function updateHeaderChatBadge() {
    const $badge = $('#headerChatBadge');

    // 배지 요소가 없으면 (비로그인 상태) 리턴
    if ($badge.length === 0) {
        return;
    }

    $.ajax({
        url: '/api/chat/unread/total',
        type: 'POST',
        success: function(response) {
            if (response.success) {
                const totalUnread = response.totalUnread;
                if (totalUnread > 0) {
                    $badge.text(totalUnread).show();
                } else {
                    $badge.hide();
                }
            }
        },
        error: function(xhr, status, error) {
            console.error('Failed to fetch unread count:', error);
        }
    });
}

//document.addEventListener("DOMContentLoaded", function() {
//  const modal = document.getElementById("calendarModal");
//  const openBtn = document.getElementById("openModalBtn");
//  const closeBtn = document.querySelector(".close-btn");
//
//  // 열기
//  openBtn.addEventListener("click", function(e) {
//    e.preventDefault(); // a태그 기본 이동 막기
//    modal.style.display = "block";
//  });
//
//  // 닫기 (X버튼)
//  closeBtn.addEventListener("click", function() {
//    modal.style.display = "none";
//  });
//
//  // 모달 바깥 클릭 시 닫기
//  window.addEventListener("click", function(e) {
//    if (e.target === modal) {
//      modal.style.display = "none";
//    }
//  });
//});


