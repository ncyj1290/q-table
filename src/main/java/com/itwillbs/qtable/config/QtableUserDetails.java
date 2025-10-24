package com.itwillbs.qtable.config;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.itwillbs.qtable.entity.Member;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

//디비에서 가져온 유저의 담을 정보 형식을 정하는 클래스 
@RequiredArgsConstructor
@AllArgsConstructor
public class QtableUserDetails implements UserDetails, OAuth2User{
	
	private final Member member;
	private Map<String, Object> attributes;
	
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
	
	// getMemberIdx() 추가
    public Integer getMemberIdx() {
        return member.getMemberIdx();
    }
	
	public Member getMember() {
        return member;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public String getName() {
        return member.getMemberId();
    }

}
