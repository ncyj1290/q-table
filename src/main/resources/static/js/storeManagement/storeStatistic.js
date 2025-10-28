$(function(){

	/* =========================================================== */
	/* 차트 초기 요소들 */
	/* =========================================================== */
	/* 차트 담는 변수 */
	let chart = null;
	
	/* 차트 타입(모양) */
	let chartType = "bar";
	
	/* 차트에 넣을 라벨, 값 */
	let labels;
	let values;

	/* Chart UI */
	let $s_canvas = $("#s_canvas");
	
	/* 초기 값으로 그래프 작성 */
	initCanvas();

	
	/* =========================================================== */
	/* 통계 선택 상자 */
	/* =========================================================== */
	let $statistic = $("#statistic_value");
	/* 통계 선택 상자 동작 */	
	$statistic.on("change", function(){
		
		$.ajax({
			url: "/select_store_statistic_data",
			type:"get",
			data:{
				st_cat: $statistic.val()
			},
			dataType:"json",
			
			success: function(res){
				let data = Array.isArray(res)? res : [];
				labels = data.map(r => r.mth);
				values = data.map(r => Number(r.month_count));
				drawCanvas();
			},
			
			error:function(){
				console.log("Fail to load Statistic Data");
			}
		});
	});
	
	/* =========================================================== */
	/* 그래프 모양 선택 버튼 */
	/* =========================================================== */
	$typeButtons = $(".type-bt").on("click", function(){
		
		$typeButtons.not(this).removeClass("active");
		$(this).toggleClass("active");
		
		chartType = $('.type-bt.active').data('type');
		
		drawCanvas();		
	});

	/* =========================================================== */
	/* Functions */
	/* =========================================================== */
	/* 초기 통계 캔버스 세팅 */
	function initCanvas(){
		
		 const $initData = $('#init_statistic');
		 let data = []; 
		 
		 if ($initData.length) {
		   const raw = $.trim($initData.text()); 
		   if (raw) {
		     try { data = JSON.parse(raw); } 
		     catch (e) { console.error('init_statistic parse error:', e, raw); }
		   }
		}
		
		labels = data.map(r => r.mth);
		values = data.map(r => Number(r.month_count));
		
		drawCanvas();
	}
	
	
	/* Canvas 작성 함수 */
	function drawCanvas(){
		
		let infoLabel = $('#statistic_value option:selected').text();
		
		/* 기존 작성된 Chart 있으면 갱신 */
		if(chart){
			chart.data.datasets[0].type = chartType;
			chart.data.datasets[0].labels = labels;
			chart.data.datasets[0].data = values;
			chart.data.datasets[0].labels = infoLabel;
			chart.update();
			return chart;
		}
		
		/* 없으면 새로 생성 */
		chart = new Chart($s_canvas, {
		  type: chartType,
		  data: {
		    labels: labels,
		    datasets: [
		      {
		        label: $('#statistic_value option:selected').text(),
		        data: values,
				backgroundColor: '#F68686'
		      },
		    ]
		  },
		  
		  options: {
	          scales: {
	              y: {
					title:{
						display: true,
						text: "횟수"
					},
					ticks: {
					    callback: (v) => Number.isInteger(v) ? v : null // 소수 라벨 숨김
					 }
					 
	              },
				  x:{
					title:{
						display: true,
						text:"날짜"
					}	
				  }
	          }
	      }
		  
		});
		
		return chart;
	}

});






