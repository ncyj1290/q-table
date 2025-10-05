// ===================================
// 이벤트 리스너 & 실행 코드
// ===================================
// 상수
const MAX_IMAGES = 3;

// 일반 변수
let currentRoomId = null;
let selectedImages = [];

$(function() {
	// 페이지 로드 시 스크롤 맨 아래로
	scrollToBottom();

	// 채팅방 선택
	$(document).on('click', '.chat-room-item', function(){
		const roomId = $(this).data('room-id');
		activateChatRoom(roomId);
	});

	// 메시지 전송 버튼 클릭
	$sendButton.on('click', sendMessage);

	// 메시지 입력 후 Enter 키
	$messageInput.on('keypress', function(e){
		if (e.key === 'Enter') {
			sendMessage();
		}
	});

	// 사진 첨부 아이콘 클릭
	$attachmentIcon.on('click', function(){
		$fileInput.click();
	});

	// 파일 선택 시
	$fileInput.on('change', function(e){
		let files = e.target.files;
		if (files.length > 0) {
			if (selectedImages.length + files.length > MAX_IMAGES) {
				alert(`이미지는 최대 ${MAX_IMAGES}개까지만 선택할 수 있습니다.`);
				return;
			}
			showImagePreview(files);
		}
	});

	// 미리보기 삭제 버튼 클릭 
	$previewArea.on('click', '.remove-preview-btn', function(){
		let index = $(this).data('index');
		removeImagePreview(index);
	});

	// 채팅창 이미지 클릭 시 확대 
	$messagesArea.on('click', 'img', function(e){
		e.preventDefault();
		e.stopPropagation();
		let imgSrc = $(this).attr('src');
		$modalImage.attr('src', imgSrc);
		$imageModal.css({'display': 'flex'});
	});

	// 모달 닫기 - X 버튼 
	$imageModal.on('click', '.close-modal', function(){
		$imageModal.hide();
	});

	// 모달 닫기 - 배경 클릭
	$imageModal.on('click', function(e){
		if (e.target.id === 'imageModal') {
			$imageModal.hide();
		}
	});

	// 채팅 검색
	$searchInput.on('keyup', function(){
		let searchText = $(this).val();
		filterChatRooms(searchText);
	});
});

// ===================================
// 변수 선언
// ===================================
// 상수
const MAX_IMAGES = 3;

// DOM 캐싱
const $messageInput = $('.chat-message-input');
const $sendButton = $('.positive-button');
const $attachmentIcon = $('.attachment-icon');
const $fileInput = $('#fileInput');
const $messagesArea = $('.chat-messages-area');
const $previewContainer = $('#previewContainer');
const $previewArea = $('.image-preview-area');
const $imageModal = $('#imageModal');
const $modalImage = $('#modalImage');
const $searchInput = $('.chat-search-section input');

// 일반 변수
let currentRoomId = null;
let selectedImages = [];

// ===================================
// 함수 정의
// ===================================

// 메시지 전송
function sendMessage(){
	let message = $messageInput.val();

	// 메시지와 이미지 둘 다 없으면 리턴
	if (message.trim() === '' && selectedImages.length === 0) return;

	// 이미지가 있으면 이미지 메시지 먼저 추가
	if (selectedImages.length > 0) {
		selectedImages.forEach(function(file) {
			const reader = new FileReader();
			reader.onload = function(e) {
				let imageHtml = `
					<div class="message-container sent">
						<div class="message-content">
							<div class="message-time">TODO:시간</div>
							<div class="message-area">
								<img src="${e.target.result}">
							</div>
						</div>
					</div>
				`;
				$messagesArea.append(imageHtml);
				scrollToBottom();
			};
			reader.readAsDataURL(file);
		});
		clearAllPreviews();
	}

	// 텍스트 메시지가 있으면 나중에 추가
	if (message.trim() !== '') {
		setTimeout(function() {
			let messageHtml = `
				<div class="message-container sent">
					<div class="message-content">
						<div class="message-time">TODO:시간</div>
						<div class="message-area">
							<p>${message}</p>
						</div>
					</div>
				</div>
			`;
			$messagesArea.append(messageHtml);
			scrollToBottom();
		}, 100);
	}

	$messageInput.val('');
}

// 이미지 미리보기 표시 (여러 개)
function showImagePreview(files) {
	Array.from(files).forEach(function(file) {
		selectedImages.push(file);
		const reader = new FileReader();

		reader.onload = function(e) {
			let previewHtml = `
				<div class="preview-item" data-index="${selectedImages.length - 1}">
					<img src="${e.target.result}">
					<button class="remove-preview-btn" data-index="${selectedImages.length - 1}">×</button>
				</div>
			`;
			$previewContainer.append(previewHtml);
			$previewArea.show();
		};

		reader.readAsDataURL(file);
	});
}

// 개별 이미지 미리보기 제거
function removeImagePreview(index) {
	selectedImages.splice(index, 1);
	$(`.preview-item[data-index="${index}"]`).remove();

	// 인덱스 재정렬
	$('.preview-item').each(function(i) {
		$(this).attr('data-index', i);
		$(this).find('.remove-preview-btn').attr('data-index', i);
	});

	// 이미지가 없으면 영역 숨김
	if (selectedImages.length === 0) {
		$('.image-preview-area').hide();
	}
}

// 모든 미리보기 제거
function clearAllPreviews() {
	selectedImages = [];
	$previewContainer.empty();
	$previewArea.hide();
	$fileInput.val('');
}

// 채팅방 목록 필터링
function filterChatRooms(searchText) {
	searchText = searchText.toLowerCase().trim();

	$('.chat-room-item').each(function() {
		let roomName = $(this).find('.chat-room-name').text().toLowerCase();
		let lastMessage = $(this).find('.chat-room-last-message').text().toLowerCase();

		// 채팅방 이름이나 마지막 메시지에 검색어가 포함되어 있으면 표시
		if (roomName.includes(searchText) || lastMessage.includes(searchText)) {
			$(this).show();
		} else {
			$(this).hide();
		}
	});
}

// 채팅방 활성화
function activateChatRoom(roomId) {
	// 모든 채팅방에서 active 클래스 제거
	$('.chat-room-item').removeClass('active');

	// 선택된 채팅방에 active 클래스 추가
	$('.chat-room-item[data-room-id="' + roomId + '"]').addClass('active');

	// 현재 채팅방 ID 저장
	currentRoomId = roomId;

	// 채팅방 헤더 이름 변경
	let roomName = $('.chat-room-item[data-room-id="' + roomId + '"]').find('.chat-room-name').text();
	$('.chat-content-header h3').text(roomName);

	// 메시지 목록 로드 (백엔드 구현 들어가면 추가 예정)
	// TODO: loadMessages(roomId);

	// 읽지 않은 메시지 수 업데이트 (0으로)
	let unreadBadge = $('.chat-room-item[data-room-id="' + roomId + '"]').find('.unread-count');
	unreadBadge.text('0').hide();

	scrollToBottom();
}

// 채팅창 스크롤 맨 아래로
function scrollToBottom() {
	$messagesArea.scrollTop($messagesArea[0].scrollHeight);
}
