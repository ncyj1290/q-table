/* 비밀번호 변경을 위한 JS 파일 */

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
