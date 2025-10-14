package com.itwillbs.qtable.service.pay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.qtable.mapper.pay.PaymentMapper;
import com.itwillbs.qtable.vo.myPage.payment;

@Service
public class PaymentService {

	@Autowired
	private PaymentMapper paymentMapper;
	
	public void savePayment(payment paymentVo) {
	   paymentMapper.insertPayment(paymentVo);
	}
	
}
