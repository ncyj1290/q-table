package com.itwillbs.qtable.service.pay;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.qtable.mapper.pay.PaymentMapper;
import com.itwillbs.qtable.vo.myPage.PaymentVO;


@Service
@Transactional
public class PaymentService {

	@Autowired
	private PaymentMapper paymentMapper;

	// 카카오페이 결제 DB
	public void savePayment(PaymentVO paymentVo) {
		paymentMapper.insertPayment(paymentVo);
	}

	// 카드 결제 DB
	public void savePortOne(PaymentVO paymentVo) {
		paymentMapper.insertPortOne(paymentVo);
	}

	// 카드 결제
	public List<PaymentVO> findByMerchantUid(String merchantUid) {
		return paymentMapper.selectPaymentsByMerchantUid(merchantUid);
	}

	// 환불
	public void updatePaymentStatus(PaymentVO pay) {
	    paymentMapper.updatePaymentCancelStatus(pay);
	}


	public void refundPayment(PaymentVO pay, int refundAmount) {
        // 결제 상태 변경
        pay.setPay_status("pyst_02");
        paymentMapper.updatePaymentCancelStatus(pay);

        // Q-money 차감
        paymentMapper.decreaseQmoney(pay.getMember_idx(), refundAmount);
    }
	
	

}
