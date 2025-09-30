$(function() {
	console.log('searchSidebar js 연동');
	initializePriceSlider();
	
	//키워드들 클릭했을때
	$('.selectedKeywords:not(.no-toggle)').on('click', '.keyword', function() {
		toggleActive(this);
		//쿼리스트링 반영하는것도 짜야함 
	});
	
	//초기화 눌렀을때
	$('.top').on('click','.reset', function() {
		removeActive(this);
		//쿼리스트링에서삭제 하느것도 해야함
	});
});

function removeActive(el) {
	let isNoReset = $(el).closest('.top').hasClass('no-reset');
	if(isNoReset) {
		const parentEl = $(el).closest('section.part').find('.selectedKeywords');
		parentEl.empty();
		parentEl.append(`<span class="dashed-box">+ 키워드를 선택해주세요</span>`);
		$(el).closest('section.part').find('.select').hide();
		return;	
	}
	$(el).closest('section.part').find('.keyword').removeClass('active');
}

function toggleActive (el) {
	const parentEl = $(el).closest('.selectedKeywords');
	if(parentEl.hasClass('single-choice')) $(el).siblings('.keyword').removeClass('active');
	$(el).toggleClass('active');
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
	// $priceSlider[0].noUiSlider는 라이브러리 API에 접근하는 공식적인 방법입니다.
	$priceSlider[0].noUiSlider.on('update', function(values) {
	    // 4. jQuery의 .html() 메소드를 사용해 값을 업데이트합니다.
	    $sliderValues.html(values.join(' ~ '));
	});
}