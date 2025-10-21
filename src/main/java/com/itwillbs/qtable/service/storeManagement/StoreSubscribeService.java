package com.itwillbs.qtable.service.storeManagement;


import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.qtable.mapper.storeManagementMapper.StoreSubscribe;
import com.itwillbs.qtable.vo.storeManagement.SubscribeVO;



@Service
public class StoreSubscribeService {
	
	@Autowired
	StoreSubscribe storeSubscribe;
	
	/* 구독권 정보 가져오는 서비스 */
	public SubscribeVO selectSubscribe(int member_idx) {
		return storeSubscribe.selectSubscribe(member_idx);
	}
	
	/* 맴버 QMONEY 겟또다재 */
	public int selectQmoneyByMemberidx(int member_idx) {
		return storeSubscribe.selectQmoneyByMemberidx(member_idx);
	}
	
	/* 구독권 구매 처리 서비스 */
	@Transactional
	public boolean purchaseSubscribe(int member_idx, int cost, int plusDate) {

		/* 기존 구독 정보 있나 확인 */
		int subCount = storeSubscribe.checkSubscribe(member_idx);
		boolean isSubscribe = subCount == 1;
		
		/* 기존 구독 여부 확인해서 VO, StartDay, EndDay 설정 */
		SubscribeVO sub = isSubscribe? storeSubscribe.selectSubscribe(member_idx) : new SubscribeVO();
		
		System.out.println("Check Sub TO STring: " + sub.toString());
		
		LocalDateTime today = LocalDateTime.now();
		
		/* 만기일이 현재시간보다 늦으면 활성화, 아니면 비활성화 상태 */
		boolean isActive = isSubscribe && sub.getSubscribe_end().toLocalDateTime().isAfter(today);
		
		/* 시작일, 만기일 */
		LocalDateTime startDay = isActive? sub.getSubscribe_start().toLocalDateTime() : today;
		LocalDateTime endDay = isActive? sub.getSubscribe_end().toLocalDateTime().plusDays(plusDate) : startDay.plusDays(plusDate);
		
		System.out.println("Check StartDay: " + startDay);
		System.out.println("Check EndDay: " + endDay);
		
		/* VO 데이터 세팅 */
		sub.setMember_idx(member_idx);
		sub.setSubscribe_start(Timestamp.valueOf(startDay));
		sub.setSubscribe_end(Timestamp.valueOf(endDay));
		
		System.out.println("Check MemberIdx In VO: " + sub.getMember_idx());
		System.out.println("Check StartDay In VO: " + sub.getSubscribe_start());
		System.out.println("Check EndDay In VO: " + sub.getSubscribe_end());
		
		/* 신규는 insert, 기존 구독자는 갱신으로 처리 */
		int res = isSubscribe? storeSubscribe.updateSubscribe(sub) : storeSubscribe.insertNewSubscribe(sub);
		
		/* 큐머니 감소 */
		int qRes = storeSubscribe.reduceUserQMoney(member_idx, cost);
		
		return true;
	}
	

	
}
