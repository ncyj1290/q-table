package com.itwillbs.qtable.mapper.pay;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.itwillbs.qtable.vo.myPage.PaymentVO;

@Mapper
public interface PaymentMapper {

	int insertPayment(PaymentVO paymentVo);
	
	int insertPortOne(PaymentVO paymentVo);

	List<PaymentVO> selectPaymentsByMember(int memberIdx);
	
    // 카드 결제
    List<PaymentVO> selectPaymentsByMerchantUid(String merchantUid);

    // 환불
    int updatePaymentStatus(PaymentVO pay);

	
}
