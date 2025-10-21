package com.itwillbs.qtable.service.mypage;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.itwillbs.qtable.mapper.mypage.PasswordMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PasswordService {

    private final PasswordMapper passwordMapper;
    private final PasswordEncoder passwordEncoder;

    public boolean checkCurrentPassword(int memberIdx, String currentPass) {
        String storedHashedPassword = passwordMapper.selectPasswordByMemberIdx(memberIdx);
        return passwordEncoder.matches(currentPass, storedHashedPassword);
    }

    public boolean updatePassword(int memberIdx, String newPass) {
        String encodedNewPass = passwordEncoder.encode(newPass);
        int updateCount = passwordMapper.updatePassword(memberIdx, encodedNewPass);
        return updateCount > 0;
    }
}
