$(function() {
	console.log('search js 연동');
	// .content는 페이지 초기에 렌더링 되므로 여기에 이벤트 위임 
	$('.content').on('click', '.scrap', function() {
		// ajax 호출
		scrapAjax(this); 
		// 비로그인이면 아래 이벤트 막아야함 
		scrapToggle(this);
	});
	
	$('#search-btn').on('click', loadInitialItems);
		$('#query').on('keydown', function(event) {
		    if (event.key === 'Enter' || event.which === 13) {
		        loadInitialItems();
	    }
	});
	
	$('.select-box').on('change', '#filter',loadInitialItems);
	$('.filter').hide();
	// 검색 결과를 조회하고 나서 다시 search 페이지 요청 했을때 결과 보존하기
	const urlParams = new URLSearchParams(window.location.search);
	urlToState(urlParams);
	urlToElement(urlParams);
	
	// 옵저버 
	let isLoading = false;
	const loader = $('#loader');
	const observerCallback = (entries, observer) => {
		const entry = entries[0];
//		if(!searchState.hasNext) showNotification();
		if (entry.isIntersecting && !isLoading) {
			observer.unobserve(loader[0]);
			loadMoreItems();
		} 
	};
	const observerOptions = {
        root: null,      
        rootMargin: '0px',  
        threshold: 0.1    
    };
	
	const observer = new IntersectionObserver(observerCallback, observerOptions);
	if (loader.length > 0) {
    	observer.observe(loader[0]); // jQuery 객체가 아닌 순수 DOM 요소를 전달
    }
	
	function loadInitialItems() {
		if(isLoading) return;
		observer.observe(loader[0]);
		const params = new URLSearchParams(); // 파라미터 셋팅을 위한 인스턴스 생성 
		// 정렬 설정 값 셋팅
		const filter = $('#filter').val() == '' ? null :  $('#filter').val();
		searchState.sort = filter; 
		searchState.query = $('#query').val(); // 사용자 입력값 가져오기 
		const values = Object.values(searchState); // 유효성 판별을 위한 값 추출 
		
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
			alert('검색조건을 입력해주세요!');
			isLoading = false;
			return;
		}
		
		//검색조건 있으면 아래 로직 실행 
	    if (searchState.query) params.set('query', $('#query').val());
	    if (searchState.personCnt) params.set('personCnt', searchState.personCnt);
	    if (searchState.day) params.set('day', searchState.day);
	    if (searchState.time) params.set('time', searchState.time);
	    if (searchState.limit) {
			params.set('limit', searchState.limit);	
		} else {
			params.set('limit', 10); // 기본값 10개 
		}
	    if (searchState.sort) {
			params.set('sort', searchState.sort);
		} else {
			params.set('sort', 'order by score desc'); // 정렬기능 기본값 설정 
		}
	    if (searchState.price) {
			params.set('price', searchState.price);
		} else {
			params.set('price', [0,400000]); // 가격 기본값 설정 
		}
		
	    searchState.atmosphere.forEach(v => params.append('atmosphere', v));
	    searchState.facility.forEach(v => params.append('facility', v));
	    searchState.food.forEach(v => params.append('food', v));
	    searchState.loc.forEach(v => params.append('loc', v));

	    const baseUrl = "/api/search";
	    const finalUrl = `${baseUrl}?${params.toString()}`;
		let displayUrl = `/search?${params.toString()}`;
	    console.log("최종 생성된 URL:", finalUrl);
		
		$.ajax({
			url:finalUrl,
			type:"get",
			dataType:"html",
			success:function(res) {
				$('.no-result').hide();
				$('.content').empty();
				const reviewCs = $(res).last().last().data('review-cursor');
				const priceCs = $(res).last().last().data('price-cursor');
				const scoreCs = $(res).last().last().data('score-cursor');
				const cursor = $(res).last().last().data('cursor');
				const hasNext = $(res).last().last().data('hasnext');
				history.pushState(null, '', displayUrl);
				searchState.cursor = cursor;
				searchState.priceCs = priceCs;
				searchState.reviewCs = reviewCs;
				searchState.scoreCs = scoreCs;
				searchState.hasNext = hasNext;
				$('.content').append(res);
				if($(res).hasClass('no-result')) { // 처음 검색부터 결과가 없음 
					observer.unobserve(loader[0]);
					isLoading = false;
					return;	
				}
				scrollTop();
				$('.filter').show();
				observer.observe(loader[0]);
				if(hasNext) loader.show();
				isLoading = false;
			},
			error: function(error) {
				console.log(error);
				alert('호출실패');
				isLoading = false;		
			}
		})	
		
	}
	
	function loadMoreItems() { // 무한 스크롤 로드 
		if(isLoading || $('.no-result').data('first')) return; 
		
		loader.addClass('visible');
		isLoading = true;
		const params = new URLSearchParams(); // 파라미터 셋팅을 위한 인스턴스 생성 
		
	 	if (searchState.query) params.set('query', searchState.query);
	    if (searchState.personCnt) params.set('personCnt', searchState.personCnt);
	    if (searchState.day) params.set('day', searchState.day);
	    if (searchState.time) params.set('time', searchState.time);
	    if (searchState.limit) {
			params.set('limit', searchState.limit);	
		} else {
			params.set('limit', 10); // 기본값 10개 
		}
	    if (searchState.sort) {
			params.set('sort', searchState.sort);
		} else {
			params.set('sort', 'order by score desc'); // 정렬기능 기본값 설정 
		}
	    if (searchState.price) {
			params.set('price', searchState.price);
		} else {
			params.set('price', [0,400000]); // 가격 기본값 설정 
		}
		
		// 커서 기반 용 파라미터 셋팅 
	    if (searchState.cursor) params.set('cursor', searchState.cursor);
	   	params.set('priceCs', searchState.priceCs);
	    params.set('reviewCs', searchState.reviewCs);
	    params.set('scoreCs', searchState.scoreCs);
	    
	    searchState.atmosphere.forEach(v => params.append('atmosphere', v));
	    searchState.facility.forEach(v => params.append('facility', v));
	    searchState.food.forEach(v => params.append('food', v));
	    searchState.loc.forEach(v => params.append('loc', v));

	    const baseUrl = "/api/search";
	    const finalUrl = `${baseUrl}?${params.toString()}`;
		let displayUrl = `/search?${params.toString()}`;
	    console.log("최종 생성된 URL:", finalUrl);
		
		$.ajax({
			url:finalUrl,
			type:"get",
			dataType:"html",
			success:function(res) {
				const cursor = $(res).last().last().data('cursor');
				const reviewCs = $(res).last().last().data('review-cursor');
				const priceCs = $(res).last().last().data('price-cursor');
				const scoreCs = $(res).last().last().data('score-cursor');
				const hasNext = $(res).last().last().data('hasnext');
				searchState.hasNext = hasNext;
				if($(res).hasClass('no-result')) { 
//					observer.observe(loader[0]);
	                loader.removeClass('visible');
					showNotification("더 이상 불러올 결과가 없습니다!");
					scrollTop();
					isLoading = false;
					return;	
				}
				$('.content').append(res);
				scrollTop();
				if(hasNext) {
	                observer.observe(loader[0]);
	            } else {
					observer.unobserve(loader[0]);
                 	loader.removeClass('visible');
					showNotification("더 이상 불러올 결과가 없습니다!");
	            }
				history.pushState(null, '', displayUrl);
				searchState.cursor = cursor;
				searchState.priceCs = priceCs;
				searchState.reviewCs = reviewCs;
				searchState.scoreCs = scoreCs;
				searchState.hasNext = hasNext;
				isLoading = false;
			},
			error: function(error) {
				console.log(error);
				alert('호출실패');
				isLoading = false;		
			}
		})		
	}
	
	$('.content').on('click','#scrollTop', function() {
		window.scrollTo({
		    top: 0,           // 스크롤을 맨 위(0px)로 이동
		    behavior: 'smooth' // 스크롤을 부드럽게 이동
	  	});
	});
	
});

function urlToElement(urlParams) {
	$('#query').val(urlParams.get('query'));
	if(urlParams.get('sort')){
		$('#filter').val(urlParams.get('sort'));
	} else {
		$('#filter').val('score desc');
	}
}

function urlToState(urlParams) {
	const reviewCs = $('.result').last().data('review-cursor');
	const priceCs = $('.result').last().data('price-cursor');
	const scoreCs = $('.result').last().data('score-cursor');
	const cursor = $('.result').last().data('cursor');
	const hasNext = $('.result').last().data('hasnext');
	const query = urlParams.get('query');
	const personCnt = urlParams.get('personCnt');
	const day = urlParams.get('day');
	const time = urlParams.get('time');
	const price = urlParams.get('price');
	const facility = urlParams.getAll('facility');
	const loc = urlParams.getAll('loc');
	const food = urlParams.getAll('food');
	const limit = urlParams.get('limit');
	const sort = urlParams.get('sort');
	const atmosphere = urlParams.getAll('atmosphere');
	if (hasNext) searchState.hasNext = hasNext;
	if (reviewCs) searchState.reviewCs = reviewCs;
	if (scoreCs) searchState.scoreCs = scoreCs;
	if (cursor) searchState.cursor = cursor;
	if (priceCs) searchState.priceCs = priceCs;
	if (query) searchState.query = query;
	if (personCnt) searchState.personCnt = personCnt;
	if (day) searchState.day = day;
	if (time) searchState.time = time;
	if (price) searchState.price = price;
	if (facility) searchState.facility = facility;
	if (loc) searchState.loc = loc;
	if (food) searchState.food = food;
	if (limit) searchState.limit = limit;
	if (sort) searchState.sort = sort;
	if (atmosphere) searchState.atmosphere = atmosphere;
}


function scrollTop () {
	const el = `<div id="scrollTop">
					<span>맨 위로 가기</span>
					<img src="/img/Q_money.png" alt="맨 위로 스크롤" > 
				</div>`
    $('.content').append(el);
}

function scrapAjax(el) {
	const onScrap = '/img/scrap_full.png';
	const offScrap = '/img/scrap.png';
	const storeIdx = {
		storeIdx : $(el).data('idx')
	}
	
	$.ajax({
		url:"/scrap/toggle",
		type:'post',
		data: storeIdx,
		success: function () {
			//스크랩 취소
			if($(el).attr('src') == onScrap) {
				$(el).attr('src', offScrap)
				showNotification("스크랩 취소 되었습니다");
				return;
			}
			//스크랩
			if($(el).attr('src') == offScrap) {
				$(el).attr('src', onScrap);
				showNotification("스크랩 되었습니다");
				return;
			}
		},
		error: function(xhr, status, error) {
			const responseData = xhr.responseJSON;
	        // 서버가 401 에러를 보냈는지 확인
	        if (xhr.status === 401) {
	            // 3. 로그인 처리
	            if (confirm(responseData.message)) {
	                location.href = responseData.redirectUrl;
	            }
	        } 
	        // 403 (권한 없음) 등 다른 에러 처리
	        else if (xhr.status === 403) {
	             alert("이 게시물을 스크랩할 권한이 없습니다.");
	        }
	        // 5. 그 외 서버 에러
	        else {
	            alert("스크랩 처리 중 오류가 발생했습니다: 관리자에게 문의바랍니다");
	        }
	    }
	});
	
} 

// 알림 표시
function showNotification(message) {
	// 기존 알림 제거
	$('.scrap-notification').remove();

	// 알림 생성
	const $notification = $('<div class="scrap-notification">' + message + '</div>');
	$('body').append($notification);

	// 페이드인
	setTimeout(function() {
		$notification.addClass('show');
	}, 10);

	// 3초 후 페이드아웃 후 제거
	setTimeout(function() {
		$notification.removeClass('show');
		setTimeout(function() {
			$notification.remove();
		}, 300);
	}, 3000);
}

