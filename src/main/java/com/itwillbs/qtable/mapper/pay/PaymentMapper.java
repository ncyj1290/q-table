package com.itwillbs.qtable.mapper.pay;

import org.apache.ibatis.annotations.Mapper;

import com.itwillbs.qtable.vo.myPage.payment;

@Mapper
public interface PaymentMapper {

	int insertPayment(payment paymentVo);
	
}
