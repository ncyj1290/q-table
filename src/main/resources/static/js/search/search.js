$(function() {
	console.log('search js 연동');
	// 1. jQuery 셀렉터로 엘리먼트를 선택합니다.
  const $priceSlider = $('#price-slider');
  const $sliderValues = $('#slider-values');

  // 2. noUiSlider를 생성합니다.
  // noUiSlider는 순수 DOM 엘리먼트를 필요로 하므로, jQuery 객체에서 [0]을 사용해 DOM 엘리먼트를 추출합니다.
  noUiSlider.create($priceSlider[0], {
      start: [3, 40],
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
});