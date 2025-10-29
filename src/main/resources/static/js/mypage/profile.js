const profileImage = document.getElementById('profileImage');
const fileInput = document.getElementById('fileInput');
const profileMenu = document.getElementById('profileMenu');
const changeImageBtn = document.getElementById('changeImageBtn');
const resetImageBtn = document.getElementById('resetImageBtn');

// 클릭 시 메뉴 보여주기
profileImage.addEventListener('click', function(e) {
  profileMenu.style.display = 'block';
  const rect = profileImage.getBoundingClientRect();
  profileMenu.style.left = (rect.left + window.scrollX - 570) + 'px';
  profileMenu.style.top = (rect.top + window.scrollY + 30) + 'px';
  console.log(rect.left, rect.bottom); 
});


// 이미지 변경 버튼 누를 때 파일창 열기
changeImageBtn.addEventListener('click', function() {
  fileInput.click();
  profileMenu.style.display = 'none';
});

// 기본 이미지로 변경 버튼
resetImageBtn.addEventListener('click', function() {
  // 서버에 기본 이미지로 변경 요청
  $.ajax({
    url: '/resetProfileImage',
    type: 'POST',
    success: function() {
      profileImage.src = '/img/profile.png'; 
      alert('기본 이미지로 변경되었습니다.');
    },
    error: function() {
      alert('기본 이미지로 변경에 실패했습니다.');
    }
  });
  profileMenu.style.display = 'none';
});

// 파일 선택 후 기존 로직 유지
fileInput.addEventListener('change', function(event) {
  if (event.target.files[0]) {
    const reader = new FileReader();
    reader.onload = function(e) {
      profileImage.src = e.target.result;
    };
    reader.readAsDataURL(event.target.files[0]);

    const formData = new FormData();
    formData.append('profileImage', event.target.files[0]);

    $.ajax({
      url: '/uploadProfileImage',
      type: 'POST',
      data: formData,
      processData: false,
      contentType: false,
      success: function() {
        alert('이미지가 성공적으로 저장되었습니다.');
      },
      error: function() {
        alert('이미지 저장에 실패했습니다.');
      }
    });
  }
});


// 메뉴 외부 클릭 시 닫기
document.addEventListener('click', function(e) {
  if (!profileMenu.contains(e.target) && e.target !== profileImage) {
    profileMenu.style.display = 'none';
  }
});


// 금액 불러오기
$(document).ready(function() {
    $.getJSON('/mypage/qmoneyBalance', function(data) {
        const formattedBalance = Number(data.balance).toLocaleString('ko-KR'); // 천 단위 쉼표
        $('#qmoneyBalance').text(formattedBalance + '원');
    });

    // 마이페이지 채팅 배지 업데이트
    updateMypageChatBadge();
});

// 마이페이지 채팅 배지 업데이트 함수
function updateMypageChatBadge() {
    const $badge = $('#mypageChatBadge');

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


// 결제 내역 천 단위 구분하기
document.addEventListener('DOMContentLoaded', function() {
  document.querySelectorAll('.price-new').forEach(function(el) {
    let text = el.textContent.trim();
    // 숫자만 추출
    let amount = text.replace(/[^0-9]/g, '');
    if (amount) {
      // 숫자를 쉼표 포함 문자열로 변환 + '원' 붙임
      el.textContent = Number(amount).toLocaleString('ko-KR') + '원';
    }
  });
});

