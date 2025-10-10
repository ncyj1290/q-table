package com.itwillbs.qtable.vo.admin;

import java.time.LocalDateTime;

import com.itwillbs.qtable.entity.Store;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StoreDetailVO {
	
    private Integer store_idx;
    private Integer member_idx;
    private String store_name;
    private String store_phone;
    private String post_code;
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

    public StoreDetailVO(Store entity) {
        this.store_idx = entity.getStoreIdx();
        this.member_idx = entity.getMemberIdx();
        this.store_name = entity.getStoreName();
        this.store_phone = entity.getStorePhone();
        this.post_code = entity.getPostCode();
        this.full_address = entity.getFullAddress();
        this.sido = entity.getSido();
        this.sigungu = entity.getSigungu();
        this.store_seat = entity.getStoreSeat();
        this.account_number = entity.getAccountNumber();
        this.deposit = entity.getDeposit();
        this.store_content = entity.getStoreContent();
        this.open_time = entity.getOpenTime();
        this.close_time = entity.getCloseTime();
        this.price_avg = entity.getPriceAvg();
        this.qr_code = entity.getQrCode();
        this.is_accept = entity.isAccept();
        this.is_24hour = entity.is24hour();
        this.bank_code = entity.getBankCode();
        this.store_status = entity.getStoreStatus();
        this.rejection_reason = entity.getRejectionReason();
        this.processed_at = entity.getProcessedAt();
        this.applied_at = entity.getAppliedAt();
    }

}
