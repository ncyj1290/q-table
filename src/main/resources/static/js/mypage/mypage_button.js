//positive-button
$(document).on('click', '.positive-button', function(e) {
	const type = $(this).data('type');
   	let msg = '';
	if (!type) return;
  switch (type) {
    case 'changeBtn':
      msg = '예약 내용을 변경하시겠습니까?';
      break;
    case 'rebookBtn':
      msg = '재예약 하시겠습니까?';
      break;
    case 'revisitBtn':
      msg = '이 가게를 재방문 하시겠습니까?';
      break;
    case 'reviewBtn':
      msg = '리뷰 작성 페이지로 이동하시겠습니까?';
      break;
    case 'editBtn':
      msg = '리뷰를 수정하시겠습니까?';
      break;
  }

  if (!confirm(msg)) {
    e.preventDefault();
  }
});


//negative-button
$(document).on('click', '.negative-button', function(e) {
	const type = $(this).data('type');
    let msg = '';
    switch(type) {
      case 'cancelBtn':
        msg = '정말로 예약을 취소하시겠습니까?';
        break;
      case 'deleteBtn':
        msg = '리뷰를 삭제하시겠습니까?';
        break;
    }

    if (!confirm(msg)) {
      e.preventDefault();
      return;
    }
  });
