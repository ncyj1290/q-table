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
	
	// 상세 페이지에서 뒤로가기했을때, searchState 값이 없어져서 버그 나던 문제 해결
	const reviewCs = $('.result').last().data('review-cursor');
	const priceCs = $('.result').last().data('price-cursor');
	const scoreCs = $('.result').last().data('score-cursor');
	const cursor = $('.result').last().data('cursor');
	const hasNext = $('.result').last().data('hasnext');
	if(hasNext) searchState.hasNext = hasNext;
	if(reviewCs) searchState.reviewCs = reviewCs;
	if(scoreCs) searchState.scoreCs = scoreCs;
	if(cursor) searchState.cursor = cursor;
	if(priceCs) searchState.priceCs = priceCs;
	
	// 옵저버 
	let isLoading = false;
	const loader = $('#loader');
	const observerCallback = (entries, observer) => {
		const entry = entries[0];
		console.log('작동은 하니')
		console.log(entry.isIntersecting)
		console.log(!isLoading)
		console.log(searchState.hasNext)
//		&& searchState.hasNext
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
		//쿼리 파라미터 값 셋팅, 셋팅된 값으로 ajax 호출, 호출후 렌더링, 호출한 주소로 url 바꾸기(히스토리.푸쉬스테이트)
		observer.unobserve(loader[0]);
		const params = new URLSearchParams(); // 파라미터 셋팅을 위한 인스턴스 생성 
		searchState.query = $('#query').val(); // 사용자 입력값 가져오기 
		const values = Object.values(searchState); // 유효성 판별을 위한 값 추출 
		
		//유효성 판별 
	    const hasAnyCondition = values.some(value => {
	        if (Array.isArray(value)) {
	            return value.length > 0;
	        }
	        return value !== null && value !== ''; 
	    });
		
		// 입력값 아무것도 없을때 리턴 
		if(!hasAnyCondition) {
			history.pushState(null, '', '/search');
			searchState.sort = null;
			console.log(searchState.sort);
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
				$('.filter').show();
				const reviewCs = $(res).last().last().data('review-cursor');
				const priceCs = $(res).last().last().data('price-cursor');
				const scoreCs = $(res).last().last().data('score-cursor');
				const cursor = $(res).last().last().data('cursor');
				const hasNext = $(res).last().last().data('hasnext');
//				displayUrl = displayUrl + '&hasNext=' + hasNext;
				$('.content').append(res);
				history.pushState(null, '', displayUrl);
				searchState.cursor = cursor;
				searchState.priceCs = priceCs;
				searchState.reviewCs = reviewCs;
				searchState.scoreCs = scoreCs;
				searchState.hasNext = hasNext;
				if($(res).hasClass('no-result')) return;
				if(hasNext) {
					observer.observe(loader[0]);
				} else {
					noResult();
				}
				isLoading = false;
				//불러온 요소 개수 
				// 리스트 개수가 11개 일때 마지막 값은 지우고 플래그 전달 더 있다는 -> 더있다. 불러오는 함수 계속 실행 
				// 리스트 개수가 10개 이하일때 마지막값 안지우고 플래그 전달 이제 없다는  -> 더 없다. 불러오는 함수 리턴 해서 실행막기 		
				// 검색 조건이 바뀌었을땡 
			},
			error: function(error) {
				console.log(error);
				alert('호출실패');
				isLoading = false;		
			}
		})	
		
	}
	
	
	function loadMoreItems() { // 무한 스크롤 로드 
		if(isLoading) return; // 로딩중 ㄲㅈ 
		
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
//				displayUrl = displayUrl + '&hasNext=' + hasNext;
				$('.content').append(res);
				if(hasNext) {
	                observer.observe(loader[0]);
	            } else {
					observer.unobserve(loader[0]);
	                loader.hide();
					noResult();
	            }
				history.pushState(null, '', displayUrl);
				searchState.cursor = cursor;
				searchState.priceCs = priceCs;
				searchState.reviewCs = reviewCs;
				searchState.scoreCs = scoreCs;
				searchState.hasNext = hasNext;
				isLoading = false;
				
				//불러온 요소 개수 
				// 리스트 개수가 11개 일때 마지막 값은 지우고 플래그 전달 더 있다는 -> 더있다. 불러오는 함수 계속 실행 
				// 리스트 개수가 10개 이하일때 마지막값 안지우고 플래그 전달 이제 없다는  -> 더 없다. 불러오는 함수 리턴 해서 실행막기 		
				// 검색 조건이 바뀌었을땡 
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

function noResult () {
	const el = `<hr> 
				<div id="scrollTop">
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

