package com.itwillbs.qtable.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.qtable.entity.EmailVerification;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long>{
		//해당 이메일로 저장된 인증 기록 중에서 가장 최근에 생성된 한 건 조회
	    EmailVerification findTopByEmailOrderByCreateAtDesc(String email);
	
}
