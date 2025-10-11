/**
 * AJAX 기반 페이지네이션 공통 모듈
 *
 * 사용 예시:
 * new Pagination({
 *     dataContainer: '#review-list',        // 데이터가 렌더링될 컨테이너
 *     paginationContainer: '#pagination',   // 페이지네이션 버튼이 렌더링될 컨테이너
 *     url: '/api/store/reviews',
 *     params: { store_idx: 1 },
 *     pageSize: 10,
 *     renderItem: function(item) {
 *         return `<div class="review">${item.content}</div>`;
 *     }
 * });
 */

class Pagination {
	constructor(options) {
		if (!options.dataContainer) {  // 필수 옵션 검증
			throw new Error('dataContainer 옵션은 필수입니다.');
		}
		if (!options.paginationContainer) {
			throw new Error('paginationContainer 옵션은 필수입니다.');
		}
		if (!options.url) {
			throw new Error('url 옵션은 필수입니다.');
		}
		if (!options.renderItem) {
			throw new Error('renderItem 옵션은 필수입니다.');
		}

		this.dataContainer = $(options.dataContainer);  // 데이터가 렌더링될 컨테이너
		this.paginationContainer = $(options.paginationContainer);  // 페이지네이션 버튼이 렌더링될 컨테이너

		this.url = options.url;
		this.params = options.params || {};
		this.pageSize = options.pageSize || 10;
		this.renderItem = options.renderItem;
		this.emptyMessage = options.emptyMessage || '데이터가 없습니다.';
		this.onPageChange = options.onPageChange || null;
		this.onError = options.onError || null;

		this.currentPage = 1;  // 상태 관리
		this.totalPages = 1;
		this.totalCount = 0;
		this.isLoading = false;

		this.loadPage(1);  // 초기 로드
	}

	// 페이지 데이터 로드
	loadPage(page) {
		if (this.isLoading) return;

		this.isLoading = true;
		this.showLoading();

		const self = this;

		$.ajax({
			url: this.url,
			type: 'GET',
			data: {
				...this.params,
				page: page,
				size: this.pageSize
			},
			success: function(response) {
				self.handleSuccess(response, page);
			},
			error: function(xhr) {
				self.handleError(xhr);
			},
			complete: function() {
				self.isLoading = false;
				self.hideLoading();
			}
		});
	}

	// 성공 응답 처리
	handleSuccess(response, page) {
		const data = response.content || response.data || [];  // PageResponse 형식으로 응답 받음
		const totalCount = response.totalCount || 0;
		const totalPages = response.totalPages || 1;

		this.currentPage = page;  // 상태 업데이트
		this.totalCount = totalCount;
		this.totalPages = totalPages;

		this.renderContent(data);  // 컨텐츠 렌더링
		this.renderPagination();  // 페이지네이션 UI 렌더링

		if (this.onPageChange) {  // 콜백 실행
			this.onPageChange(data, page);
		}
	}

	// 에러 처리
	handleError(xhr) {
		console.error('페이지 로드 실패:', xhr);

		let errorMessage = '데이터를 불러오는데 실패했습니다.';

		if (xhr.status === 404) {
			errorMessage = '요청한 페이지를 찾을 수 없습니다.';
		} else if (xhr.status === 500) {
			errorMessage = '서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.';
		} else if (xhr.status === 0) {
			errorMessage = '네트워크 연결을 확인해주세요.';
		}

		if (this.onError) {
			this.onError(xhr, errorMessage);
		} else {
			this.dataContainer.html(`<div class="error-message">${errorMessage}</div>`);
		}
	}

	// 로딩 표시
	showLoading() {
		this.dataContainer.addClass('loading');
	}

	// 로딩 숨김
	hideLoading() {
		this.dataContainer.removeClass('loading');
	}

	// 컨텐츠 렌더링
	renderContent(data) {
		if (data.length === 0) {
			this.dataContainer.html(this.emptyMessage);
			return;
		}

		const html = data.map(item => this.renderItem(item)).join('');
		this.dataContainer.html(html);
	}

	// 페이지네이션 UI 렌더링
	renderPagination() {
		if (this.totalPages <= 1) {
			this.paginationContainer.html('');
			return;
		}

		const currentPage = this.currentPage;
		const totalPages = this.totalPages;
		const pageGroupSize = 10;  // 한 번에 보여줄 페이지 수

		const currentGroup = Math.ceil(currentPage / pageGroupSize);  // 현재 페이지 그룹 계산
		const startPage = (currentGroup - 1) * pageGroupSize + 1;
		const endPage = Math.min(startPage + pageGroupSize - 1, totalPages);

		let html = '<div class="page-selector-layout"><div class="d-flex flex-row justify-content-center">';

		// 첫 페이지 버튼
		if (currentPage > 1) {
			html += `<button class="page-arrow" data-page="1" title="첫 페이지">&laquo;</button>`;
		} else {
			html += `<button class="page-arrow" disabled title="첫 페이지">&laquo;</button>`;
		}

		// 이전 페이지 버튼
		if (currentPage > 1) {
			html += `<button class="page-arrow" data-page="${currentPage - 1}" title="이전 페이지">&lt;</button>`;
		} else {
			html += `<button class="page-arrow" disabled title="이전 페이지">&lt;</button>`;
		}

		// 페이지 번호 버튼
		for (let i = startPage; i <= endPage; i++) {
			if (i === currentPage) {
				html += `<button class="page-selector" disabled data-page="${i}">${i}</button>`;
			} else {
				html += `<button class="page-selector" data-page="${i}">${i}</button>`;
			}
		}

		// 다음 페이지 버튼
		if (currentPage < totalPages) {
			html += `<button class="page-arrow" data-page="${currentPage + 1}" title="다음 페이지">&gt;</button>`;
		} else {
			html += `<button class="page-arrow" disabled title="다음 페이지">&gt;</button>`;
		}

		// 마지막 페이지 버튼
		if (currentPage < totalPages) {
			html += `<button class="page-arrow" data-page="${totalPages}" title="마지막 페이지">&raquo;</button>`;
		} else {
			html += `<button class="page-arrow" disabled title="마지막 페이지">&raquo;</button>`;
		}

		html += '</div></div>';

		this.paginationContainer.html(html);
		this.bindPaginationEvents();  // 이벤트 바인딩
	}

	// 페이지네이션 이벤트 바인딩
	bindPaginationEvents() {
		const self = this;

		this.paginationContainer.find('.page-selector:not(:disabled), .page-arrow:not(:disabled)').on('click', function() {
			const page = parseInt($(this).data('page'));
			self.loadPage(page);

			$('html, body').animate({  // 페이지 이동 시 스크롤 최상단으로
				scrollTop: self.dataContainer.offset().top - 100
			}, 300);
		});
	}

	// 파라미터 업데이트 및 새로고침
	updateParams(newParams) {
		this.params = { ...this.params, ...newParams };
		this.loadPage(1);
	}

	// 새로고침 (현재 페이지 유지)
	refresh() {
		this.loadPage(this.currentPage);
	}

	// 첫 페이지로 이동
	reset() {
		this.loadPage(1);
	}

	// 현재 페이지 번호 반환
	getCurrentPage() {
		return this.currentPage;
	}

	// 전체 페이지 수 반환
	getTotalPages() {
		return this.totalPages;
	}
}
