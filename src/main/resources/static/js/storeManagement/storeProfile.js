$(function(){
	
	console.log("Store Profile JS Import");
	
	/* 예약 받기 상태 토글 버튼 동작 */
	$("#accept_bt").on("click", async function(event){
		
		event.preventDefault();
		
		let process;
		let answer = confirm("예약 받기 상태를 변경하시겠습니까?");
		let msg;
		
		/* 사용자가 상태 변경 취소하면 */
		if(!answer){
			alert("예약 상태 변경을 취소합니다.");
			return;
		}
		
		/* If user Confirm -> 상태 토글하고 해당 결과 Return, Checked 에 반영 */
		try{
			process = await $.ajax({
				type: "post",
				url: "toggle_accept_status",
				data: {
					store_idx : $("#store_idx").val()
				},
				dataType:"text"
			});
			
			/* 프로세싱 결과에 맞게 msg 분배 */
			switch(process){
				case "true": msg ="이제부터 예약을 받기 시작합니다."; break;
				case "false": msg = "이제부터 예약을 받지 않습니다."; break;
			}
			
			/* 뭐 아무튼 결과들 다 종합해서 이케 저케 UI 변경 */
			const isAccept = (process === "true");
			$("#accept_bt").prop("checked", isAccept);
			alert(msg);
			
		}catch(error){
			console.log("Server Error, Cannot Switch Status");
			alert("예약 상태 갱신에 실패했습니다. 잠시 후 다시 이용해 주세요.");				
		}
	});

	
});