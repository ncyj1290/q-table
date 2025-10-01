$(function() {
	
//	console.log(slider.noUiSlider.get(true)); 값가져오기
	console.log('searchSidebar js 연동');
	
	//슬라이더 라이브러리 초기화
	initializePriceSlider();
	
	//키워드들 클릭했을때
	$('.selectedKeywords:not(.no-toggle)').on('click', '.keyword', function() {
		toggleActive(this);//active 토글
		priceUpdate(this);//가격 키워드 반영
		//쿼리스트링 반영하는것도 짜야함 
	});
	
	//초기화 눌렀을때
	$('.top').on('click','.reset', function() {
		reset(this);
		//쿼리스트링에서삭제 하느것도 해야함
	});
	
	//모달 열기
	$('.selectedKeywords').on('click', '.dashed-box', showModal);
	//모달닫기 
	$('.modal-overlay, .close-btn').on('click', function(e){
		if ($(e.target).closest('.modal-content').length) return; 
		$('#myCustomModal').removeClass('is-active');
		$('#myCustomModal').addClass('is-hidden');
	})
});

function showModal() {
	$('#myCustomModal').removeClass('is-hidden');
	$('#myCustomModal').addClass('is-active');
}
function priceUpdate(el) {
	let isPrice = $(el).hasClass('priceKeyword');
	let isActive = $(el).hasClass('active');
	if(!isPrice) return;
	if($(el).hasClass(0) && isActive) $('#price-slider')[0].noUiSlider.set([0,10]);
	if($(el).hasClass(10) && isActive) $('#price-slider')[0].noUiSlider.set([10,20]);
	if($(el).hasClass(20) && isActive) $('#price-slider')[0].noUiSlider.set([20,30]);
	if($(el).hasClass(30) && isActive) $('#price-slider')[0].noUiSlider.set([30,40]);
	if($(el).hasClass(40) && isActive) $('#price-slider')[0].noUiSlider.set([40,40]);
	if(!isActive) $('#price-slider')[0].noUiSlider.set([0,40]);
}

function reset(el) {
	const isNoReset = $(el).closest('.top').hasClass('no-reset');
	const isPrice = $(el).closest('.top').hasClass('price');
	if(isNoReset) {
		const parentEl = $(el).closest('section.part').find('.selectedKeywords');
		parentEl.empty();
		parentEl.append(`<span class="dashed-box">+ 키워드를 선택해주세요</span>`);
		$(el).closest('section.part').find('.select').hide();
		return;	
	}
	if(isPrice) $('#price-slider')[0].noUiSlider.reset();
	$(el).closest('section.part').find('.keyword').removeClass('active');
}

function toggleActive (el) {
	const parentEl = $(el).closest('.selectedKeywords');
	if(parentEl.hasClass('single-choice')) $(el).siblings('.keyword').removeClass('active');
	$(el).toggleClass('active');
}

function priceToggle(minValue) {
	$("."+ minValue).addClass('active');
}

// noUiSlider 라이브러리 
function initializePriceSlider() {
	// 1. jQuery 셀렉터로 엘리먼트를 선택합니다.
	const $priceSlider = $('#price-slider');
	const $sliderValues = $('#slider-values');
	// 2. noUiSlider를 생성합니다.
	// noUiSlider는 순수 DOM 엘리먼트를 필요로 하므로, jQuery 객체에서 [0]을 사용해 DOM 엘리먼트를 추출합니다.
	noUiSlider.create($priceSlider[0], {
	    start: [0, 40],
	    connect: true,
	    step: 1,
	    range: {
	        'min': 0,
	        'max': 40
	    },
	    format: {
	        to: function(value) {
	            return Math.round(value) + '만원';
	        },
	        from: function(value) {
	            return Number(value.replace('만원', ''));
	        }
	    }
	});
	// 3. 'update' 이벤트를 리스닝합니다.
	$priceSlider[0].noUiSlider.on('update', function(values) {
		const sliderEl = $priceSlider[0].noUiSlider;
        const cValues = sliderEl.get(true);
	    values[0] == '40만원' && values[1] == '40만원' ? $sliderValues.html(values[0]+ '이상')
													 : $sliderValues.html(values.join(' ~ '));
	 //키워드랑 슬라이드 값이랑 동기화 시키기 
		if (cValues[0] == 0 && cValues[1] == 10) priceToggle(cValues[0]);
		else if (cValues[0] == 10 && cValues[1] == 20) priceToggle(cValues[0]);
		else if (cValues[0] == 20 && cValues[1] == 30) priceToggle(cValues[0]);
		else if (cValues[0] == 30 && cValues[1] == 40) priceToggle(cValues[0]);
		else if (cValues[0] == 40 && cValues[1] == 40) priceToggle(cValues[0]);
		else $('.priceKeyword').removeClass('active');
	});
	
	$priceSlider[0].noUiSlider.on('slide', function() {
		const sliderEl = $priceSlider[0].noUiSlider;
		const cValues = sliderEl.get(true);
		// 최소 0~1만원
		if (cValues[0] == 0 && cValues[1] < 1) sliderEl.set([null, 1])
  	});
}