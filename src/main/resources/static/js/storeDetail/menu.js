let menuPagination = null;
$(function() {
	const urlParams = new URLSearchParams(window.location.search);
	const storeIdx = urlParams.get('store_idx');

	menuPagination = new Pagination({
		dataContainer: '#menu-list',
		paginationContainer: '#menu-pagination',
		url: '/api/storeDetail/menu',
		params: {
			store_idx: storeIdx
		},
		pageSize: 6,
		renderItem: function(menu) {
			const menuImage = menu.menu_image || '/img/logo.png';
			const menuGram = menu.menu_gram ? '(' + menu.menu_gram + 'g)' : '';
			const menuContent = menu.menu_content || ''

			return `
				<div class="menu-item">
					<div class="menu-left">
						<div class="menu-name-row">
							<span class="menu-name">${menu.menu_name}</span>
							<span class="menu-gram">${menuGram}</span>
						</div>
						<span class="menu-price">${menu.price}원</span>
					</div>
					<div class="menu-center">
						<span class="menu-description">${menuContent}</span>
					</div>
					<div class="menu-thumbnail">
						<img src="${menuImage}" alt="${menu.menu_name}">
					</div>
				</div>
			`;
		},
		emptyMessage: `
			<div class="empty-state">
				<p>등록된 메뉴가 없습니다.</p>
			</div>
		`
	});
	menuPagination.loadPage(1);
});