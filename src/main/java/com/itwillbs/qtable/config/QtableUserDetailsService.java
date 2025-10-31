package com.itwillbs.qtable.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.itwillbs.qtable.entity.Member;
import com.itwillbs.qtable.exception.AccountRestoreRequiredException;
import com.itwillbs.qtable.repository.MemberRepository;

import lombok.RequiredArgsConstructor;


//로그인하고 반환될 유저의 정보들, 디비에 있는 컬럼값 다 들어가있음
@RequiredArgsConstructor
@Service 
public class QtableUserDetailsService implements UserDetailsService{
	
	private final MemberRepository repo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Member member = repo.findByMemberId(username)
					        .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다"));

		// mstat_02 상태(탈퇴한 회원)는 로그인 차단
		if ("mstat_02".equals(member.getMemberStatus())) {
			throw new AccountRestoreRequiredException("탈퇴처리된 회원입니다.");
		}

		return new QtableUserDetails(member);
	}
}
