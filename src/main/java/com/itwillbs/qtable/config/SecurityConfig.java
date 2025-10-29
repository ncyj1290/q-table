package com.itwillbs.qtable.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itwillbs.qtable.exception.AccountRestoreRequiredException;

import jakarta.servlet.http.HttpServletResponse;
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
//	private final QtableAccessDeniedHandler accessDeniedHandler;
    // 소셜 로그인 유저디테일 서비스 
	private final QtableOAuth2UserService oAuth2UserService;
	// 소셜 로그인 예외 핸들러 
//	private final AuthenticationFailureHandler authenticationFailureHandler;
	//아이디 기억하기 위한 시크릿키 
	@Value("${security.rememberme.key}")
	private String rememberme;
	
	@Bean //크롬 개발자 도구 url 요청 무시하기
	public WebSecurityCustomizer webSecurityCustomizer() {
	    return (web) -> web.ignoring().requestMatchers("/.well-known/**");
	}
	
	@Bean
    public PasswordEncoder passwordEncoder() { // 암호화 메서드 
		return new BCryptPasswordEncoder();
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
	                "/write_store", "/modify_store"
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
	                "/chat", "/api/chat/**", "/on_site_payment", "/reservation", "/mypage_main",
	                "/mypage_review", "/mypage_scrap", "/mypage_history",
	                "/reservation_list", "/reservation_cancel", "/qmoney_charge",
	                "/setting", "/card_edit", "/member_delete**", "/profile_settings",
	                "/api/member_delete_social"
	            ).authenticated()
	
	            // 4.그 외 모든 비로그인까지 모두 허용되는 경로는 여기에 추가 
	            .requestMatchers(
	                "/", "/find_account**", "/member_join**", "/terms_of_use",
	                "/privacy_policy", "/error/**", "/search**", "/store_detail_main**", 
	                "/upload/**", "/api/storeDetail/**", "/oauth/**","/api/member_restore",
	                "/api/search_getSubLocation","/send","/verify","/api/search", "/checkMemberId","/checkBusinessNo",
	                "/ws-chat/**", "/api/find**/**"
	            ).permitAll()
	            
	            .requestMatchers(
            		"/login", "/loginPro"
	            ).anonymous()
	            
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
			 	)
				.oauth2Login(oauth2 -> oauth2
	                .loginPage("/login")  // 직접 만든 로그인 페이지 경로
	                .defaultSuccessUrl("/", true)  // 로그인 성공 시 이동할 곳
	                .userInfoEndpoint(userInfo -> userInfo
	                		.userService(oAuth2UserService) // 커스텀한 소셜유저서비스 적용시키기 
                	).failureHandler(authenticationFailureHandler())
	                 .successHandler(successHandler)
	            )
				//로그인 유지 설정 
				.rememberMe(rememberMe -> rememberMe
					.rememberMeParameter("remember-me")
					.key(rememberme)
				    .tokenValiditySeconds(86400 * 30) //30일
				    .userDetailsService(qtableUserDetailsService)
				)
				//로그아웃 설정 
				.logout(logout -> logout
		            .logoutUrl("/logout") // 로그아웃을 처리할 URL 지정
		            .logoutSuccessUrl("/") // 로그아웃 성공 후 리다이렉트될 URL 지정
		            .deleteCookies("QTABLE_SID") // 로그아웃 시 삭제할 쿠키 지정
		            .invalidateHttpSession(true) // HTTP 세션을 무효화할지 여부 (기본값 true)
		        )
				//403에러 페이지 처리, 추후 세부 구현 예정 
				.exceptionHandling(exception -> exception
		            .accessDeniedPage("/error/denied") // 1. 인가 실패(403) 시 이동할 페이지
                    // 2. 인증 실패(401) 시 처리할 핸들러
                    .authenticationEntryPoint((request, response, authException) -> {
                        String requestedWith = request.getHeader("X-Requested-with");

                        // AJAX 요청인지 확인
                        if ("XMLHttpRequest".equals(requestedWith)) {
                        	ObjectMapper objectMapper = new ObjectMapper();

                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");

                            // Map 객체를 사용하여 응답 데이터 생성
                            Map<String, Object> responseData = new HashMap<>();
                            responseData.put("error", "Unauthorized");
                            responseData.put("message", "로그인이 필요합니다 \n로그인 페이지로 이동하시겠습니까?");
                            responseData.put("redirectUrl", "/login");
                            
                            // ObjectMapper를 사용해 Map을 JSON 문자열로 변환하고 응답에 쓴다.
                            String jsonResponse = objectMapper.writeValueAsString(responseData);
                            response.getWriter().write(jsonResponse);
                        } else {
                            // AJAX 요청이 아니면 로그인 페이지로 리다이렉트 (기본 동작)
                            response.sendRedirect("/login");
                        }
                    })
			    )
				.userDetailsService(qtableUserDetailsService) //커스텀한 객체로 사용하기
				.build();
	}
	
	@Bean
	public AuthenticationFailureHandler authenticationFailureHandler() {
	    return (request, response, exception) -> {
	        String redirectUrl;
	        System.err.println("인증 실패 예외: " + exception.getClass().getName());
	        System.err.println("메시지: " + exception.getMessage());
	        if (exception instanceof AccountRestoreRequiredException) {
	            // 탈퇴 회원일 경우, 복구 페이지나 특정 쿼리 파라미터와 함께 리다이렉트
	            redirectUrl = "/login?error=DELETED_ACCOUNT";
	        } else {
	            // 그 외 다른 인증 예외 처리
	            redirectUrl = "/login?error=true";
	        }
	        
	        response.sendRedirect(redirectUrl);
	    };
	}
}

	



