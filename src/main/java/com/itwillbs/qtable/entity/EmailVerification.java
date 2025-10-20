package com.itwillbs.qtable.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="email_verification")
@Getter
@Setter
public class EmailVerification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="email_verification_idx")
	private Integer emailVerification_idx;
	
	@Column(name ="member_idx")
	private Integer memberIdx;
	
	@Column(name ="email", nullable = false, length = 200 )
	private String email;
	
	@Column(name ="is_verified", nullable = false)
	private boolean isVerified;
	
	@Column(name ="create_at", nullable = false)
	@CreationTimestamp
	private LocalDateTime createAt;
	
	@Column(name ="expires_at")
	private LocalDateTime expiresAt;
	
	@Column(name ="verification_code", nullable = false, length = 100)
	private String verification_code;
		
}
