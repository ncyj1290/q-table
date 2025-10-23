// JS (예시: jQuery)
$(document).on('click', 'button[data-type="deleteBtn"]', function() {
	const reviewIdx = $(this).data('review-idx');
	console.log("찍히나" + reviewIdx);
	$.ajax({
		url: '/delect_review',
		type: 'POST',
		data: { reviewIdx: reviewIdx }, // 파라미터 전달은 data 객체로!
		success: function(result) {
			alert('삭제되었습니다!');
			location.reload();
		},
		error: function() {
			alert('삭제 실패');
		}
	});
});

//리뷰 수정
$(document).on('click', '[data-type="editBtn"]', function() {
    const $btn = $(this);
    const $reviewItem = $btn.closest('.review-item');
    const reviewIdx = $btn.data('review-idx');
	


    // textarea가 이미 있다면 (편집 중 상태)
    let $textarea = $reviewItem.find('textarea.edit-area');
    if ($textarea.length > 0 && $textarea.is(':visible')) {
        // 수정 내용 저장
        const updatedContent = $textarea.val();
		console.log('reviewIdx:', reviewIdx, 'updatedContent:', updatedContent);
        $.ajax({
            url: '/reviews_update',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                reviewIdx: reviewIdx,
                content: updatedContent
            }),
            success: function() {
                alert('리뷰가 수정되었습니다.');

                // 편집 종료: textarea 숨기고, span에 텍스트 반영 후 표시
                $textarea.hide();
                $reviewItem.find('.content').text(updatedContent).show();

                // 버튼 텍스트 원상복구
                $btn.text('수정');
            },
            error: function(xhr, status, error) {
				console.error('리뷰 수정 실패:', status, error, xhr.responseText);
                alert('리뷰 수정에 실패했습니다.');
            }
        });

    } else {
        // 편집 시작: textarea 없으면 생성, span 숨기고 textarea 보이기
        const $contentSpan = $reviewItem.find('.content');
        const currentText = $contentSpan.text();

        $contentSpan.hide();

        if ($textarea.length === 0) {
            $textarea = $('<textarea class="edit-area"></textarea>').val(currentText);
            $contentSpan.after($textarea);
        } else {
            $textarea.val(currentText).show();
        }

        $textarea.focus();

        // 버튼 텍스트 변경
        $btn.text('저장');
    }
});

