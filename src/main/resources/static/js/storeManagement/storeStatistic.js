$(function(){
	
	/* Chart Type */
	let chartType = "bar";
//	let chartType = "doughnut";

	/* Chart UI */
	let $chart_ui = $("#chart_ui");
	
	/* 통계 선택 상자 */
	let $statistic = $("#statistic_value");
	/* 통계 선택 상자 동작 */	
	$statistic.on("change", function(){
		console.log($(this).val());
	});

	
	let chart = new Chart($chart_ui, {
	  type: chartType,
	  data: {
	    labels: ['2020', '2021', '2022', '2023', '2024', '2025'],
	    datasets: [
	      {
	        label: $('#statistic_value option:selected').text(),
	        data: [10,20,30,40,50,60],
			backgroundColor: '#F68686'
	      },
	    ]
	  },
	  
	  options: {
          scales: {
              y: {
				title:{
					display: true,
					text: "예약 수"
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
	
});