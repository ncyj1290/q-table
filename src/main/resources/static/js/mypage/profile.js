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
    profileImage.src = e.target.result; // 이미지 변경
  };
  if (event.target.files[0]) {
    reader.readAsDataURL(event.target.files[0]);
  }
});
