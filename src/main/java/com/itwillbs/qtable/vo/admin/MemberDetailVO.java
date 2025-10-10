package com.itwillbs.qtable.vo.admin;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private String member_status;
    private String leave_reason;
    private LocalDateTime leave_at;
    private boolean mail_auth_status;
    private boolean marketing_agreed;
    private int q_money;
    private int no_show_count;

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
}