package com.itwillbs.qtable.mapper.pay;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.itwillbs.qtable.vo.myPage.payment;

@Mapper
public interface PaymentMapper {

	int insertPayment(payment paymentVo);
	
	int insertPortOne(payment paymentVo);

	List<payment> selectPaymentsByMember(int memberIdx);
	
    // 카드 결제
    List<payment> selectPaymentsByMerchantUid(String merchantUid);
	
}
