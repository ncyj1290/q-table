// Grid.js 공통 함수
function createGrid(options) {
	const defaultOptions = {
		search: true,
		sort: true,
		pagination: {
			limit: 5 // 기본 페이지당 5개로 설정
		},
		language: {
			'search': { 'placeholder': '🔍 검색...' },
			'pagination': {
				'previous': '이전',
				'next': '다음',
				'showing': '총',
				'results': () => '개 중'
			}
		}
	};

	const finalOptions = { ...defaultOptions, ...options };

	const grid = new gridjs.Grid(finalOptions);

	const wrapper = $(finalOptions.targetId);

	if (wrapper.length) { // 해당 ID를 가진 요소가 페이지에 존재할 때만 실행
		wrapper.empty();
		grid.render(wrapper[0]);
	}
}

$(function() {

	var currentUrl = window.location.pathname;

	$('.nav-treeview .nav-link').each(function() {
		var linkUrl = $(this).attr('href');

		if (currentUrl === linkUrl) {

			// 하위 메뉴 링크 활성화
			$(this).addClass('active');

			// 메뉴의 드롭다운 (menu-open)
			$(this).closest('.nav-item').parent().closest('.nav-item').addClass('menu-open');

			// 부모 메뉴의 링크를 활성화
			$(this).closest('.nav-item').parent().closest('.nav-item').children('.nav-link').addClass('active');
		}
	});
});

$(function () {
    // meta 태그에서 토큰과 헤더 이름 가져오기
    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");

    // jQuery의 모든 AJAX 요청에 자동으로 CSRF 헤더 추가
    $.ajaxSetup({
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        }
    });
});

