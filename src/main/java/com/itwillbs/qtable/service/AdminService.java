package com.itwillbs.qtable.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.qtable.entity.Member;
import com.itwillbs.qtable.repository.MemberRepository;
import com.itwillbs.qtable.vo.admin.MemberListVO;

@Service

public class AdminService {
 
	@Autowired
	private MemberRepository memberRepository;
	
	// 전체 회원 목록 조회
	public List<MemberListVO> memberFindAll(){
		
		List<Member> memberList = memberRepository.findAll();
		System.out.println("memberList : " + memberList);
		
		return memberList.stream()
				.map(entity -> new MemberListVO(entity, entity.getMemberIdx()))
                .collect(Collectors.toList());
	}

}
