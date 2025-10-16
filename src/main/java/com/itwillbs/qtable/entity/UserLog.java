package com.itwillbs.qtable.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLog {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer log_idx; 
	
	@Column(name="member_idx")
	private Integer member_idx; 
	
	@CreationTimestamp
	@Column(name="login_time")
	private LocalDateTime login_time ;
	
	@Column(name="ip_address")
	private String ip_address;
	
}
