package com.itwillbs.qtable.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

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

    public List<CommonCodeVO> getAllCodes() {
        return commonCodeMapper.selectAllCodes();
    }
    public List<CommonCodeVO> getCodesByGroup(String groupCode) {
        return commonCodeMapper.selectByGroupCode(groupCode);
    }

	public void save(Member member) {
		memberRepository.save(member);
	}

}
