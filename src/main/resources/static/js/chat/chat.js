// ===================================
// 변수 선언
// ===================================
// 상수
const MAX_IMAGES = 3;

// 일반 변수
let currentRoomId = null;
let selectedImages = [];
let currentUserIdx = null;  // 현재 로그인한 사용자 ID
let currentOppositeIdx = null;  // 현재 채팅방의 상대방 ID
let currentOppositeName = null;  // 현재 채팅방의 상대방 이름

// WebSocket 관련 변수
let stompClient = null;  // STOMP 클라이언트
let currentSubscription = null;  // 현재 구독 객체

$(function() {
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

	// ===================================
	// 이벤트 리스너
	// ===================================

	// 현재 사용자 ID 초기화 
	const userIdElement = $('[data-current-user-idx]');
	if (userIdElement.length > 0) {
		currentUserIdx = parseInt(userIdElement.data('current-user-idx'));
	}
	
	// 페이지 로드 시 WebSocket 연결
	connectWebSocket();
	
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

	// ===================================
	// 함수 정의
	// ===================================

	// WebSocket 연결
	function connectWebSocket() {
		// SockJS와 STOMP를 사용하여 WebSocket 연결
		const socket = new SockJS('/ws-chat');
		stompClient = Stomp.over(socket);
		// 로그 끄기 키려면 주석
		stompClient.debug = null;

		// 연결 성공 시 콜백
		stompClient.connect({}, function() {
			console.log('WebSocket 연결 성공');

			// 모든 사용자의 개인 채널 구독 (읽지 않은 메시지 카운트 업데이트용)
			subscribeToUserChannel();
			console.log("브로드캐스트 구독");
		}, function(error) {
			// 연결 실패 시 콜백
			console.error('WebSocket 연결 실패:', error);
			// 5초 후 재연결 시도
			setTimeout(connectWebSocket, 5000);
		});
	}

	// 사용자 개인 채널 구독 (미읽음 카운트 업데이트용)
	function subscribeToUserChannel() {
		stompClient.subscribe('/topic/chat-broadcast', function(message) {
			const chatMessage = JSON.parse(message.body);

			console.log("브로드캐스트 실행");

			// 자신이 보낸 메시지는 제외
			if (chatMessage.senderIdx === currentUserIdx) {
				return;
			}

			// 내 채팅방 목록에 있는지 확인
			const myRoomIds = [];
			$('.chat-room-item').each(function() {
				myRoomIds.push(parseInt($(this).data('room-id')));
			});

			// 내 채팅방이 아니면 무시
			if (!myRoomIds.includes(chatMessage.roomIdx)) {
				console.log("내 채팅방 아님 무시:", chatMessage.roomIdx);
				return;
			}

			// 현재 보지 않는 채팅방의 메시지만 unread-count 증가
			if (chatMessage.roomIdx !== currentRoomId) {
				let unreadBadge = $('.chat-room-item[data-room-id="' + chatMessage.roomIdx + '"]').find('.unread-count');
				let currentCount = parseInt(unreadBadge.text()) || 0;
				let newCount = currentCount + 1;
				console.log(newCount);
				// 미읽음 수가 0이면 배지 숨김, 아니면 표시
				if (newCount > 0) {
					unreadBadge.text(newCount).show();
				} else {
					unreadBadge.hide();
				}
			}
		});
	}

	// 채팅방 구독
	function subscribeToRoom(roomIdx) {
		// 이전 구독이 있으면 해제
		if (currentSubscription) {
			currentSubscription.unsubscribe();
		}

		// 새로운 채팅방 구독
		currentSubscription = stompClient.subscribe('/topic/chat/' + roomIdx, function(message) {
			// 메시지 수신 시 화면에 표시
			const chatMessage = JSON.parse(message.body);
			displayReceivedMessage(chatMessage);
		});
		
	}

	// 수신한 메시지를 화면에 표시
	function displayReceivedMessage(chatMessage) {
		let messageHtml = '';

		// 시간 포맷팅
		const time = new Date(chatMessage.timestamp).toLocaleTimeString('ko-KR', {
			hour: '2-digit',
			minute: '2-digit',
			hour12: false
		});

		// 메시지 타입에 따라 다르게 표시
		if (chatMessage.type === 'ENTER' || chatMessage.type === 'LEAVE') {
			// 입장/퇴장 메시지
			messageHtml = `
				<div class="date-divider">
					<span>${chatMessage.message}</span>
				</div>
			`;
		} else {
			// 일반 메시지 - 발신자와 수신자 구분
			const isSentByCurrentUser = chatMessage.senderIdx === currentUserIdx;
			const messageContainerClass = isSentByCurrentUser ? 'sent' : 'received';

			if (isSentByCurrentUser) {
				// 내가 보낸 메시지
				messageHtml = `
					<div class="message-container ${messageContainerClass}">
						<div class="message-content">
							<div class="message-time">${time}</div>
							<div class="message-area">
								<p>${chatMessage.message}</p>
							</div>
						</div>
					</div>
				`;
			} else {
				// 다른 사람이 보낸 메시지
				messageHtml = `
					<div class="message-container ${messageContainerClass}">
						<div class="message-profile">
							<div class="profile-image"></div>
							<span class="sender-name">${currentOppositeName || chatMessage.senderName}</span>
						</div>
						<div class="message-content">
							<div class="message-area">
								<p>${chatMessage.message}</p>
							</div>
							<div class="message-time">${time}</div>
						</div>
					</div>
				`;
			}
		}

		$messagesArea.append(messageHtml);
		scrollToBottom();
	}

	// 현재 시간 가져오기 
	function getCurrentTime() {
		const now = new Date();
		const hours = String(now.getHours()).padStart(2, '0');
		const minutes = String(now.getMinutes()).padStart(2, '0');
		return `${hours}:${minutes}`;
	}

	// 채팅창 스크롤 맨 아래로
	function scrollToBottom() {
		// DOM 요소가 존재하는지 확인 후 실행
		if ($messagesArea && $messagesArea.length > 0) {
			$messagesArea.scrollTop($messagesArea[0].scrollHeight);
		}
	}

	// 메시지 전송
	function sendMessage(){
		let message = $messageInput.val();

		// 메시지와 이미지 둘 다 없으면 리턴
		if (message.trim() === '' && selectedImages.length === 0) return;

		// 현재 채팅방이 선택되지 않았으면 리턴
		if (!currentRoomId) {
			alert('채팅방을 선택해주세요.');
			return;
		}

		// WebSocket이 연결되지 않았으면 리턴
		if (!stompClient || !stompClient.connected) {
			alert('연결 중입니다. 잠시 후 다시 시도해주세요.');
			return;
		}

		// 이미지가 있으면 이미지 메시지 먼저 추가 (임시 - 추후 파일 업로드 구현 필요)
		if (selectedImages.length > 0) {
			selectedImages.forEach(function(file) {
				const reader = new FileReader();
				reader.onload = function(e) {
					let imageHtml = `
						<div class="message-container sent">
							<div class="message-content">
								<div class="message-time">${getCurrentTime()}</div>
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

		// 텍스트 메시지가 있으면 WebSocket으로 전송
		if (message.trim() !== '') {
			// 채팅 메시지 객체 생성
			const chatMessage = {
				type: 'TALK',
				roomIdx: currentRoomId,
				message: message
			};

			// WebSocket으로 메시지 전송
			// 서버에서 브로드캐스트될 메시지를 수신해서 displayReceivedMessage로 표시됨
			stompClient.send('/app/chat/send', {}, JSON.stringify(chatMessage));

			// DB에 메시지 저장
			saveMessage(message, currentRoomId);

			// 입력 필드 비우기
			$messageInput.val('');
		}
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
			$previewArea.hide();
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
		const $selectedRoom = $('.chat-room-item[data-room-id="' + roomId + '"]');
		$selectedRoom.addClass('active');

		// 현재 채팅방 ID 저장
		currentRoomId = roomId;

		// 상대방 정보 저장 (opposite_name은 이미 opposite_name 데이터 속성으로 저장)
		currentOppositeName = $selectedRoom.find('.chat-room-name').text();

		// 데이터 속성에서 opposite_member_idx 가져오기 (필요시)
		let $roomItem = $('.chat-room-item[data-room-id="' + roomId + '"]');
		currentOppositeIdx = $roomItem.data('opposite-idx') || null;

		// 채팅방 헤더 이름 변경
		$('.chat-content-header h3').text(currentOppositeName);

		// WebSocket이 연결되어 있으면 채팅방 구독
		if (stompClient && stompClient.connected) {
			subscribeToRoom(roomId);
		}

		// 메시지 목록 로드
		loadMessages(roomId);

		// 채팅방의 메시지들을 읽음으로 표시
		markMessagesAsRead(roomId);

		// 읽지 않은 메시지 수 업데이트 (배지 숨김)
		let unreadBadge = $selectedRoom.find('.unread-count');
		unreadBadge.text('0').hide();

		scrollToBottom();
	}

	// 채팅 메시지 목록 불러오기
	function loadMessages(roomIdx) {
		$.ajax({
			url: '/api/chat/messages',
			type: 'POST',
			data: { room_idx: roomIdx },
			success: function(response) {
				if (response.success) {
					// 기존 메시지 영역 비우기
					$messagesArea.empty();

					// 메시지 목록 렌더링
					if (response.messages && response.messages.length > 0) {
						response.messages.forEach(function(msg) {
							displayLoadedMessage(msg);
						});
					} else {
						// 메시지가 없으면 empty-state 표시
						$messagesArea.html(`
							<div class="empty-state">
								<i class="fas fa-comments"></i>
								<h2>아직 메시지가 없습니다</h2>
								<p>대화를 시작해보세요</p>
							</div>
						`);
					}

					// 스크롤 맨 아래로
					scrollToBottom();
				} else {
					console.error('메시지 로드 실패:', response.message);
				}
			},
			error: function(xhr) {
				console.error('메시지 로드 에러:', xhr);
			}
		});
	}

	// 불러온 메시지를 화면에 표시
	function displayLoadedMessage(msg) {
		let messageHtml = '';

		// 시간 포맷팅
		const time = new Date(msg.created_at).toLocaleTimeString('ko-KR', {
			hour: '2-digit',
			minute: '2-digit',
			hour12: false
		});

		// 발신자와 수신자 구분
		const isSentByCurrentUser = msg.sender_idx === currentUserIdx;
		const messageContainerClass = isSentByCurrentUser ? 'sent' : 'received';

		if (isSentByCurrentUser) {
			// 내가 보낸 메시지
			messageHtml = `
				<div class="message-container ${messageContainerClass}">
					<div class="message-content">
						<div class="message-time">${time}</div>
						<div class="message-area">
							<p>${msg.message_content}</p>
						</div>
					</div>
				</div>
			`;
		} else {
			// 다른 사람이 보낸 메시지
			messageHtml = `
				<div class="message-container ${messageContainerClass}">
					<div class="message-profile">
						<div class="profile-image"></div>
						<span class="sender-name">${msg.sender_name || '알 수 없음'}</span>
					</div>
					<div class="message-content">
						<div class="message-area">
							<p>${msg.message_content}</p>
						</div>
						<div class="message-time">${time}</div>
					</div>
				</div>
			`;
		}

		$messagesArea.append(messageHtml);
	}
	
	// 메시지 DB 저장
	function saveMessage(message, roomId) {
		$.ajax({
			url: '/api/chat/message/insert',
			type: 'POST',
			data: {
				msg: message,
				room_id: roomId
			},
			success: function(response) {
			},
			error: function(xhr) {
				console.error('메시지 저장 실패:', xhr);
			}
		});
	}

	// 채팅방 메시지 읽음 처리
	function markMessagesAsRead(roomIdx) {
		$.ajax({
			url: '/api/chat/mark-as-read',
			type: 'POST',
			data: { room_idx: roomIdx },
			success: function(response) {
			},
			error: function(xhr) {
				console.error('메시지 읽음 처리 실패:', xhr);
			}
		});
	}
});
