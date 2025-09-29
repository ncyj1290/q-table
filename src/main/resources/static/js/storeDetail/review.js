$(function() {

    // 리뷰 작성 모달 기능
    $('.review-controls .positive-button').on('click', function() {
        $('#reviewModal').show();
    });

    // 모달 닫기 기능
    $('.close').on('click', function() {
        $('#reviewModal').hide();
    });

    // 모달 배경 클릭시 닫기
    $(window).on('click', function(event) {
        if (event.target.id === 'reviewModal') {
            $('#reviewModal').hide();
        }
    });

    // 별점 인터랙션
    let selectedRating = 0;

    $('.star').on('mouseenter', function() {
        const rating = $(this).data('rating');
        highlightStars(rating);
    });

    $('.star').on('mouseleave', function() {
        highlightStars(selectedRating);
    });

    $('.star').on('click', function() {
        selectedRating = $(this).data('rating');
        highlightStars(selectedRating);
        updateStarDisplay(selectedRating);
    });

    function highlightStars(rating) {
        $('.star').each(function(index) {
            if (index < rating) {
                $(this).text('★').addClass('filled');
            } else {
                $(this).text('★').removeClass('filled');
            }
        });
    }

    function updateStarDisplay(rating) {
        $('.star').each(function(index) {
            if (index < rating) {
                $(this).text('★').addClass('filled');
            } else {
                $(this).text('★').removeClass('filled');
            }
        });
    }
});