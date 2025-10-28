package com.itwillbs.qtable.service.storeManagement;


import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.qtable.mapper.storeManagementMapper.StoreSubscribe;
import com.itwillbs.qtable.service.pay.PaymentHistoryService;
import com.itwillbs.qtable.vo.storeManagement.SubscribeVO;

/* 매장 구독권 구매 관련 서비스들 */
@Service
public class StoreSubscribeService {
	
	@Autowired
	StoreSubscribe storeSubscribe;
	
	@Autowired
	PaymentHistoryService paymentHistoryService;
	
	/* 구독권 있나 없나 확인하는 서비스 */
	public int checkSubscribe(int member_idx) {
		return storeSubscribe.checkSubscribe(member_idx);
	}
	
	/* 구독권 정보 가져오는 서비스 */
	public SubscribeVO selectSubscribe(int member_idx) {
		return storeSubscribe.selectSubscribe(member_idx);
	}
	
	/* 맴버 QMONEY 겟또다재 */
	public int selectQmoneyByMemberidx(int member_idx) {
		return storeSubscribe.selectQmoneyByMemberidx(member_idx);
	}
	
	/* 만료된 구독권 삭제 하는 서비스 */
	public int deleteExpiredSubscribe() {
		return storeSubscribe.deleteExpiredSubscribe();
	}
	
	
	/* 구독권 구매 처리 서비스 */
	@Transactional
	public boolean purchaseSubscribe(int member_idx, int cost, int plusDate) {
		
		try {
			
			/* 기존 구독 정보 있나 확인 */
			int subCount = storeSubscribe.checkSubscribe(member_idx);
			boolean isSubscribe = subCount == 1;
			
			/* 기존 구독 여부 확인해서 VO, StartDay, EndDay 설정 */
			SubscribeVO sub = isSubscribe? storeSubscribe.selectSubscribe(member_idx) : new SubscribeVO();
			LocalDateTime today = LocalDateTime.now();
			
			/* 만기일이 현재시간보다 늦으면 활성화, 아니면 비활성화 상태 */
			boolean isActive = isSubscribe && sub.getSubscribe_end().toLocalDateTime().isAfter(today);
			
			/* 시작일, 만기일 */
			LocalDateTime startDay = isActive? sub.getSubscribe_start().toLocalDateTime() : today;
			LocalDateTime endDay = isActive? sub.getSubscribe_end().toLocalDateTime().plusDays(plusDate) : startDay.plusDays(plusDate);

			/* VO 데이터 세팅 */
			sub.setMember_idx(member_idx);
			sub.setSubscribe_start(Timestamp.valueOf(startDay));
			sub.setSubscribe_end(Timestamp.valueOf(endDay));

			/* 신규는 insert, 기존 구독자는 갱신으로 처리 */
			int res = isSubscribe? storeSubscribe.updateSubscribe(sub) : storeSubscribe.insertNewSubscribe(sub);
			
			/* 큐머니 감소 */
			int qRes = storeSubscribe.reduceUserQMoney(member_idx, cost);
			
			/* 결제 기록 */
			PaymentVO paymentVo = new PaymentVO();
			paymentVo.setMember_idx(member_idx);
			paymentVo.setPayment_amount(cost);
			paymentVo.setReference_idx(sub.getSubscribe_idx());
			
			int payRes = storeSubscribe.insertNewPaylog(paymentVo);
			
		}catch (Exception e) {
			/* 중간에 문제 터지면 이걸로 기록 남기게 됨 -> 떙큐 영재 게이야 */
			paymentHistoryService.recordSubscribePayment(member_idx, cost, null, "pyst_02");
		}
		
		return true;
	}
	

}
