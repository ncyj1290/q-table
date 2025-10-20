package com.itwillbs.qtable.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "payment")
@Getter
@Setter
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentIdx;

    @Column(name = "member_idx")
    private Integer memberIdx;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "payment_amount")
    private int paymentAmount;

    @Column(name = "pay_status")
    private String payStatus;

    @Column(name = "pay_way")
    private String payWay;

    @Column(name = "pay_type")
    private String payType;

    @Column(name = "pay_reference")
    private String payReference;

    @Column(name = "reference_idx")
    private Integer referenceIdx;

    @Column(name = "external_transaction_idx")
    private String externalTransactionIdx;
}