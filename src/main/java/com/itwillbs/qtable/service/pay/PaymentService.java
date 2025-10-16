package com.itwillbs.qtable.service.pay;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.qtable.mapper.pay.PaymentMapper;
import com.itwillbs.qtable.vo.myPage.payment;

@Service
public class PaymentService {

	@Autowired
	private PaymentMapper paymentMapper;

	// 카카오페이 결제 DB
	public void savePayment(payment paymentVo) {
		paymentMapper.insertPayment(paymentVo);
	}

	// 카드 결제 DB
	public void savePortOne(payment paymentVo) {
		paymentMapper.insertPortOne(paymentVo);
	}

	// 카드 결제
	public List<payment> findByMerchantUid(String merchantUid) {
		return paymentMapper.selectPaymentsByMerchantUid(merchantUid);
	}

}
