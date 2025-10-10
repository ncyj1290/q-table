// ===================================
// 변수 선언
// ===================================
let currentImageIndex = 0;  // 현재 보이는 이미지 인덱스

$(function() {
	// DOM 캐싱
	const $sliderImages = $('.slider-image');
	const $prevButton = $('.prev-button');
	const $nextButton = $('.next-button');
	const $viewAllButton = $('.view-all-images');
	const $modal = $('#allImagesModal');
	const $closeModal = $('.close-modal');

	const totalImages = $sliderImages.length;

	// ===================================
	// 이벤트 리스너
	// ===================================

	// 이전 이미지 버튼
	$prevButton.on('click', function() {
		showPreviousImage();
	});

	// 다음 이미지 버튼
	$nextButton.on('click', function() {
		showNextImage();
	});

	// 모든 사진 보기 버튼
	$viewAllButton.on('click', function() {
		openAllImagesModal();
	});

	// 모달 닫기 버튼
	$closeModal.on('click', function() {
		closeAllImagesModal();
	});

	// 모달 배경 클릭 시 닫기
	$modal.on('click', function(e) {
		if (e.target === this) {
			closeAllImagesModal();
		}
	});

	// ===================================
	// 함수 정의
	// ===================================

	// 이전 이미지 표시
	function showPreviousImage() {
		currentImageIndex--;

		// 첫 이미지에서 이전 버튼 누르면 마지막 이미지로
		if (currentImageIndex < 0) {
			currentImageIndex = totalImages - 1;
		}

		updateActiveImage();
	}

	// 다음 이미지 표시
	function showNextImage() {
		currentImageIndex++;

		// 마지막 이미지에서 다음 버튼 누르면 첫 이미지로
		if (currentImageIndex >= totalImages) {
			currentImageIndex = 0;
		}

		updateActiveImage();
	}

	// 활성 이미지 업데이트
	function updateActiveImage() {
		// 모든 이미지 숨김
		$sliderImages.removeClass('active');

		// 현재 인덱스 이미지만 표시
		$sliderImages.eq(currentImageIndex).addClass('active');
	}

	// 모든 이미지 보기 모달 열기
	function openAllImagesModal() {
		$modal.fadeIn(300);
		$('body').css('overflow', 'hidden'); // 스크롤 방지
	}

	// 모든 이미지 보기 모달 닫기
	function closeAllImagesModal() {
		$modal.fadeOut(300);
		$('body').css('overflow', 'auto'); // 스크롤 복원
	}
});
