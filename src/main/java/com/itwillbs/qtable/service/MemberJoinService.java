package com.itwillbs.qtable.service;

import org.springframework.stereotype.Service;

import com.itwillbs.qtable.entity.Member;
import com.itwillbs.qtable.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@Service
@RequiredArgsConstructor
public class MemberJoinService {
	
	private final MemberRepository memberRepository;
	
	public void save(Member member) {
		memberRepository.save(member);
	}
}
