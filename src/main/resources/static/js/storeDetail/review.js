// ===================================
// 변수 선언
// ===================================
// 일반 변수
let selectedRating = 0;  // 선택된 별점 (1-5)
let selectedFiles = [];  // 선택된 이미지 파일 배열
let reviewPagination = null;  // 페이지네이션 인스턴스

$(function() {
	const $reviewButton = $('.review-controls .positive-button');
	const $reviewModal = $('#reviewModal');
	const $starElements = $('.star');
	const $reviewImages = $('#reviewImages');
	const $imagePreview = $('#imagePreview');

	// URL에서 store_idx 가져오기
	const urlParams = new URLSearchParams(window.location.search);
	const storeIdx = urlParams.get('store_idx');

	// ===================================
	// 이벤트 리스너
	// ===================================

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

	// 리뷰 정렬 변경
	$('#reviewSort').on('change', function() {
		const sortType = $(this).val();

		// 페이지네이션 인스턴스가 있으면 정렬 타입 업데이트하고 첫 페이지 로드
		if (reviewPagination) {
			reviewPagination.params.sort_type = sortType;
			reviewPagination.loadPage(1);
		}
	});

	// ===================================
	// 페이지네이션 초기화
	// ===================================

	// 리뷰 페이지네이션 초기화
	reviewPagination = new Pagination({
		dataContainer: '#reviews-list',
		paginationContainer: '#review-pagination',
		url: '/api/storeDetail/reviews',
		params: {
			store_idx: storeIdx,
			sort_type: $('#reviewSort').val() || 'rvs_01'
		},
		pageSize: 3,  // 3개씩 렌더링
		renderItem: function(review) {
			const starWidth = review.score * 20;
			let html = `
				<div class="review-item">
					<div class="review-header">
						<div class="reviewer-info">
							<div class="reviewer-avatar">
								<img src="${review.profile_img_url}" alt="프로필">
							</div>
							<div>
								<div class="reviewer-name">${review.member_name}</div>
								<div class="star-rating">
									<div class="stars-background">★★★★★</div>
									<div class="stars-filled" style="width: ${starWidth}%;">★★★★★</div>
								</div>
							</div>
						</div>
						<div class="review-date">${review.create_at}</div>
					</div>
					<div class="review-content">${review.content}</div>
			`;

			// 리뷰 이미지가 있으면 추가
			if (review.images) {
				const images = review.images.split(',');
				html += '<div class="review-images">';
				images.forEach(function(img) {
					html += `
						<div class="review-image">
							<img src="${img}" alt="리뷰 이미지">
						</div>
					`;
				});
				html += '</div>';
			}

			html += '</div>'; // review-item 종료
			return html;
		},
		emptyMessage: `
			<div class="empty-state">
				<p>등록된 리뷰가 없습니다.</p>
			</div>
		`
	});

	// 첫 페이지 로드
	reviewPagination.loadPage(1);

	// ===================================
	// 함수 정의
	// ===================================

	// 별점 표시
	function displayStars(rating) {
		$starElements.each(function(index) {
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
			$imagePreview.append(previewHtml);
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
});
