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

