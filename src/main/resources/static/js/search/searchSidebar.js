$(function() {
	
//	console.log(slider.noUiSlider.get(true)); 값가져오기
	console.log('searchSidebar js 연동');
	
	//슬라이더 라이브러리 초기화
	initializePriceSlider();
	
	//키워드들 클릭했을때
	$('.keywords:not(.no-toggle)').on('click', '.keyword', function() {
		$('.footer-modal').show(); 
		toggleActive(this);//active 토글
		priceUpdate(this);//가격 키워드 반영
		keywordUpdate(this);
		hideSelectedDiv();
		//쿼리스트링 반영하는것도 짜야함 
	});
	
	//초기화 눌렀을때
	$('.top').on('click','.reset', function() {
		reset(this);
		//다 삭제 했을때 키워드 목록창 가리기 
		hideSelectedDiv();
		//쿼리스트링에서삭제 하느것도 해야함
	});
	
	//모달 열기
	$('.keywords').on('click', '.show-modal', showModal);
	
	// 모달닫기
	$(".close-btn").on("click", function() {
		hideModal();
//		deleteAll();
	});
	//모달 외에 부분 눌렀을때
	$(window).on("click", function(e) {
	    if (e.target == $("#myModal")[0]) {
			hideModal();
//			deleteAll();
		}
	});	
	
	// 모달 적용하기 버튼 
	$("#apply-btn").on("click", function() {
	    hideModal();
		updateSidebar();
	});
	
	//개별 키워드 삭제버튼 
	$('.selectedKeywords').on('click', '.delete', function(){
		deleteKeyword(this);
		//다 삭제 했을때 키워드 목록창 가리기 
		hideSelectedDiv();
	})
	
	//휴지통 버튼 눌렀을때
	$('.selectedKeywords').on('click', '#delete-all-btn', function(){
		deleteAll();
		//다 삭제 했을때 키워드 목록창 가리기 
		hideSelectedDiv();
	})
	
});

// 모달 상태관리를 용이하게 하기 위한 변수 선언 
let tempFilterState = {
	location: [],
	food: []
}

let filterState = {
	location: [],
	food: []
}


function updateSidebar() {
	const selectBtn = `<span class="select show-modal">종류선택</span>`;
	filterState = JSON.parse(JSON.stringify(tempFilterState));
	console.log(filterState);
	const locKeywords = $('.selectedKeyword:not(.food)').map(function() {
	  return $(this).clone().find('.delete').remove().end().text().trim();
	}); 
	const foodKeywords = $('.selectedKeyword:not(.location)').map(function() {
	  return $(this).clone().find('.delete').remove().end().text().trim();
	}); 
	// 나중에 value 값도 추가 해야함
	$('#locationArea').empty();
	$('#foodArea').empty();
	
	renderKeyword(locKeywords, $('#locationArea'), $('#sidebar').find('#locReset'), selectBtn);
	renderKeyword(foodKeywords, $('#foodArea'), $('#sidebar').find('#foodReset'), selectBtn);
}

function renderKeyword(keywords, area, resetBtn, selectBtn) {
	if(!keywords.length) {
		reset(resetBtn);
	} else {
		keywords.map(function(_,keyword) {
			const el = `<span class="keyword active">${keyword}</span>`
			area.append(el);		
		});
		area.append(selectBtn);
	}
}

// 초기화 버튼 실행 함수 
function reset(el) {
	const isNoReset = $(el).closest('.top').hasClass('no-reset');
	const isPrice = $(el).closest('.top').hasClass('price');
	const isLocation = $(el).closest('.parent').hasClass('locReset');
	const isFood = $(el).closest('.parent').hasClass('foodReset');
	
	//사이드바에서 초기화 눌렀을때 초기화 되지 말아야할 부분
	if(isNoReset) {
		//사이드바에서 지역, 음식 부분 초기화 버튼 클릭시 동작 
		const parentEl = $(el).closest('section.part').find('.keywords');
		parentEl.empty();
		parentEl.append(`<span class="dashed-box show-modal">+ 키워드를 선택해주세요</span>`);
		$(el).closest('section.part').find('.select').hide();
		
		//사이드바에서 초기화 누르면 모달에서 키워드, 선택된 목록에도 반영하기
		if(isLocation) {
			$('.location').removeClass('active');
			$('.selectedKeywords').find('.location').detach();
			// filterState에 반영 로직 
			filterState.location = [];
		} 
		if(isFood) {
			$('.food').removeClass('active');
			$('.selectedKeywords').find('.food').detach();	
			// filterState에 반영 로직
			filterState.food = [];
		}
		return;	
	}
	//사이드바에서 가격 부분 리셋 
	if(isPrice) $('#price-slider')[0].noUiSlider.reset();
	// 모달에서 지역 대분류는 리셋방지하고 그외 액티브 효과 없애기 
	$(el).closest('.parent').find('.keyword:not(.no-outline)').removeClass('active');
	//모달에서 초기화 버튼 눌렀을때, 선택된 목록들, 임시 보관소에서도 초기화 시키기 
	if(isLocation) {
		$('.selectedKeywords').find('.location').detach();
		tempFilterState.location = [];		
	}
	if(isFood) {
		$('.selectedKeywords').find('.food').detach();
		tempFilterState.food = [];	
	}
	console.log(tempFilterState);
}

function hideSelectedDiv() {
	//개별로 다 삭제 했을때 키워드 목록창 가리기 
	let childCnt = $('.selectedKeywords').find('.selectedKeyword').length;
	if(!childCnt) $('.footer-modal').hide();
}

function deleteKeyword(el) {
	const parentEl = $(el).closest('.selectedKeyword');
	//배열을 돌면서 직접 매치되는것의 요소의 클래스 삭제 -> 백엔드 들어가면, vaule값으로 판별하는걸로 바꾸기
	const keywordText = $(el).closest('.selectedKeyword').clone().find('.delete').remove()      
							 .end().text().trim();                       
	$('.keywords').find('.keyword').each(function() { 
		if($(this).text().trim() == keywordText) {
			$(this).removeClass('active');
		}
	});
	parentEl.detach();
	const isLocation = $(el).closest('.selectedKeyword').hasClass('location');
	isLocation ? tempFilterState.location = tempFilterState.location.filter(loc => loc != keywordText):
				 tempFilterState.food = tempFilterState.food.filter(food => food != keywordText);
	console.log(tempFilterState);  

	//개별로 다 삭제 했을때 키워드 목록창 가리기 
	hideSelectedDiv();
}

//(모달)선택한 키워드에 반영하기 
function keywordUpdate(el) {
	const keyword = $(el).text(); //선택한 키워드
	const keywordClass = $(el).attr('class').split(' ')[1]; //keyword food or location 클래스
	//초기화 기능을 위해 food/location 클래스를 줘서 구분하여 반영시킴
	const selectedKeyword = `<span class="selectedKeyword active ${keywordClass}">${keyword}
							 <span class="delete active">&times;</span></span>`
	
	
	//모달에서 선택된것만 반영되게함
	const isModalEvent = $(el).closest('.modal-overlay').length > 0;
	if(!isModalEvent) return;
	
	//지역 대분류선택은 키워드 반영 막음
	const isMainLocation = $(el).closest('.keywords').attr('id') == 'main-location' ;
	if(isMainLocation) return;
	
	//중복 선택 방어
	let isDetach = false;
	$('.selectedKeywords').children('.selectedKeyword').each(function() {
	let keywordText = $(this).clone().find('.delete').remove().end().text().trim(); 
		if(keywordText == keyword) {
			//이미 선택된거는 목록에서 지우기 
			$(this).detach();
			//
			if(keywordClass == 'location') tempFilterState.location = tempFilterState.location.filter(loc => loc != keyword);
			if(keywordClass == 'food') tempFilterState.food =tempFilterState.food.filter(food => food != keyword);
			console.log(tempFilterState);
			isDetach = true;
		}
	});
	if(isDetach) return;
	
	// 선택된 목록에 요소 추가 
	$('.selectedKeywords').append(selectedKeyword);
	
	//tempFilterState 에 저장하기 
	if(keywordClass == 'location') tempFilterState.location.push(keyword);
	if(keywordClass == 'food') tempFilterState.food.push(keyword);
	console.log(tempFilterState);
}

// 휴지통 버튼 눌렀을때: 선택된 키워드 전부 삭제, active 효과 다 지우기 
function deleteAll() {
	$('.selectedKeywords').find('.selectedKeyword').detach();
	$('.locfood-content').find('.active:not(.no-outline)').removeClass('active');
	// 임시 보관소에 값들 지우기
	tempFilterState.food = [];
	tempFilterState.location = [];
	console.log(tempFilterState);
}

function renderModal() {
		
}

// 모달 열기
function showModal() {
	$("#myModal").fadeIn(200); 
	tempFilterState = JSON.parse(JSON.stringify(filterState));
	hideSelectedDiv();
}

// 모달 닫기
function hideModal () {
	$("#myModal").fadeOut(200);
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

function toggleActive (el) {
	const parentEl = $(el).closest('.keywords');
	const isMainLocation = parentEl.attr('id') == 'main-location' ;
	if(isMainLocation) { //지역대분류는 선택 아예 해제하는거 없게
		$(el).siblings('.keyword').removeClass('active');
		$(el).addClass('active');
		return;
	}
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

function priceToggle(minValue) {
	$("."+ minValue).addClass('active');
}