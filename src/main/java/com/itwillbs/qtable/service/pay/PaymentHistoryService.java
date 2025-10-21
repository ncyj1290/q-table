package com.itwillbs.qtable.service.pay;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.qtable.mapper.reservation.ReservationMapper;
import com.itwillbs.qtable.mapper.storeManagementMapper.StoreSubscribe;
import com.itwillbs.qtable.service.storeManagement.PaymentVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentHistoryService {

	private final ReservationMapper reservationMapper;
	
	private final StoreSubscribe storeSubscribe;

	/**
	 * 예약 결제 내역 기록 (별도 트랜잭션)
	 * 부모 트랜잭션이 롤백되어도 이 기록은 남음 (REQUIRES_NEW)
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void recordReservationPayment(
		Integer memberIdx,
		Integer amount,
		Integer reserveIdx,
		String status
	) {
		reservationMapper.insertPaymentHistory(memberIdx, amount, reserveIdx, status);
	}
	
	/* 구독권 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void recordSubscribePayment(
		int memberIdx,
		int amount,
		Integer subscribe_idx,
		String status
	) {
		PaymentVO pay = new PaymentVO();
		pay.setMember_idx(memberIdx);
		pay.setPayment_amount(amount);
		pay.setPay_status(status);
		
		storeSubscribe.insertNewPaylog(pay);
	}
	
}
