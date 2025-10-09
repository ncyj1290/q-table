$(document).ready(function() {
    const $icon = $('.profile-icon');
    const $dropdown = $('.dropdown-menu');

    // 아이콘 클릭 시 토글
    $icon.on('click', function(e) {
        e.stopPropagation();
        $dropdown.toggle();
    });

    
    $dropdown.on('click', function(e) {
        e.stopPropagation();
    });

    // 외부 클릭 시 닫기
    $(document).on('click', function() {
        $dropdown.hide();
    });
});
