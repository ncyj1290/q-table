$(function() {
    // 행 추가 버튼 클릭 시
    $('#add-row-btn').on('click', function(e) {

        const newRow = `
            <tr>
                <td><input type="text" placeholder="그룹 ID"></td>
                <td><input type="text" placeholder="코드 ID"></td>
                <td><input type="text" placeholder="코드명"></td>
                <td><input type="text" placeholder="부모코드 ID"></td>
                <td><button class="negative-button">삭제</button></td>
            </tr>
        `;

        $('.editable-table tbody').append(newRow);
    });

    // 삭제 버튼 클릭 시
    $('.editable-table').on('click', '.negative-button', function() {
        $(this).closest('tr').remove();
    });
});