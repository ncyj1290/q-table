package com.itwillbs.qtable.config;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.itwillbs.qtable.entity.Member;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QtableOAuth2User implements OAuth2User{
	private final Member member;
    private final OAuth2User oAuth2User;


    public Member getMember() {
        return member;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return member.getMemberId();
    }
}
