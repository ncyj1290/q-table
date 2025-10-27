package com.itwillbs.qtable.vo.admin;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.itwillbs.qtable.config.MaskingUtils;
import com.itwillbs.qtable.entity.Member; 

@Data
@NoArgsConstructor
public class MemberDetailVO {

    private Integer member_idx;
    private String member_type;
    private String member_name;
    private String member_id;
    private String gender;
    private String post_code;
    private String address;
    private String address_detail;
    private LocalDate birth;
    private String email;
    private LocalDateTime signup_date;
    private String business_reg_no;
    private String member_status;      // 코드 값
    private String member_status_name; // 코드 라벨
    private String leave_reason;
    private LocalDateTime leave_at;
    private boolean mail_auth_status;
    private boolean marketing_agreed;
    private int q_money;
    private int no_show_count;
    private String profile_img_url;
    
    private Integer store_idx;
    private String store_name;
    private String store_phone;
    private String full_address;
    private String sido;
    private String sigungu;
    private int store_seat;
    private String account_number;
    private int deposit;
    private String store_content;
    private String open_time;
    private String close_time;
    private int price_avg;
    private String qr_code;
    private boolean is_accept;
    private boolean is_24hour;
    private String bank_code;
    
    // 입점 신청 관리
    private String store_status;
    private String rejection_reason;
    private LocalDateTime processed_at;
    private LocalDateTime applied_at;

    public MemberDetailVO(Member entity) {
        this.member_idx = entity.getMemberIdx();
        this.member_type = entity.getMemberType();
        this.member_name = entity.getMemberName();
        this.member_id = entity.getMemberId();
        this.gender = entity.getGender();
        this.post_code = entity.getPostCode();
        this.address = entity.getAddress();
        this.address_detail = entity.getAddressDetail();
        this.birth = entity.getBirth();
        this.email = entity.getEmail();
        this.signup_date = entity.getSignupDate();
        this.business_reg_no = entity.getBusinessRegNo();
        this.member_status = entity.getMemberStatus();
        this.leave_reason = entity.getLeaveReason();
        this.leave_at = entity.getLeaveAt();
        this.mail_auth_status = entity.isMailAuthStatus();
        this.marketing_agreed = entity.isMarketingAgreed();
        this.q_money = entity.getQMoney();
        this.no_show_count = entity.getNoShowCount();
        
    }
    
    
 // 마스킹 로직을 적용하는 별도 메소드 (Service에서 호출)
    public void applyMasking() {
        if ("mstat_02".equals(this.member_status) &&
            this.leave_at != null &&
            this.leave_at.isAfter(LocalDateTime.now().minusMonths(3)))
        {
            this.member_name = MaskingUtils.maskName(this.member_name); // // 별도 MaskingUtils 유틸리티 클래스로 분리
            this.email = MaskingUtils.maskEmail(this.email);
            this.address = "탈퇴 회원 주소";
            this.address_detail = "";
            this.birth = null;
            this.post_code = "*****";
            this.business_reg_no = "*****";
            this.account_number = "*****";
        }
    }	
}