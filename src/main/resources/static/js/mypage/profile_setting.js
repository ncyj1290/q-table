/* 비밀번호 변경  */

/* ====================================================== */
// 유효성 검사 결과 플래그
let isCurrentPassOk = false;
let isNewPassOk = false;
let isNewPassCheckOk = false;

$(function (){
    // CSRF 토큰과 헤더명 읽기
    const csrfToken = $("meta[name='_csrf']").attr("content");
    const csrfHeader = $("meta[name='_csrf_header']").attr("content");

    /* 현재 비밀번호 입력 시 서버에 일치 여부 확인 */
    $("#current_pass").on("input", function(){
        checkCurrentPassword($(this).val(), csrfToken, csrfHeader);
    });

    /* 새 비밀번호 입력 시 유효성 검사 및 확인 필드 재검사 */
    $("#new_pass").on("input", function(){
        validateNewPassword($(this).val());
        checkNewPasswordMatch();
    });

    /* 새 비밀번호 확인 입력 시 일치 여부 검사 */
    $("#new_pass_check").on("input", function(){
        checkNewPasswordMatch();
    });

    /* '변경 완료' 버튼 클릭 시 최종 검사 및 제출 */
    $("#submit_button").on("click", function(){
        // 1. 모든 필드가 비어있는지 확인
        if(!$("#current_pass").val() || !$("#new_pass").val() || !$("#new_pass_check").val()) {
            alert("모든 비밀번호 필드를 입력해주세요.");
            return;
        }
        // 2. 실시간 유효성 검사 결과가 모두 true인지 확인
        if(!isCurrentPassOk) {
            alert("현재 비밀번호가 올바르지 않습니다.");
            $("#current_pass").focus();
            return;
        }
        if(!isNewPassOk) {
            alert("새 비밀번호가 유효성 조건에 맞지 않습니다.");
            $("#new_pass").focus();
            return;
        }
        if(!isNewPassCheckOk) {
            alert("새 비밀번호 확인이 일치하지 않습니다.");
            $("#new_pass_check").focus();
            return;
        }

        // 3. 모든 검사를 통과하면 서버로 폼 데이터 전송 (CSRF 토큰 포함)
        $.ajax({
            type: "POST",
            url: "/UpdatePassword", // 비밀번호를 최종 변경하는 API 주소
            data: {
                current_pass: $("#current_pass").val(),
                new_pass: $("#new_pass").val()
            },
            beforeSend: function(xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function(response) {
                if(response.success) {
                    alert("비밀번호가 성공적으로 변경되었습니다. 다시 로그인해주세요.");
                    location.href = "/";
                } else {
                    alert(response.message || "비밀번호 변경에 실패했습니다.");
                }
            },
            error: function() {
                alert("비밀번호 변경 중 오류가 발생했습니다.");
            }
        });
    });
});

/**
 * 현재 비밀번호가 DB의 비밀번호와 일치하는지 서버에 확인 요청
 * @param {string} currentPass - 사용자가 입력한 현재 비밀번호
 * @param {string} csrfToken - CSRF 토큰 값
 * @param {string} csrfHeader - CSRF 헤더 이름
 */
async function checkCurrentPassword(currentPass, csrfToken, csrfHeader) {
    const span = $("#span_current_pass");
    if(!currentPass) {
        span.text("");
        isCurrentPassOk = false;
        return;
    }

    try {
        const response = await $.ajax({
            type: "POST",
            url: "/CheckCurrentPassword", // 현재 비밀번호를 검증하는 API 주소
            data: { current_pass: currentPass },
            beforeSend: function(xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken);
            }
        });

        if(response.isValid) {
            span.text("현재 비밀번호와 일치합니다.").css("color", "#008000");
            isCurrentPassOk = true;
        } else {
            span.text("현재 비밀번호와 일치하지 않습니다.").css("color", "#ff0000");
            isCurrentPassOk = false;
        }
    } catch (error) {
        span.text("비밀번호 확인 중 오류가 발생했습니다.").css("color", "#ff0000");
        isCurrentPassOk = false;
    }
}

/**
 * 새 비밀번호의 유효성(형식)을 검사하고 결과를 span에 표시
 * @param {string} newPass - 사용자가 입력한 새 비밀번호
 */
function validateNewPassword(newPass) {
    // 정규식 => 비밀번호 조건: 대문자, 소문자, 특수문자, 숫자 포함 10 글자 이상
    const regex = /^(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,16}$/;
    const isValid = regex.test(newPass);
    const span = $("#span_new_pass");

    if(isValid) {
        span.text("사용 가능한 비밀번호 입니다.").css("color", "#008000");
        isNewPassOk = true;
    } else {
        span.text("8~16자, 영문/숫자/특수문자 포함").css("color", "#ff0000");
        isNewPassOk = false;
    }
}

/**
 * '새 비밀번호'와 '새 비밀번호 확인' 필드가 일치하는지 검사
 */
function checkNewPasswordMatch() {
    const newPass = $("#new_pass").val();
    const newPassCheck = $("#new_pass_check").val();
    const span = $("#span_new_pass_check");

    // 확인 필드가 비어있으면 메시지를 지웁니다.
    if(!newPassCheck) {
        span.text("");
        isNewPassCheckOk = false;
        return;
    }

    if(newPass === newPassCheck) {
        span.text("비밀번호가 일치합니다.").css("color", "#008000");
        isNewPassCheckOk = true;
    } else {
        span.text("비밀번호가 일치하지 않습니다.").css("color", "#ff0000");
        isNewPassCheckOk = false;
    }
}


/* 닉네임 변경 */

/* ====================================================== */
// 닉네임 글자 수 제약 조건
const MIN_NICKNAME_LENGTH = 3;
const MAX_NICKNAME_LENGTH = 12;

/**
 * Debounce 함수: 연속적인 이벤트 발생 시, 마지막 이벤트 후 일정 시간 뒤에만 콜백 실행
 * @param {function} func - 실행할 콜백 함수
 * @param {number} delay - 지연 시간 (ms)
 */
function debounce(func, delay) {
	let timer;
	return function(...args) {
		clearTimeout(timer);
		timer = setTimeout(() => func.apply(this, args), delay);
	};
}

// jQuery: 문서가 완전히 로드된 후 실행
$(function() {
	const DEBOUNCE_DELAY = 300; // 0.3초

	// 닉네임 입력 시 실시간 유효성 검사 (디바운스 적용)
	$('#input_nickname').on('input', debounce(async function() {
		await validateNickname($(this).val());
	}, DEBOUNCE_DELAY));

	// '변경 완료' 버튼 클릭 시 최종 유효성 검사 및 제출 
	$('#nickname_button').on('click', async function(e) {
		e.preventDefault(); // 폼의 기본 제출 동작을 막음

		const currentNickname = $('#input_nickname').val();
		let isNicknameValid = false;

		// 1. 닉네임 최종 유효성 검사
		//    - 닉네임이 원래 닉네임과 같다면, 무조건 '유효'로 처리
		if (currentNickname === originalNickname) {
			isNicknameValid = true;
		} else {
			//    - 닉네임이 변경되었다면, 실시간 검사 함수를 다시 호출하여 최종 확인
			isNicknameValid = await validateNickname(currentNickname);
		}

		if (!isNicknameValid) {
			alert('닉네임이 유효하지 않습니다. 확인해주세요.');
			$('#input_nickname').focus();
			return;
		}
	});
});

/**
 * 닉네임 유효성(자신의 닉네임, 정규식, 서버 중복)을 검사하고 결과를 span에 표시
 * @param {string} nickname - 검사할 닉네임
 * @returns {Promise<boolean>} - 유효성 통과 여부
 */
async function validateNickname(nickname) {
	const span = '#span_nickname';

	// 1. 자신의 '원래 닉네임'인지 가장 먼저 확인
	//    (JSP 파일 하단에서 선언해준 `originalNickname` 전역 변수를 사용)
	if (nickname === originalNickname) {
		$(span).text('현재 사용 중인 닉네임입니다.').css('color', 'blue');
		return true; // 자신의 닉네임은 항상 유효함
	}

	// 2. 닉네임 형식(정규식) 검사
	const regex = new RegExp(`^[가-힣A-Za-z][가-힣A-Za-z0-9]{${MIN_NICKNAME_LENGTH - 1},${MAX_NICKNAME_LENGTH - 1}}$`);
	if (!regex.test(nickname)) {
		$(span).text(`닉네임은 한글/영어로 시작, 한글/영어/숫자만 사용 (${MIN_NICKNAME_LENGTH}~${MAX_NICKNAME_LENGTH}자)`).css('color', 'red');
		return false;
	}

	// 3. 서버에 새로운 닉네임 중복 확인 요청 (AJAX)
	try {
		const response = await $.ajax({
			type: 'get',
			url: 'CheckNicknameForProfile', 
			dataType: 'json',
			data: {
				value: nickname
			}
		});

		$(span).text(response.text).css('color', response.color);

		// 서버가 보내준 boolean 값을 직접 비교
		return response.dupResult === false;

	} catch (e) {
		$(span).text('닉네임 중복 확인 중 오류가 발생했습니다.').css('color', 'red');
		console.error('AJAX Error:', e);
		return false;
	}
}

// 음식 취향 선택 및 저장 기능
$(function() {
    const selectedPrefs = new Set();

    // 음식 취향 스팬 클릭 시 선택 토글
    $('.food-prefs .negative-button').on('click', function() {
        const pref = $(this).text();

        if (selectedPrefs.has(pref)) {
            selectedPrefs.delete(pref);
            $(this).removeClass('selected');
        } else {
            selectedPrefs.add(pref);
            $(this).addClass('selected');
        }
    });

    // 저장 버튼 클릭 시 색상만 유지 (특별한 출력 없음)
    $('.save-btn-area .positive-button').on('click', function() {
        if (selectedPrefs.size === 0) {
            alert('음식 취향을 하나 이상 선택해주세요.');
            return;
        }
        // 저장 시에는 별도로 표시하는 영역 없이 선택 상태(색상)만 유지
        alert('선택한 음식 취향이 저장되었습니다.'); 
    });
});


