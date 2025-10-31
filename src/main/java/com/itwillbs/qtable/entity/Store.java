package com.itwillbs.qtable.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "store")
@Getter
@Setter
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer storeIdx;

    @Column(name = "member_idx")
    private Integer memberIdx;

    @Column(name = "store_name", length = 50)
    private String storeName;

    @Column(name = "store_phone", length = 20)
    private String storePhone;

    @Column(name = "post_code", length = 10)
    private String postCode;

    @Column(name = "full_address", length = 100)
    private String fullAddress;

    @Column(name = "sido", length = 50)
    private String sido;

    @Column(name = "sigungu", length = 50)
    private String sigungu;

    @Column(name = "store_seat")
    private Integer storeSeat;

    @Column(name = "account_number", length = 20)
    private String accountNumber;

    @Column(name = "deposit")
    private Integer  deposit;

    @Column(name = "store_content", columnDefinition = "TEXT")
    private String storeContent;

    @Column(name = "open_time", length = 30)
    private String openTime;

    @Column(name = "close_time", length = 30)
    private String closeTime;

    @Column(name = "price_avg")
    private Integer priceAvg;

    @Column(name = "qr_code", length = 255)
    private String qrCode;

    @Column(name = "is_accept")
    private boolean isAccept;

    @Column(name = "is_24hour")
    private boolean is24Hour;

    @Column(name = "bank_code", length = 30)
    private String bankCode;

    // --- 입점 신청 관련 컬럼 ---
    @Column(name = "store_status", length = 30)
    private String storeStatus;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Column(name = "applied_at")
    private LocalDateTime appliedAt;
    
    @Transient
    private double avgScore;
    
    @Transient
    private Integer reviewCount;
    
    //뷰
    @Transient
    private Image mainImage;
    
    @Transient
    private Long storeCount;

    @Transient
    private String imageUrl;
    
    @Transient
    private String regionCode;

}