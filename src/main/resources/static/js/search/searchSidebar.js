$(function() {
	
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
		updateQueryForSidebar(this); 
	});
	
	//초기화 눌렀을때
	$('.top').on('click','.reset', function() {
		reset(this);
		//다 삭제 했을때 키워드 목록창 가리기 
		hideSelectedDiv();
		//쿼리스트링에서삭제 하느것도 해야함
	});
	
	//모달 열기
	$('.keywords').on('click', '.show-modal', showLocFoodModal);
	
	// 모달닫기
	$(".close-btn").on("click", function() {
		hideLocFoodModal();
		hideDateModal();
	});
	//모달 외에 부분 눌렀을때
	$(window).on("click", function(e) {
	    if (e.target == $("#myModal")[0]) {
			hideLocFoodModal();
		}
	});	
	
	// 모달 적용하기 버튼 
	$("#apply-btn").on("click", function() {
		updateSidebar();
		updateQueryForLocFood();
	    hideLocFoodModal();
	});
	
	// 지역 대분류 선택시 
	$('#main-location').on('click', '.keyword', toggleSubLocation);
	
	
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
	
	$('.select-box').on('change', '#filter',updateQueryForFilter);
	
	$('#search-btn').on('click', buildUrlAndFetchData);
	$('#query').on('keydown', function(event) {
	    if (event.key === 'Enter' || event.which === 13) {
	        buildUrlAndFetchData();
	    }
	});
});

// 모달 상태관리를 용이하게 하기 위한 변수 선언 
let tempFilterState = {
	location: {
		code:[],
		code_label:[]
	},
	food: {
		code:[],
		code_label:[]
	}
}

let filterState = {
	location: {
		code:[],
		code_label:[]
	},
	food: {
		code:[],
		code_label:[]
	}
}

let searchState = {
	loc: [],
	food: [],
	atmosphere: [],
	facility: [],
	personCnt: null,
	price: null,
	sort : null,
	day : null,
	time : null,
	query : null,
	cursor: null,
	limit: null,
	reviewCs:null,
	priceCs:null,
	scoreCs:null
}

let isLoading = false;

function updateQueryForFilter() {
	const filter = $('#filter').val() == '' ? null :  $('#filter').val();
	searchState.sort = filter;
}

function buildUrlAndFetchData() {
	if(isLoading) return;
	isLoading = true;
    const params = new URLSearchParams();
	searchState.query = $('#query').val();
	const values = Object.values(searchState);
	
	//유효성 판별 
    const hasAnyCondition = values.some(value => {
        if (Array.isArray(value)) {
            return value.length > 0;
        }
        return value !== null && value !== ''; 
    });
	
	if(!hasAnyCondition) {
		history.pushState(null, '', '/search');
		searchState.sort = null;
		console.log(searchState.sort);
		alert('검색조건을 입력해주세요!');
		isLoading = false;
		return;
	}
	
	//검색조건 있으면 아래 로직 실행 
    if (searchState.price) params.set('price', searchState.price);
    if (searchState.personCnt) params.set('personCnt', searchState.personCnt);
    if (searchState.sort) {
		params.set('sort', searchState.sort);
	} else {
		params.set('sort', 'order by score desc'); // 정렬기능 기본값 설정 
	}
    if (searchState.day) params.set('day', searchState.day);
    if (searchState.time) params.set('time', searchState.time);
    if (searchState.cursor) params.set('cursor', searchState.cursor);
    if (searchState.limit) {
		params.set('limit', searchState.limit);	
	} else {
		params.set('limit', 10); // 기본값 10개 
	}
    if (searchState.query) params.set('query', $('#query').val());
   	params.set('priceCs', searchState.priceCs);
    params.set('reviewCs', searchState.reviewCs);
    params.set('scoreCs', searchState.scoreCs);
    
    searchState.atmosphere.forEach(v => params.append('atmosphere', v));
    searchState.facility.forEach(v => params.append('facility', v));
    searchState.food.forEach(v => params.append('food', v));
    searchState.loc.forEach(v => params.append('loc', v));

    const baseUrl = "/api/search";
    const finalUrl = `${baseUrl}?${params.toString()}`;
	const displayUrl = `/search?${params.toString()}`;
    console.log("최종 생성된 URL:", finalUrl);
	
	$.ajax({
		url:finalUrl,
		type:"get",
		dataType:"html",
		success:function(res) {
			$('.no-result').hide();
			$('.filter').show();
			alert('호출성공');
			$('.content').append(res);
			history.pushState(null, '', displayUrl);
			console.log(res);
			const cursor = $('.content').children().last().data('cursor');
			const reviewCs = $('.content').children().last().data('review-cursor');
			const priceCs = $('.content').children().last().data('price-cursor');
			const scoreCs = $('.content').children().last().data('score-cursor');
			console.log(cursor);
			searchState.cursor = cursor;
			searchState.priceCs = priceCs;
			searchState.reviewCs = reviewCs;
			searchState.scoreCs = scoreCs;
			isLoading = false;
			
			//불러온 요소 개수 
			// 개수가 limit 보다 작을때? 더이상 불러올 값이 없으므로 다음 호출이 안되게 해야함 
			// 개수가 딱 10개만 불러오고 이후에 값이 없을때? // 원래 결과가 없는 경우랑, 더 불러올 결과가 없는 경우 어케 나누지?  		
			// 검색 조건이 바뀌었을땡 
		},
		error: function(error) {
			console.log(error);
			alert('호출실패');
			isLoading = false;		
		}
	})
}
//사이드바에서 선택한 키워드 쿼리에 적용 
function updateQueryForSidebar(el) {
	// 모달에서 선택된 키워드는 ㄲㅈ 
	const isModalEvent = $(el).closest('.modal-overlay').length > 0;
	if(isModalEvent) return;
	
	const price = $('#price-slider')[0].noUiSlider.get(true);
	const atmosphere = $(el).data('atmosphere');
	const facility = $(el).data('facility');
	
	if(price) {
		searchState.price = price;
	}
    if (atmosphere) {
        const alreadyExists = searchState.atmosphere.includes(atmosphere);
        if (alreadyExists) {
            searchState.atmosphere = searchState.atmosphere.filter(v => v != atmosphere);
        } else {
            searchState.atmosphere.push(atmosphere);
        }
    }

    if (facility) {
        const alreadyExists = searchState.facility.includes(facility);
        if (alreadyExists) {
            searchState.facility = searchState.facility.filter(v => v != facility);
        } else {
            searchState.facility.push(facility);
        }
    }
	
}

//지역,음식 모달에서 선택한 키워드 쿼리에 적용 
function updateQueryForLocFood() {
	searchState.loc = filterState.location.code_label;
	searchState.food = filterState.food.code;
	
}

// 적용하기 버튼 눌러서 모달에 반영 
function updateSidebar() {
	// 선택된 값이 있을 경우 추가할 버튼 
	const selectBtn = `<span class="select show-modal">종류선택</span>`;
	// 임시저장소값을 실제 저장소에 옮김 
	filterState = JSON.parse(JSON.stringify(tempFilterState));
	renderKeyword(filterState.location, $('#locationArea'), $('#sidebar').find('#locReset'), selectBtn);
	renderKeyword(filterState.food, $('#foodArea'), $('#sidebar').find('#foodReset'), selectBtn);
}

function renderKeyword(object, area, resetBtn, selectBtn) {
	console.log(object);
	if(object.code.length == 0 && object.code_label.length == 0) {
		reset(resetBtn);
	} else {
		area.empty();
		object.code.forEach((code,index) => {
			const label = object.code_label[index];
			const el = `<span class="keyword active" data-loc-food="${code}">${label}</span>`
			area.append(el);		
		});
		area.append(selectBtn);
	}
}


// 모달 열기
function showLocFoodModal() {
	$("#myModal").fadeIn(200); 
	tempFilterState = JSON.parse(JSON.stringify(filterState));
	renderModal();
	hideSelectedDiv();
}

// 모달 렌더링
function renderModal() {
	// 선택된 키워드 목록 비우기 
	$('.selectedKeywords').find('.selectedKeyword').detach();
	// 모든 키워드(지역대분류제외) active 효과 지우기 
	$('.locFoodModal').find('.keyword:not(.no-outline)').removeClass('active');
	
	//renderSelectedKeywords 렌더링 해주는 내부 함수 
	const renderSelectedKeywords = (categoryObject, categoryName) => {
		categoryObject.code.forEach((code, index) => {
			const label = categoryObject.code_label[index];
			const selectedKeyword = `<span data-loc-food="${code}" class="selectedKeyword active ${categoryName}" data-loc-food="${code}">
									   ${label}<span class="delete active">&times;</span></span>`;
			console.log(selectedKeyword);
			$('.selectedKeywords').append(selectedKeyword);
		});
	};
	//renderSelectedKeywords 함수를 사용하여 선택된 목록에 렌더링 시키기 
	renderSelectedKeywords(tempFilterState.location, 'location');
	renderSelectedKeywords(tempFilterState.food, 'food');
	
	//액티브 효과 주기 
	$('.locFoodModal').find('.keyword:not(.no-outline)').each(function(_,key) {
		if(tempFilterState.food.code.includes($(key).data('loc-food'))) $(this).addClass('active');
		if(tempFilterState.location.code.includes($(key).data('loc-food'))) $(this).addClass('active');
	});
}

// 지역 대분류 클릭시 소분류 토글
function toggleSubLocation() {
	const mainValue = $(this).data('loclarge'); // 이 값으로 보여줄 div 판별
	const subContainer = $('#sub-location-container').find('.subLoc-container');
	// 일단 모든 소분류 div 숨기기
	subContainer.hide();
	$('#sub-location-container .subLoc-container').each(function() {
		const subValue = $(this).data('loclarge');
		if (mainValue == subValue) $(this).show();
	}); 
	
}

//(모달)선택한 키워드에 반영하기 
function keywordUpdate(el) {
	const code_label = $(el).text(); //선택한 키워드의 label, code 
	const code = $(el).data('loc-food'); //선택한 키워드의 code 
	const keywordClass = $(el).attr('class').split(' ')[1]; //keyword food or location 클래스
	
	//사이드바에서 선택한 것이 아닌 모달에서 선택된것만 반영되게함
	const isModalEvent = $(el).closest('.modal-overlay').length > 0;
	if(!isModalEvent) return;
	
	//지역 대분류선택은 키워드 반영 막음
	const isMainLocation = $(el).closest('.keywords').attr('id') == 'main-location' ;
	if(isMainLocation) return;
	
	//지역, 음식 별로 초기화 기능을 위해 food/location 클래스를 줘서 구분하여 반영시킴
	const selectedKeyword = `<span data-loc-food="${code}" class="selectedKeyword active ${keywordClass}">${code_label}
							 <span class="delete active">&times;</span></span>`
		
	//중복 선택 방어
	let isDetach = false;
	$('.selectedKeywords').children('.selectedKeyword').each(function() {
	let selectedCode = $(this).clone().find('.delete').remove().end().data('loc-food'); 
		if(selectedCode == code) {//이미 선택되어 있어서 다시 해제할때  
			$(this).detach(); //이미 선택된거니 선택된 목록에서 지워야함 
			// 이미 선택된거를 다시 해제 하니 임시 저장소에도 없애야함 
			if(keywordClass == 'location') {
				tempFilterState.location.code = tempFilterState.location.code.filter(loc => loc != code);
				tempFilterState.location.code_label = tempFilterState.location.code_label.filter(loc => loc != code_label);
			}	
			if(keywordClass == 'food') {
				tempFilterState.food.code = tempFilterState.food.code.filter(fod => fod != code);
				tempFilterState.food.code_label = tempFilterState.food.code_label.filter(fod => fod != code_label);
			}
			console.log(tempFilterState);
			isDetach = true;
		}
	});
	if(isDetach) return;
	
	// 선택된 목록에 요소 추가 
	$('.selectedKeywords').append(selectedKeyword);
	
	//tempFilterState 에 저장하기 
	if(keywordClass == 'location') {
		tempFilterState.location.code.push(code);
		tempFilterState.location.code_label.push(code_label);
	}
	if(keywordClass == 'food') {
		tempFilterState.food.code.push(code);
		tempFilterState.food.code_label.push(code_label);
	}
	
	console.log('현재 값');
	console.log(tempFilterState);
}

// 초기화 버튼 실행 함수 
function reset(el) {
	const isNoReset = $(el).closest('.top').hasClass('no-reset');
	const isPrice = $(el).closest('.top').hasClass('price');
	const isLocation = $(el).closest('.parent').hasClass('locReset');
	const isFood = $(el).closest('.parent').hasClass('foodReset');
	const isFacility = $(el).closest('.parent').hasClass('facility');
	const isAtmosphere = $(el).closest('.parent').hasClass('atmosphere');
	const isModalEvent = $(el).closest('.modal-overlay').length > 0;
	
	// ================================
	//   사이드바 부분 
	// ================================
	if(!isModalEvent) { // 사이드바 부분
		if(isNoReset) {//지역 음식 부분만 구분 
			
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
				filterState.location.code = [];
				filterState.location.code_label = [];
				//검색 조건 초기화 
				searchState.loc = [];	
			} 
			if(isFood) {
				$('.food').removeClass('active');
				$('.selectedKeywords').find('.food').detach();	
				// filterState에 반영 로직
				filterState.food.code = [];
				filterState.food.code_label = [];
				//검색 조건 초기화 
				searchState.food = [];
			}
			return;	
		}
		//사이드바에서 가격 부분 리셋 
		if(isPrice) {
			$('#price-slider')[0].noUiSlider.reset();
			searchState.price = null;
		}
		if (isAtmosphere) { // isAtmosphere가 jQuery 객체라면 if (isAtmosphere.length > 0) 권장
		    searchState.atmosphere = [];
		    $(el).closest('.parent').find('.keyword').each(function() {
				console.log('분우기ㅣ111')
		        $(this).removeClass('active');
		    });
		}
		if(isFacility) {
			searchState.facility = [];	
			$(el).closest('.parent').find('.keyword').each(function() {
		        $(this).removeClass('active');
		    });
		}
	}		
	// ================================
	//    모달 부분 
	// ================================
	
	
	if(isModalEvent) {
		// 모달에서 지역 대분류는 리셋방지하고 그외 액티브 효과 없애기 
		$(el).closest('.parent').find('.keyword:not(.no-outline)').removeClass('active');
		//모달에서 초기화 버튼 눌렀을때, 선택된 목록들, 임시 보관소에서도 초기화 시키기 
		if(isLocation) {
			$('.selectedKeywords').find('.location').detach();
			tempFilterState.location.code = [];
			tempFilterState.location.code_label = [];		
		}
		if(isFood) {
			$('.selectedKeywords').find('.food').detach();
			tempFilterState.food.code = [];
			tempFilterState.food.code_label = [];
		}
	}
}

// 선택된 목록에서 개별 키워드 삭제할때 
function deleteKeyword(el) {
	const parentEl = $(el).closest('.selectedKeyword');
	//배열을 돌면서 직접 매치되는것의 요소의 클래스 삭제 -> 백엔드 들어가면, vaule값으로 판별하는걸로 바꾸기
	const code = $(el).closest('.selectedKeyword').clone().find('.delete').remove()      
							 .end().data('loc-food');                       
	const code_label = $(el).closest('.selectedKeyword').clone().find('.delete').remove()      
							 .end().text().trim();                       
	$('.locFoodModal').find('.keyword').each(function() { 
		if($(this).data('loc-food') == code) {
			$(this).removeClass('active');
		}
	});
	parentEl.detach();
	const isLocation = $(el).closest('.selectedKeyword').hasClass('location');
	if(isLocation) {
		tempFilterState.location.code = tempFilterState.location.code.filter(loc => loc != code);
		tempFilterState.location.code_label = tempFilterState.location.code_label.filter(loc => loc != code_label);
	}	
	if(!isLocation) {
		tempFilterState.food.code = tempFilterState.food.code.filter(fod => fod != code);
		tempFilterState.food.code_label = tempFilterState.food.code_label.filter(fod => fod != code_label);
	}
	//개별로 다 삭제 했을때 키워드 목록창 가리기 
	hideSelectedDiv();
}

function hideSelectedDiv() {
	//개별로 다 삭제 했을때 키워드 목록창 가리기 
	let childCnt = $('.selectedKeywords').find('.selectedKeyword').length;
	if(!childCnt) $('.footer-modal').hide();
}

// 모달에서 휴지통 버튼 눌렀을때: 선택된 키워드 전부 삭제, active 효과 다 지우기 
function deleteAll() {
	$('.selectedKeywords').find('.selectedKeyword').detach();
	$('.locfood-content').find('.active:not(.no-outline)').removeClass('active');
	// 임시 보관소에 값들 지우기
	emptyTempFilterState();
}

// 모달 닫기
function hideLocFoodModal () {
	emptyTempFilterState();
	$("#myModal").fadeOut(200);
}

function emptyTempFilterState() {
	tempFilterState.food.code = [];
	tempFilterState.food.code_label = [];
	tempFilterState.location.code = [];
	tempFilterState.location.code_label = [];
}

function priceUpdate(el) {
	let isPrice = $(el).hasClass('priceKeyword');
	let isActive = $(el).hasClass('active');
	if(!isPrice) return;
	if($(el).hasClass(0) && isActive) $('#price-slider')[0].noUiSlider.set([0,10]);
	if($(el).hasClass(100000) && isActive) $('#price-slider')[0].noUiSlider.set([10,20]);
	if($(el).hasClass(200000) && isActive) $('#price-slider')[0].noUiSlider.set([20,30]);
	if($(el).hasClass(300000) && isActive) $('#price-slider')[0].noUiSlider.set([30,40]);
	if($(el).hasClass(400000) && isActive) $('#price-slider')[0].noUiSlider.set([40,40]);
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
	    start: [0, 400000],
	    connect: true,
	    step: 10000,
	    range: {
	        'min': 0,
	        'max': 400000
	    },
		format: {
	        // [수정] 값을 10000으로 나눠서 표시
	        to: function(value) {
	            return Math.round(value / 10000) + '만원'; 
	        },
	        // [수정] 표시된 값에서 10000을 곱해서 내부 값으로 변환
	        from: function(value) {
	            return Number(value.replace('만원', '')) * 10000;
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
		if (cValues[0] == 0 && cValues[1] == 100000) priceToggle(cValues[0]);
		else if (cValues[0] == 100000 && cValues[1] == 200000) priceToggle(cValues[0]);
		else if (cValues[0] == 200000 && cValues[1] == 300000) priceToggle(cValues[0]);
		else if (cValues[0] == 300000 && cValues[1] == 400000) priceToggle(cValues[0]);
		else if (cValues[0] == 400000 && cValues[1] == 400000) priceToggle(cValues[0]);
		else $('.priceKeyword').removeClass('active');
	});
	
	$priceSlider[0].noUiSlider.on('change', function() {
		searchState.price = $priceSlider[0].noUiSlider.get(true);
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