//positive-button
document.querySelectorAll('.positive-button').forEach(function(btn) {
  btn.addEventListener('click', function(e) {
    let msg = '';
    switch(btn.id) {
      case 'changeBtn':
        msg = '에약 내용을 변경하시겠습니까?';
        break;
      case 'rebookBtn':
        msg = '재예약 하시겠습니까?';
        break;
      case 'revisitBtn':
        msg = '이 가게를 재방문 하시겠습니까?﻿';
        break;
	  case 'reviewBtn':
		msg = '리뷰 작성 페이지로 이동하시겠습니까?﻿';
		break;
	  case 'editBtn':
		msg = '리뷰를 수정하시겠습니까?﻿';
		break;
    }

    if (!confirm(msg)) {
      e.preventDefault();
      return;
    }
  });
});


//negative-button
document.querySelectorAll('.negative-button').forEach(function(btn) {
  btn.addEventListener('click', function(e) {
    let msg = '';
    switch(btn.id) {
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
});
