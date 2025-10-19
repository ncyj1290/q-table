package com.itwillbs.qtable.entity;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "reservation")
@Getter
@Setter
public class Reservation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer reserveIdx;
	private Integer storeIdx;
	private Integer memberIdx;
	private String reserveName;
	private String reserveEmail;
	private Date reserveDate;
	private Time reserveTime;
	private Timestamp createdAt;
	private String reserveResult;
	private Timestamp updatedAt;
	private Integer personCount;
	private String requirement;
	private String allergy;
	private String denyReason;

}
