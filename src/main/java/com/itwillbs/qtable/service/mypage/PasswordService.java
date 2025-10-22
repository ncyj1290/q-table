package com.itwillbs.qtable.service.mypage;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.itwillbs.qtable.mapper.mypage.PasswordMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PasswordService {

    private final PasswordMapper passwordMapper;
    private final PasswordEncoder passwordEncoder;
    private final RandomNickname randomNickname;

    public boolean checkCurrentPassword(int memberIdx, String currentPass) {
        String storedHashedPassword = passwordMapper.selectPasswordByMemberIdx(memberIdx);
        return passwordEncoder.matches(currentPass, storedHashedPassword);
    }

    public boolean updatePassword(int memberIdx, String newPass) {
        String encodedNewPass = passwordEncoder.encode(newPass);
        int updateCount = passwordMapper.updatePassword(memberIdx, encodedNewPass);
        return updateCount > 0;
    }
    
    // 닉네임 중복 확인
    public boolean isNicknameDuplicate(String nickname) {
        return passwordMapper.countNickname(nickname) > 0;
    }

    // 닉네임 업데이트
    public boolean updateNickname(int memberIdx, String newNickname) {
        int updatedRows = passwordMapper.updateNickname(memberIdx, newNickname);
        return updatedRows > 0;
    }
    
    // 음식 취향
    public void saveFoodPrefs(int memberIdx, List<String> foodPrefs) {
    	passwordMapper.deleteByMemberIdx(memberIdx); // 기존 취향 삭제
        for (String pref : foodPrefs) {
        	passwordMapper.insert(memberIdx, pref);
        }
    }
    
    public List<String> getFoodPrefs(int memberIdx) {
        return passwordMapper.selectByMemberIdx(memberIdx);
    }
    
    // 닉네임이 비어있는 회원들 가져오기
    public List<Integer> findMembersWithEmptyNickname() {
        return passwordMapper.selectMembersWithEmptyNickname();
    }

    // 랜덤 닉네임 생성 후 중복체크하여 회원 닉네임 업데이트
    public void fillEmptyNicknames() {
        List<Integer> memberIdxList = findMembersWithEmptyNickname();

        for (int memberIdx : memberIdxList) {
            String generatedNick = randomNickname.generate();

            // 중복 방지용
            while (isNicknameDuplicate(generatedNick)) {
                generatedNick = randomNickname.generate();
            }

            updateNickname(memberIdx, generatedNick);
        }
    }
    
    
    
    
}
