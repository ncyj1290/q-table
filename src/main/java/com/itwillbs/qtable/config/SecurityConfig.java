package com.itwillbs.qtable.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity //컨트롤러로 가기 전에 이 클래스의 필터를 먼저 거쳐가게 가로챔
public class SecurityConfig {
	
	private final QtableUserDetailsService qtableUserDetailsService;
	//로그인 실패 핸들러 주입 
	private final LoginFailHandler failHandler;
	//로그인 성공 핸들러 
	private final LoginSuccessHandler successHandler;
	// 403 에러 핸들러 
	private final QtableAccessDeniedHandler accessDeniedHandler;
    
	@Bean //크롬 개발자 도구 url 요청 무시하기
	public WebSecurityCustomizer webSecurityCustomizer() {
	    return (web) -> web.ignoring().requestMatchers("/.well-known/**");
	}
	
	@Bean
    public PasswordEncoder passwordEncoder() { // 암호화 메서드 
		return NoOpPasswordEncoder.getInstance(); //회원가입 아직 안돼서 암호화 없는걸로함 
//		return new BCryptPasswordEncoder();
    }
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { //필터링 내용들
		return http.authorizeHttpRequests(auth -> auth
				
				// 관리자 : mtype_01
				// 일반회원 : mtype_02
				// 매장회원 : mtype_03
				// 1. 사이트 관리자(mtype_01)만 접근 가능한 경로는 여기에 추가 
	            .requestMatchers(
	            	"/admin**/**",
	                "/store_entry", "/member_payment", "/store_payment", "/store_refund",
	                "/comcode_list", "/comcode_commit", 
	                "/api/members"
	            ).hasRole("mtype_01")
	            
	            // 일반 회원만 접근 가능한 경로는 여기에 추가 
//	            .requestMatchers(
//		               
//	            ).hasRole("mtype_02")
	            
	            // 2. 매장회원(mtype_03)만 접근 가능한 경로는 여기에 추가
	            .requestMatchers(
	                "/store_management_main", "/store_reservation_list",
	                "/store_reservation_detail", "/purchase_subscribe",
	                "/store_calculate_list", "/store_calculate_detail", 
	                "/write_store"
	            ).hasRole("mtype_03")
	            
	            // 사이트 관리자, 일반 회원만 접근 가능한 경로 
//	            .requestMatchers(
//	                
//	            ).hasAnyRole("mtype_01", "mtype_02")
	            
	            // 사이트 관리자, 매장 회원만 접근 가능한 경로 
//	            .requestMatchers(
//	                
//	            ).hasAnyRole("mtype_01", "mtype_03")
	            
	            // 일반회원, 매장 회원만 접근 가능한 경로 
//	            .requestMatchers(
//	                
//	            ).hasAnyRole("mtype_02", "mtype_03")
	            
	            // 3. 인가된 사용자(로그인한 모든 사용자)만 접근 가능한 경로는 여기에 추가
	            .requestMatchers(
	                "/chat", "/on_site_payment", "/reservation", "/mypage_main",
	                "/mypage_review", "/mypage_scrap", "/mypage_history",
	                "/reservation_list", "/reservation_cancel", "/qmoney_charge",
	                "/setting", "/card_edit", "/member_delete", "/profile_settings"
	            ).authenticated()
	
	            // 4.그 외 모든 비로그인까지 모두 허용되는 경로는 여기에 추가 
	            .requestMatchers(
	                "/", "/login**", "/find_account**", "/member_join**", "/terms_of_use",
	                "/privacy_policy", "/error/**", "/search**", "/store_detail_main**", 
	                "/upload/**", "/api/storeDetail/**"
	            ).permitAll()
	            
	            //5.static 파일 경로 
	            .requestMatchers(
            		"/css/**", "/js/**", "/img/**", "/icons/**", "/plugins/**"
	            ).permitAll()
	            // 6. 위에서 설정한 경로 외 나머지 모든 경로는 일단 인증요구
	            .anyRequest().authenticated())
				
				// 로그인 폼 설정 
				.formLogin(form -> form
					.loginPage("/login") //로그인 페이지 경로 
					.loginProcessingUrl("/loginPro") //로그인처리하는 경로 
					.usernameParameter("id") //로그인 페이지에서 name값하고 일치시켜야함 
					.passwordParameter("passwd") //로그인 페이지에서 name값하고 일치시켜야함 
//					.defaultSuccessUrl("/") //성공시 이동하는 기본 경로 
					.successHandler(successHandler) //  성공시 핸들러 
					.failureHandler(failHandler) // 실패시 핸들러 
//					.failureUrl("/login?error=true") //로그인 실패시 이동하는 경로
					.permitAll() //로그인 처리는 누구나 할 수 있어야함 
			 	)
				//로그인 유지 설정 
				.rememberMe(rememberMe -> rememberMe
				    .tokenValiditySeconds(86400 * 30) //30일
				    .userDetailsService(qtableUserDetailsService)
				)
				//로그아웃 설정 
				.logout(logout -> logout
		            .logoutUrl("/logout") // 로그아웃을 처리할 URL 지정
		            .logoutSuccessUrl("/") // 로그아웃 성공 후 리다이렉트될 URL 지정
		            .invalidateHttpSession(true) // HTTP 세션을 무효화할지 여부 (기본값 true)
		            .deleteCookies("QTABLE_SID") // 로그아웃 시 삭제할 쿠키 지정
		        )
				//403에러 페이지 처리, 추후 세부 구현 예정 
				.exceptionHandling(exception -> exception
//		            .accessDeniedPage("/error") // 접근 거부 시 이동할 페이지 지정
					.accessDeniedHandler(accessDeniedHandler)
			    )
				.userDetailsService(qtableUserDetailsService) //커스텀한 객체로 사용하기
				.build();
		 		// 더 해야하는거:  에러컨트롤러, 소셜로그인, 로그인(회원가입) 암호화, + jwt? 
	}
	
}
