package com.itwillbs.qtable.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name= "review")
@Getter
@Setter
public class Review {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "review_idx")
	private Integer reviewIdx;
	
	@Column(name = "member_idx")
	private Integer memberId;
	
	@Column(name = "store_idx")
	private Integer storeId;
	
	@Column(name = "create_at", updatable = false)
	private LocalDateTime createAt = LocalDateTime.now();

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "score")
    private Integer score;
    
    @Column(name = "like_count", columnDefinition = "INT DEFAULT 0")
    private Integer likeCount = 0;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_idx", insertable = false, updatable = false)
    private Store store;
    
    
}
