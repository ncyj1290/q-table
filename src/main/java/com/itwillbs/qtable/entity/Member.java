package com.itwillbs.qtable.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Member {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer memberIdx;

    @Column(name = "member_type")
    private String memberType;

    @Column(name = "member_name")
    private String memberName;

    @Column(name = "member_id")
    private String memberId;
    
    @Column(name = "member_pw")
    private String memberPw;
    
    @Column(name = "gender")
    private String gender;
    
    @Column(name = "post_code")
    private String postCode;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "address_detail")
    private String addressDetail;
    
    @Column(name = "birth")
    private LocalDate birth;
    
    @Column(name = "email")
    private String email;
    
    @CreationTimestamp
    @Column(name = "signup_date", updatable = false)
    private LocalDateTime signupDate;
    
    @Column(name = "member_status")
    private String memberStatus;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "profile_img_url")
    private String profileImgUrl;
    
    @Column(name = "business_reg_no")
    private String businessRegNo;
    
    @Column(name = "mail_auth_status")
    private boolean mailAuthStatus;
    
    @Column(name = "marketing_agreed")
    private boolean marketingAgreed;
    
    @Column(name = "q_money")
    private int qMoney;
    
    @Column(name = "no_show_count")
    private int noShowCount;
    
    @Column(name = "leave_reason")
    private String leaveReason;
    
    @Column(name = "leave_at")
    private LocalDateTime leaveAt;
    
    @Column(name = "social_id")
    private String socialId;
}