$(document).ready(function() {
    $('.restaurant-wrapper').each(function() {
        const $wrapper = $(this);
        const restaurantList = $wrapper.find('.card-list')[0]; // DOM 요소
        const btnLeft = $wrapper.find('.scroll-btn.left')[0];
        const btnRight = $wrapper.find('.scroll-btn.right')[0];

        // 방어 코드
        if (!restaurantList || !btnLeft || !btnRight) return;

        // 버튼 상태 업데이트 함수
        function updateButtons() {
            const maxScroll = restaurantList.scrollWidth - restaurantList.clientWidth;

            // 좌측 버튼
            if (restaurantList.scrollLeft <= 0) {
                $(btnLeft).hide();
            } else {
                $(btnLeft).show();
            }

            // 우측 버튼
            if (restaurantList.scrollLeft >= maxScroll) {
                $(btnRight).hide();
            } else {
                $(btnRight).show();
            }
        }

        // 초기 버튼 상태
        updateButtons();

        // 클릭 이벤트
        $(btnLeft).on('click', function() {
            restaurantList.scrollBy({ left: -400, behavior: 'smooth' });
            setTimeout(updateButtons, 300); // 스크롤 끝난 후 버튼 상태 업데이트
        });

        $(btnRight).on('click', function() {
            restaurantList.scrollBy({ left: 400, behavior: 'smooth' });
            setTimeout(updateButtons, 300); // 스크롤 끝난 후 버튼 상태 업데이트
        });
		
        // 스크롤할 때 버튼 상태 실시간 업데이트 (사용자 드래그 대비)
        $(restaurantList).on('scroll', updateButtons);
		
		
    });
		
});