// ===================================
// 변수 선언
// ===================================
let storeInfo = null;  // 매장 정보

$(function() {
	// ===================================
	// URL 파라미터에서 데이터 가져오기
	// ===================================
	const urlParams = new URLSearchParams(location.search);
	const storeIdx = urlParams.get('store_idx');
	const reserveDate = urlParams.get('reserve_date');
	const reserveTime = urlParams.get('reserve_time');
	const personCount = urlParams.get('person_count');

	// 필수 파라미터 검증
	if (!storeIdx || !reserveDate || !reserveTime || !personCount) {
		alert('잘못된 접근입니다. 매장 상세 페이지에서 예약해주세요.');
		location.href = '/';
		return;
	}

	// ===================================
	// DOM 캐싱
	// ===================================
	const $storeName = $('#storeName');
	const $storeImage = $('#storeImage');
	const $storeLocation = $('#storeLocation');
	const $reservationDate = $('#reservationDate');
	const $reservationTime = $('#reservationTime');
	const $personCountDisplay = $('#personCount');
	const $depositAmount = $('#depositAmount');
	const $totalAmount = $('#totalAmount');
	const $reservationForm = $('#reservationForm');

	// ===================================
	// 초기화
	// ===================================
	loadStoreInfo(storeIdx);
	displayReservationInfo(reserveDate, reserveTime, personCount);

	// ===================================
	// 이벤트 리스너
	// ===================================

	// 폼 전송 시 검증
	$reservationForm.on('submit', function(e) {
		e.preventDefault();

		// 입력 검증
		const customerName = $('#customerName').val().trim();
		const customerEmail = $('#customerEmail').val().trim();
		const requirement = $('#specialRequest').val().trim();
		const allergy = $('#allergies').val().trim();
		const agreePolicy = $('#agreePolicy').is(':checked');

		if (!customerName) {
			alert('이름을 입력해주세요.');
			$('#customerName').focus();
			return false;
		}

		if (!customerEmail) {
			alert('이메일을 입력해주세요.');
			$('#customerEmail').focus();
			return false;
		}

		// 이메일 형식 검증
		const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
		if (!emailRegex.test(customerEmail)) {
			alert('올바른 이메일 형식을 입력해주세요.');
			$('#customerEmail').focus();
			return false;
		}

		if (!agreePolicy) {
			alert('예약 정책에 동의해주세요.');
			return false;
		}

		// 예약 데이터 생성
		const reservationData = {
			store_idx: storeIdx,
			reserve_name: customerName,
			reserve_email: customerEmail,
			reserve_date: reserveDate,
			reserve_time: reserveTime,
			person_count: parseInt(personCount),
			requirement: requirement,
			allergy: allergy
		};

		// 예약 제출
		submitReservation(reservationData);
	});

	// ===================================
	// 함수 정의
	// ===================================

	// 매장 정보 로드
	function loadStoreInfo(storeIdx) {
		$.ajax({
			url: '/api/reservation/store_info',
			type: 'GET',
			data: { store_idx: storeIdx },
			success: function(res) {
				storeInfo = res.data;
//				console.log('서버 응답:', res);
//				console.log('매장 정보:', storeInfo);
				displayStoreInfo(storeInfo);
				calculateDeposit(storeInfo.deposit, personCount);
			},
			error: function(xhr) {
				console.error('매장 정보 로드 실패:', xhr);
				alert('매장 정보를 불러올 수 없습니다.');
				location.href = '/';
			}
		});
	}

	// 매장 정보 표시
	function displayStoreInfo(storeInfo) {
		$storeName.text(storeInfo.store_name);
		$storeImage.attr('src', storeInfo.store_img);
		$storeLocation.text(storeInfo.full_address || storeInfo.sido + ' ' + storeInfo.sigungu);
	}

	// 예약 정보 표시
	function displayReservationInfo(date, time, count) {
		$reservationDate.text(date);
		$reservationTime.text(time);
		$personCountDisplay.text(count + '명');
	}

	// 예약금 계산 및 표시
	function calculateDeposit(depositPerPerson, personCount) {
		const totalDeposit = depositPerPerson * parseInt(personCount);
		$depositAmount.text(depositPerPerson.toLocaleString() + ' Q-money');
		$totalAmount.text(totalDeposit.toLocaleString() + ' Q-money');
	}

	// 예약 제출
	function submitReservation(reservationData) {
		$.ajax({
			url: '/api/reservation/submit',
			type: 'POST',
			contentType: 'application/json',
			data: JSON.stringify(reservationData),
			success: function() {
				alert('예약이 완료되었습니다');
				location.href = '/reservation_list';
			},
			error: function(xhr) {
				console.log(xhr);
				alert("예약에 실패하였습니다.");
			}
		});
	}
});
