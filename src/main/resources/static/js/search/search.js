$(function() {
	console.log('search js 연동');
	$('.store-name-scrap').on('click', '.scrap', function() {
		scrap(this);
	});

});

function scrap(el) {
	const onScrap = '/img/scrap_full.png';
	const offScrap = '/img/scrap.png';
	
	//스크랩 취소
	if($(el).attr('src') == onScrap) {
		$(el).attr('src', offScrap)
		return;
	}
	
	//스크랩
	if($(el).attr('src') == offScrap) {
		$(el).attr('src', onScrap)
		return;
	}
	
}

