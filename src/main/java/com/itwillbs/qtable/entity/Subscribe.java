package com.itwillbs.qtable.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "subscribe")
@Getter
@Setter
public class Subscribe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer subscribeIdx;

    @Column(name = "store_idx")
    private Integer storeIdx;

    @Column(name = "subscribe_start")
    private LocalDate subscribeStart;
    
    @Column(name = "subscribe_end")
    private LocalDate subscribeEnd;

}