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
let currentOppositeProfileImg = null;  // 현재 채팅방의 상대방 프로필 이미지
let lastMessageDate = null;  // 마지막 메시지의 날짜 (날짜 구분선용)

// WebSocket 관련 변수
let stompClient = null;  // STOMP 클라이언트
let currentSubscription = null;  // 현재 구독 객체
let currentReadSubscription = null;  // 현재 읽음 이벤트 구독 객체

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

	// 페이지 로드 시 채팅방 목록 프로필 이미지 렌더링
	renderChatRoomProfiles();

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

	// 채팅방을 맨 위로 이동시키는 함수 (실시간 정렬)
	function moveChatRoomToTop(roomId) {
		const $targetRoom = $('.chat-room-item[data-room-id="' + roomId + '"]');
		if ($targetRoom.length === 0) return;

		const $targetSeparator = $targetRoom.next('.horizontal-line'); // 구분선도 함께 이동

		// "관리자" 채팅방인지 확인
		const roomName = $targetRoom.find('.chat-room-name').text().trim();
		const isAdminRoom = (roomName === '관리자');

		if (isAdminRoom) {
			// 관리자 채팅방은 맨 위로
			$('#chatRoomList').prepend($targetSeparator);
			$('#chatRoomList').prepend($targetRoom);
		} else {
			// 일반 채팅방은 "관리자" 채팅방 다음 위치로
			const $adminRooms = $('.chat-room-item').filter(function() {
				return $(this).find('.chat-room-name').text().trim() === '관리자';
			});

			if ($adminRooms.length > 0) {
				// 마지막 관리자 채팅방의 구분선 다음에 삽입
				const $lastAdminRoom = $adminRooms.last();
				const $lastAdminSeparator = $lastAdminRoom.next('.horizontal-line');
				$lastAdminSeparator.after($targetRoom);
				$targetRoom.after($targetSeparator);
			} else {
				// 관리자 채팅방이 없으면 맨 위로
				$('#chatRoomList').prepend($targetSeparator);
				$('#chatRoomList').prepend($targetRoom);
			}
		}
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

			// 채팅방을 맨 위로 이동
			moveChatRoomToTop(chatMessage.roomIdx);

			// 채팅방 목록의 마지막 메시지 업데이트
			const $targetRoom = $('.chat-room-item[data-room-id="' + chatMessage.roomIdx + '"]');
			$targetRoom.find('.chat-room-last-message').text(chatMessage.message);

			// 현재 보지 않는 채팅방의 메시지만 unread-count 증가
			if (chatMessage.roomIdx !== currentRoomId) {
				let unreadBadge = $targetRoom.find('.unread-count');
				let currentCount = parseInt(unreadBadge.text()) || 0;
				let newCount = currentCount + 1;
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
		if (currentReadSubscription) {
			currentReadSubscription.unsubscribe();
		}

		// 새로운 채팅방 구독
		currentSubscription = stompClient.subscribe('/topic/chat/' + roomIdx, function(message) {
			// 메시지 수신 시 화면에 표시
			const chatMessage = JSON.parse(message.body);
			displayReceivedMessage(chatMessage);
		});

		// 읽음 이벤트 구독
		currentReadSubscription = stompClient.subscribe('/topic/chat/' + roomIdx + '/read', function(message) {
			// 읽음 이벤트 수신 시 처리
			const readEvent = JSON.parse(message.body);
			handleReadEvent(readEvent);
		});
	}

	// 읽음 이벤트 처리
	function handleReadEvent(readEvent) {
		// 상대방이 읽었을 때만 처리 (내가 읽은 건 무시)
		if (readEvent.readerIdx !== currentUserIdx) {
			// 화면의 모든 "1" 표시를 제거
			$('.message-container.sent .message-read-status').text('');
		}
	}

	// 수신한 메시지를 화면에 표시
	function displayReceivedMessage(chatMessage) {
		// 날짜 구분선 확인
		const messageDate = getDateString(chatMessage.timestamp);

		// 날짜가 바뀌었으면 날짜 구분선 추가
		if (lastMessageDate !== messageDate) {
			const dateDividerHtml = `
				<div class="date-divider">
					<span>${formatDateKorean(chatMessage.timestamp)}</span>
				</div>
			`;
			$messagesArea.append(dateDividerHtml);
			lastMessageDate = messageDate;
		}

		// 시간 포맷팅
		const time = new Date(chatMessage.timestamp).toLocaleTimeString('ko-KR', {
			hour: '2-digit',
			minute: '2-digit',
			hour12: false
		});

		let messageHtml = '';

		// 발신자와 수신자 구분
		const isSentByCurrentUser = chatMessage.senderIdx === currentUserIdx;
		const messageContainerClass = isSentByCurrentUser ? 'sent' : 'received';

		if (isSentByCurrentUser) {
			// 내가 보낸 메시지 (실시간으로 보낸 메시지는 항상 안 읽음 상태)
			if (isImageUrl(chatMessage.message)) {
				// 이미지 메시지
				messageHtml = `
					<div class="message-container ${messageContainerClass}">
						<div class="message-content">
							<div class="message-read-status">1</div>
							<div class="message-time">${time}</div>
							<div class="message-area">
								<img src="${chatMessage.message}" alt="image">
							</div>
						</div>
					</div>
				`;
				$messagesArea.append(messageHtml);
			} else {
				// 텍스트 메시지
				messageHtml = `
					<div class="message-container ${messageContainerClass}">
						<div class="message-content">
							<div class="message-read-status">1</div>
							<div class="message-time">${time}</div>
							<div class="message-area">
								<p class="message-text"></p>
							</div>
						</div>
					</div>
				`;
				const $msg = $(messageHtml);
				$msg.find('.message-text').text(chatMessage.message); // XSS 방지
				$messagesArea.append($msg);
			}
		} else {
			// 다른 사람이 보낸 메시지
			const senderName = currentOppositeName || chatMessage.senderName;
			const profileImgUrl = chatMessage.senderProfileImg || currentOppositeProfileImg;

			if (isImageUrl(chatMessage.message)) {
				// 이미지 메시지
				messageHtml = `
					<div class="message-container ${messageContainerClass}">
						<div class="message-profile">
							<div class="profile-image"></div>
							<span class="sender-name"></span>
						</div>
						<div class="message-content">
							<div class="message-area">
								<img src="${chatMessage.message}" alt="image">
							</div>
							<div class="message-time">${time}</div>
						</div>
					</div>
				`;
				const $msg = $(messageHtml);
				$msg.find('.profile-image').html(getProfileImageHtml(profileImgUrl, senderName));
				$msg.find('.sender-name').text(senderName); // XSS 방지
				$messagesArea.append($msg);
			} else {
				// 텍스트 메시지
				messageHtml = `
					<div class="message-container ${messageContainerClass}">
						<div class="message-profile">
							<div class="profile-image"></div>
							<span class="sender-name"></span>
						</div>
						<div class="message-content">
							<div class="message-area">
								<p class="message-text"></p>
							</div>
							<div class="message-time">${time}</div>
						</div>
					</div>
				`;
				const $msg = $(messageHtml);
				$msg.find('.profile-image').html(getProfileImageHtml(profileImgUrl, senderName));
				$msg.find('.sender-name').text(senderName); // XSS 방지
				$msg.find('.message-text').text(chatMessage.message); // XSS 방지
				$messagesArea.append($msg);
			}

			// 현재 채팅방에서 실시간으로 받은 메시지는 즉시 읽음 처리
			if (currentRoomId) {
				markMessagesAsRead(currentRoomId);
			}
		}

		scrollToBottom();
	}

	// 현재 시간 가져오기
	function getCurrentTime() {
		const now = new Date();
		const hours = String(now.getHours()).padStart(2, '0');
		const minutes = String(now.getMinutes()).padStart(2, '0');
		return `${hours}:${minutes}`;
	}

	// 이미지 URL인지 확인
	function isImageUrl(text) {
		return text && (text.startsWith('/uploads/') || text.match(/\.(jpg|jpeg|png|gif|webp)$/i));
	}

	// 프로필 이미지 HTML 생성
	function getProfileImageHtml(profileImgUrl, senderName) {
		if (profileImgUrl && profileImgUrl.trim() !== '') {
			return `<img src="${profileImgUrl}" alt="profile">`;
		} else {
			// 프로필 이미지가 없으면 텍스트 표시
			// 관리자는 전체 텍스트, 그 외는 첫 글자
			const displayText = senderName === '관리자' ? '관리자' : (senderName ? senderName.charAt(0) : '?');
			return `<span class="profile-initial">${displayText}</span>`;
		}
	}

	// 채팅방 목록 프로필 이미지 렌더링
	function renderChatRoomProfiles() {
		$('.chat-room-item').each(function() {
			const $roomItem = $(this);
			const profileImgUrl = $roomItem.data('opposite-profile-img');
			const roomName = $roomItem.find('.chat-room-name').text();
			const $profileArea = $roomItem.find('.chat-room-profile');

			$profileArea.html(getProfileImageHtml(profileImgUrl, roomName));
		});
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

		// 이미지가 있으면 서버에 업로드
		if (selectedImages.length > 0) {
			selectedImages.forEach(function(file) {
				uploadImage(file, currentRoomId);
			});
			clearAllPreviews();
		}

		// 텍스트 메시지가 있으면 WebSocket으로 전송
		if (message.trim() !== '') {
			// 채팅 메시지 객체 생성
			const chatMessage = {
				roomIdx: currentRoomId,
				message: message
			};

			// WebSocket으로 메시지 전송
			// 서버에서 브로드캐스트될 메시지를 수신해서 displayReceivedMessage로 표시됨
			stompClient.send('/app/chat/send', {}, JSON.stringify(chatMessage));
			// DB에 메시지 저장
			saveMessage(message, currentRoomId);

			// 현재 채팅방을 맨 위로 이동 (내가 메시지 보냈을 때도 실시간 정렬)
			moveChatRoomToTop(currentRoomId);

			// 채팅방 목록의 마지막 메시지 업데이트
			const $currentRoom = $('.chat-room-item[data-room-id="' + currentRoomId + '"]');
			$currentRoom.find('.chat-room-last-message').text(message);

			// 입력 필드 비우기
			$messageInput.val('');
		}
	}

	// 이미지 업로드
	function uploadImage(file, roomId) {
		const formData = new FormData();
		formData.append('image', file);
		formData.append('room_id', roomId);

		$.ajax({
			url: '/api/chat/upload-image',
			type: 'POST',
			data: formData,
			processData: false,
			contentType: false,
			success: function(response) {
				if (response.success) {
					// 이미지 URL을 WebSocket으로 전송
					const chatMessage = {
						roomIdx: roomId,
						message: response.imageUrl
					};
					stompClient.send('/app/chat/send', {}, JSON.stringify(chatMessage));

					// 채팅방을 맨 위로 이동
					moveChatRoomToTop(roomId);

					// 채팅방 목록의 마지막 메시지 업데이트
					const $currentRoom = $('.chat-room-item[data-room-id="' + roomId + '"]');
					$currentRoom.find('.chat-room-last-message').text('[이미지]');
				} else {
					alert('이미지 업로드에 실패했습니다.');
				}
			},
			error: function(xhr) {
				console.error('이미지 업로드 에러:', xhr);
				alert('이미지 업로드에 실패했습니다.');
			}
		});
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
			const $separator = $(this).next('.horizontal-line'); // 다음 구분선 찾기

			// 채팅방 이름이나 마지막 메시지에 검색어가 포함되어 있으면 표시
			if (roomName.includes(searchText) || lastMessage.includes(searchText)) {
				$(this).show();
				$separator.show(); // 구분선도 함께 표시
			} else {
				$(this).hide();
				$separator.hide(); // 구분선도 함께 숨김
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

		// 데이터 속성에서 프로필 이미지 가져오기
		currentOppositeProfileImg = $roomItem.data('opposite-profile-img') || null;

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

					// 날짜 추적 초기화
					lastMessageDate = null;

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

	// 날짜를 YYYY-MM-DD 형식으로 변환
	function getDateString(date) {
		const d = new Date(date);
		return d.getFullYear() + '-' +
		       String(d.getMonth() + 1).padStart(2, '0') + '-' +
		       String(d.getDate()).padStart(2, '0');
	}

	// 날짜를 한글 형식으로 포맷팅 (예: 2025년 10월 26일)
	function formatDateKorean(date) {
		const d = new Date(date);
		return d.getFullYear() + '년 ' + (d.getMonth() + 1) + '월 ' + d.getDate() + '일';
	}

	// 불러온 메시지를 화면에 표시
	function displayLoadedMessage(msg) {
		// 현재 메시지의 날짜
		const messageDate = getDateString(msg.created_at);

		// 날짜가 바뀌었으면 날짜 구분선 추가
		if (lastMessageDate !== messageDate) {
			const dateDividerHtml = `
				<div class="date-divider">
					<span>${formatDateKorean(msg.created_at)}</span>
				</div>
			`;
			$messagesArea.append(dateDividerHtml);
			lastMessageDate = messageDate;
		}

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
			const readStatus = msg.is_read ? '' : '1';

			if (isImageUrl(msg.message_content)) {
				// 이미지 메시지
				messageHtml = `
					<div class="message-container ${messageContainerClass}">
						<div class="message-content">
							<div class="message-read-status">${readStatus}</div>
							<div class="message-time">${time}</div>
							<div class="message-area">
								<img src="${msg.message_content}" alt="image">
							</div>
						</div>
					</div>
				`;
				$messagesArea.append(messageHtml);
			} else {
				// 텍스트 메시지
				messageHtml = `
					<div class="message-container ${messageContainerClass}">
						<div class="message-content">
							<div class="message-read-status">${readStatus}</div>
							<div class="message-time">${time}</div>
							<div class="message-area">
								<p class="message-text"></p>
							</div>
						</div>
					</div>
				`;
				const $msg = $(messageHtml);
				$msg.find('.message-text').text(msg.message_content); // XSS 방지
				$messagesArea.append($msg);
			}
		} else {
			// 다른 사람이 보낸 메시지
			const senderName = msg.sender_name || '알 수 없음';
			const profileImgUrl = msg.sender_profile_img;

			if (isImageUrl(msg.message_content)) {
				// 이미지 메시지
				messageHtml = `
					<div class="message-container ${messageContainerClass}">
						<div class="message-profile">
							<div class="profile-image"></div>
							<span class="sender-name"></span>
						</div>
						<div class="message-content">
							<div class="message-area">
								<img src="${msg.message_content}" alt="image">
							</div>
							<div class="message-time">${time}</div>
						</div>
					</div>
				`;
				const $msg = $(messageHtml);
				$msg.find('.profile-image').html(getProfileImageHtml(profileImgUrl, senderName));
				$msg.find('.sender-name').text(senderName); // XSS 방지
				$messagesArea.append($msg);
			} else {
				// 텍스트 메시지
				messageHtml = `
					<div class="message-container ${messageContainerClass}">
						<div class="message-profile">
							<div class="profile-image"></div>
							<span class="sender-name"></span>
						</div>
						<div class="message-content">
							<div class="message-area">
								<p class="message-text"></p>
							</div>
							<div class="message-time">${time}</div>
						</div>
					</div>
				`;
				const $msg = $(messageHtml);
				$msg.find('.profile-image').html(getProfileImageHtml(profileImgUrl, senderName));
				$msg.find('.sender-name').text(senderName); // XSS 방지
				$msg.find('.message-text').text(msg.message_content); // XSS 방지
				$messagesArea.append($msg);
			}
		}
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
				// 헤더 배지 업데이트 (header.js에 정의된 함수)
				if (typeof updateHeaderChatBadge === 'function') {
					updateHeaderChatBadge();
				}
			},
			error: function(xhr) {
				console.error('메시지 읽음 처리 실패:', xhr);
			}
		});
	}
});
