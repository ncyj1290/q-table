package com.itwillbs.qtable.service.member;

import java.util.List;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.qtable.entity.Member;
import com.itwillbs.qtable.mapper.memberjoin.memerjoin;
import com.itwillbs.qtable.repository.MemberRepository;
import com.itwillbs.qtable.vo.commonCode.CommonCodeVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@Service
@RequiredArgsConstructor
public class MemberJoinService {
	
	private final MemberRepository memberRepository;
    private final memerjoin commonCodeMapper;
    
    private final PasswordEncoder passwordEncoder;
		
	
    public List<CommonCodeVO> getAllCodes() {
        return commonCodeMapper.selectAllCodes();
    }
    public List<CommonCodeVO> getCodesByGroup(String groupCode) {
        return commonCodeMapper.selectByGroupCode(groupCode);
    }

    @Transactional
    public void save(Member member) {
        member.setMemberPw(passwordEncoder.encode(member.getMemberPw()));
        memberRepository.save(member);
    }

}
