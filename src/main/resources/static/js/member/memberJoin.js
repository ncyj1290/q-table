//$(function() {
//    $('#signupForm').on('submit', function(e) {
//        e.preventDefault();
//
//        const userId = $('#userId').val().trim();
//        const password = $('#password').val().trim();
//        const confirmPassword = $('#confirmPassword').val().trim();
//        const userName = $('#userName').val().trim();
//        const gender = $('#gender').val();
//        const birthDate = $('#birthDate').val().trim();
//        const addressPostcode = $('#addressPostcode').val().trim();
//        const addressDetail = $('#addressDetail').val().trim();
//        const email = $('#email').val().trim();
//        const emailVerification = $('#emailVerification').val().trim();
//
//        if (!userId) { alert('아이디를 입력해주세요.'); return; }
//        if (!password) { alert('비밀번호를 입력해주세요.'); return; }
//        if (!confirmPassword) { alert('비밀번호 확인을 입력해주세요.'); return; }
//        if (password !== confirmPassword) { alert('비밀번호가 일치하지 않습니다.'); return; }
//        if (!userName) { alert('이름을 입력해주세요.'); return; }
//        if (!gender) { alert('성별을 선택해주세요.'); return; }
//        if (!birthDate) { alert('생년월일을 입력해주세요.'); return; }
//        if (!addressPostcode) { alert('우편번호를 입력해주세요.'); return; }
//        if (!addressDetail) { alert('상세주소를 입력해주세요.'); return; }
//        if (!email) { alert('이메일을 입력해주세요.'); return; }
//        if (!emailVerification) { alert('이메일 인증번호를 입력해주세요.'); return; }
//
//        alert('모든 입력이 완료되었습니다!');
//    });
//});
