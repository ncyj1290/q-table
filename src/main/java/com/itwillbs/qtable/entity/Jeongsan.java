package com.itwillbs.qtable.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "jeongsan")
@Getter
@Setter
public class Jeongsan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer jeongsanIdx;

    @Column(name = "member_idx")
    private Integer memberIdx;

    @Column(name = "jeongsan_amount")
    private Integer jeongsanAmount;
    
    @Column(name = "calculate_result")
    private String calculateResult;
    
    @Column(name = "requested_at")
    private LocalDateTime requestedAt;
    
    @Column(name = "processed_at")
    private LocalDateTime processedAt;
    
    @Column(name = "rejection_reason")
    private String rejectionReason;

}