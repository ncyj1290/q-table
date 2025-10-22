// 프로필 이미지 클릭 시 파일 선택 창 열기
const profileImage = document.getElementById('profileImage');
const fileInput = document.getElementById('fileInput');

profileImage.addEventListener('click', function() {
  fileInput.click();
});

// 파일 선택 후 이미지 미리보기
fileInput.addEventListener('change', function(event) {
  const reader = new FileReader();
  reader.onload = function(e) {
    profileImage.src = e.target.result;
  };
  if (event.target.files[0]) {
    reader.readAsDataURL(event.target.files[0]);

    // 파일 서버로 전송
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

// 금액 불러오기
$(document).ready(function() {
    $.getJSON('/mypage/qmoneyBalance', function(data) {
        const formattedBalance = Number(data.balance).toLocaleString('ko-KR'); // 천 단위 쉼표
        $('#qmoneyBalance').text(formattedBalance + '원');
    });
});
