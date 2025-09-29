$(function() {
    
    var grid = new gridjs.Grid({
        columns: ["NO.","회원 이름", "회원 아이디", "회원 이메일", "가입일", "회원상태", "관리"],
        data: [
            ["1", "김철수", "kim1234", "kim@example.com", "2025-09-25", "" , ""],
            ["2", "이영희", "lee1234", "lee@example.com", "2025-09-29", "" , ""]
        ],
        search: true,
        sort: true,
        pagination: {
            limit: 3
        }
    });

    var wrapper = $("#my-grid-table");
	
    wrapper.empty();
	
    grid.render(wrapper[0]);

});