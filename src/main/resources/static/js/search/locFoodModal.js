$(function() {
	
	console.log('이거 연동')
	$('#main-location').on('click', '.keyword', getSubLocation);
	
	

});

// 함수 선언

// 키워드 클릭하면 -> 임시 필터에 담겨야함 
// 다른 대분류 옮겨갔다가 다시 올때 임시 필터에 값으로 선택된채로 렌더링 

function getSubLocation () {
//	$('#not-selected').hide();
	const dataValue = {
		"code_label": $(this).text(),
		"code" : $(this).data('loclarge') 
	}
	const container = $('#sub-location-container');
	
	// 보내야할것 
	// 데이타값, 텍스트 값 
	$.ajax({
		url:"/api/search_getSubLocation",
		type:"post",
		data: dataValue,
		dataType:"json",
		success: function(res) {
			console.log(res);
			container.empty();
			res.map((r,i) => {
				const el = `<span class="keyword location ${i}" data-loc-food="${r.code}">${r.code_label}</span>`
				container.append(el);			
			})
			
		},
		error: function(error) {
			console.log(error);
			alert('서버와 통신오류!');
		}
	})
	
}
