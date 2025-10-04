// ===================================
// 이벤트 리스너 & 실행 코드
// ===================================
$(function() {
	// 리뷰 작성 모달 열기
	$reviewButton.on('click', function() {
		$reviewModal.show();
	});

	// 모달 닫기 - X 버튼
	$reviewModal.on('click', '.close', function() {
		$reviewModal.hide();
	});

	// 모달 닫기 - 배경 클릭
	$reviewModal.on('click', function(e) {
		if (e.target.id === 'reviewModal') {
			$reviewModal.hide();
		}
	});

	// 별점 마우스 오버
	$starElements.on('mouseenter', function() {
		const rating = $(this).data('rating');
		displayStars(rating);
	});

	// 별점 마우스 아웃
	$starElements.on('mouseleave', function() {
		displayStars(selectedRating);
	});

	// 별점 클릭
	$starElements.on('click', function() {
		selectedRating = $(this).data('rating');
		displayStars(selectedRating);
	});

	// 이미지 파일 선택
	$reviewImages.on('change', function(e) {
		const files = Array.from(e.target.files);

		files.forEach(file => {
			if (file.type.startsWith('image/')) {
				selectedFiles.push(file);
				addImagePreview(file);
			}
		});
	});

	// 이미지 삭제
	$imagePreview.on('click', '.remove-btn', function() {
		const filename = $(this).parent().data('filename');
		removeImagePreview(filename);
	});
});

// ===================================
// 변수 선언
// ===================================
// DOM 캐싱
const $reviewButton = $('.review-controls .positive-button');
const $reviewModal = $('#reviewModal');
const $starElements = $('.star');
const $reviewImages = $('#reviewImages');
const $imagePreview = $('#imagePreview');

// 일반 변수
let selectedRating = 0;  // 선택된 별점 (1-5)
let selectedFiles = [];  // 선택된 이미지 파일 배열

// ===================================
// 함수 정의
// ===================================

// 별점 표시
function displayStars(rating) {
    $('.star').each(function(index) {
        // index는 0부터 시작, rating보다 작으면 채워진 별
        if (index < rating) {
            $(this).text('★').addClass('filled');
        } else {
            $(this).text('★').removeClass('filled');
        }
    });
}

// 이미지 미리보기 추가
function addImagePreview(file) {
    const reader = new FileReader(); // 파일 익는 브라우저 내장 api 
	// 파일 읽기가 끝났을 떄 실행되는 콜백 함수 
    reader.onload = function(e) {
        const previewHtml = `
            <div class="preview-item" data-filename="${file.name}">
                <img src="${e.target.result}" alt="미리보기">
                <button type="button" class="remove-btn">×</button>
            </div>
        `;
        $('#imagePreview').append(previewHtml);
    };
    reader.readAsDataURL(file);  // 파일을 Base64로 읽어 미리보기
}

// 이미지 미리보기 삭제
function removeImagePreview(filename) {
    // 배열에서 해당 파일 제거
    selectedFiles = selectedFiles.filter(file => file.name !== filename);
    // DOM에서 미리보기 요소 제거
    $(`.preview-item[data-filename="${filename}"]`).remove();
}
