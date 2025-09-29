$(function() {

    var currentUrl = window.location.pathname; 

    $('.nav-treeview .nav-link').each(function() {
        var linkUrl = $(this).attr('href');

        if (currentUrl === linkUrl) {
            
            // 하위 메뉴 링크 활성화
            $(this).addClass('active');

            // 메뉴의 드롭다운 (menu-open)
            $(this).closest('.nav-item').parent().closest('.nav-item').addClass('menu-open');

            // 부모 메뉴의 링크를 활성화
            $(this).closest('.nav-item').parent().closest('.nav-item').children('.nav-link').addClass('active');
        }
    });
});