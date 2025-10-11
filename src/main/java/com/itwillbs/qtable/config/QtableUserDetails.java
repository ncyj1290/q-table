package com.itwillbs.qtable.config;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.itwillbs.qtable.entity.Member;

import lombok.RequiredArgsConstructor;

//디비에서 가져온 유저의 담을 정보 형식을 정하는 클래스 
@RequiredArgsConstructor
public class QtableUserDetails implements UserDetails{
	
	private final Member member;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// DB에서 가져온 권한 값 앞에 "ROLE_"을 붙여서 권한 객체를 생성
	    return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + member.getMemberType()));
	}

	@Override
	public String getPassword() {
		return member.getMemberPw();
	}

	@Override
	public String getUsername() {
		return member.getMemberId();
	}
	
	public Member getMember() {
        return member;
    }

}
