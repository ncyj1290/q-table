$(function() {
    var $mapContainer = $('#map');
    var fullAddress = $('#mapAddress').val();
    var storeName = $('#storeName').val();

    // fullAddress에서 두 번째 부분 추출
    var addressParts = fullAddress.split(',');
    var address = addressParts.length > 1 ? addressParts[1].trim() : fullAddress;

    // 주소로 좌표 검색
    var geocoder = new kakao.maps.services.Geocoder();
    geocoder.addressSearch(address, function(result, status) {
        if (status === kakao.maps.services.Status.OK) {
            var coords = new kakao.maps.LatLng(result[0].y, result[0].x);

            // 지도 생성
            var mapOption = {
                center: coords,
                level: 3
            };
            var map = new kakao.maps.Map($mapContainer[0], mapOption);

            // 마커 표시
            var marker = new kakao.maps.Marker({
                map: map,
                position: coords
            });

            // 인포윈도우 표시
            var infowindow = new kakao.maps.InfoWindow({
                content: '<div style="width:150px;text-align:center;padding:6px 0;">'+storeName+'</div>'
            });
            infowindow.open(map, marker);

            // 좌표 받아온 후 a태그 href 갱신
            var lat = result[0].y;
            var lng = result[0].x;
            $('#mapLink').attr('href', 'https://map.kakao.com/link/map/' + storeName + ',' + lat + ',' + lng);
            $('#mapLink').attr('target', '_blank'); // 새창에서 열리게

        } else {
            console.error('해당 주소를 찾을 수 없습니다:', address);
            // 지도 컨테이너에 에러 메시지 표시
            $('#map').html('<div style="display:flex;align-items:center;justify-content:center;height:100%;color:#999;">주소를 찾을 수 없습니다</div>');
        }
    });
});
