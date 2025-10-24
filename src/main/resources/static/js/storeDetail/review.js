// ===================================
// 변수 선언
// ===================================
// 일반 변수
let selectedRating = 0;  // 선택된 별점 (1-5)
let selectedFiles = [];  // 선택된 이미지 파일 배열
let reviewPagination = null;  // 페이지네이션 인스턴스
let currentStoreIdx = null;  // 현재 선택된 매장 IDX (마이페이지용)

$(function() {
	const $reviewButton = $('.review-controls .positive-button');
	const $reviewModal = $('#reviewModal');
	const $starElements = $('.star');
	const $reviewImages = $('#reviewImages');
	const $imagePreview = $('#imagePreview');
	const $mypageReview = $('.visit-buttons .positive-button.review');

	// URL에서 store_idx 가져오기 (식당상세페이지용)
	const urlParams = new URLSearchParams(window.location.search);
	const storeIdx = urlParams.get('store_idx');

	// ===================================
	// 이벤트 리스너
	// ===================================

	// 리뷰 작성 모달 열기 (식당상세페이지)
	$reviewButton.on('click', function() {
		currentStoreIdx = storeIdx;  // 식당상세페이지의 storeIdx 사용
		$reviewModal.show();
	});

	// 리뷰 작성 모달 열기 (마이페이지)
	$mypageReview.on('click', function() {
		currentStoreIdx = $(this).data('store-idx');  // 버튼의 data-store-idx 사용
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
	
	// 리뷰 작성
	$('#write-review-btn').on('click', function(){
		writeReview();
	});

	// 리뷰 좋아요 토글 
	$('#reviews-list').on('click', '.review-like-btn', function() {
		const $btn = $(this);
		const reviewIdx = $btn.data('review-idx');
		toggleReviewLike(reviewIdx, $btn);
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
		pageSize: 5,  // 5개씩 렌더링
		renderItem: function(review) {
			const starWidth = review.score * 20;
			const isLiked = review.is_liked ? 'liked' : '';
			const likeCount = review.like_count || 0;

			let html = `
				<div class="review-item" data-review-idx="${review.review_idx}">
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

			// 좋아요 버튼 추가 (하단)
			html += `
				<div class="review-footer">
					<button class="review-like-btn ${isLiked}" data-review-idx="${review.review_idx}">
						<i class="far fa-heart"></i>
						<span class="like-count">${likeCount}</span>
					</button>
				</div>
			`;

			html += '</div>'; 
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
	
	// 리뷰 작성
	function writeReview(){
		// store_idx 체크
		if (!currentStoreIdx) {
			alert('매장 정보를 찾을 수 없습니다.');
			return;
		}

		// 별점 체크
		if (selectedRating === 0) {
			alert('별점을 선택해주세요.');
			return;
		}

		// 리뷰 내용
		const reviewContent = $('#reviewText').val().trim();

		const formData = new FormData();

		formData.append("store_idx", currentStoreIdx);  // currentStoreIdx 사용
		formData.append("score", selectedRating);
		formData.append("content", reviewContent);

		// 이미지 추가
	    selectedFiles.forEach(file => {
	        formData.append('images', file);
	    });


		$.ajax({
			url: '/api/storeDetail/reviews',
			type: 'POST',
			data: formData,
			processData: false,
			contentType: false,
			success: function(response){
				handleAjaxSuccess(response, function() {
					alert('리뷰가 등록되었습니다.');

					// 모달창 숨기기
					$reviewModal.hide();

					// 모달창 초기화
					resetModal();

					// 리뷰 목록 새로고침 (식당상세페이지에만 있음 - ajax페이지네이션)
					if (reviewPagination) {
						reviewPagination.loadPage(1);
					} else {
						// 마이페이지에서는 페이지 새로고침
						location.reload();
					}
				}, '리뷰 등록에 실패했습니다.');
			},
			error: function(xhr){
				handleAjaxError(xhr, '리뷰 등록 중 오류가 발생했습니다.');
			}
		})
	}
	
	// 모달 초기화 함수
	function resetModal() {
	    selectedRating = 0;
	    selectedFiles = [];
	    currentStoreIdx = null;  // 매장 IDX 초기화
	    $('#reviewText').val('');
	    $imagePreview.empty();
	    $('#reviewImages').val('');  // 파일 input 초기화
	    displayStars(0);
	}

	// 리뷰 좋아요 토글
	function toggleReviewLike(reviewIdx, $btn) {
		$.ajax({
			url: '/api/storeDetail/reviews/' + reviewIdx + '/like',
			type: 'POST',
			success: function(response) {
				handleAjaxSuccess(response, function(res) {
					// 좋아요 상태 토글
					$btn.toggleClass('liked');

					// 좋아요 수 업데이트
					const $likeCount = $btn.find('.like-count');
					$likeCount.text(res.likeCount);

					// 아이콘 애니메이션
					const $icon = $btn.find('i');
					$icon.addClass('animate-heart');
					setTimeout(() => {
						$icon.removeClass('animate-heart');
					}, 300);
				}, '좋아요 처리 중 오류가 발생했습니다.');
			},
			error: function(xhr) {
				handleAjaxError(xhr, '좋아요 처리 중 오류가 발생했습니다.');
			}
		});
	}
});
