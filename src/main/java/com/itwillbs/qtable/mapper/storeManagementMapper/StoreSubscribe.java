package com.itwillbs.qtable.mapper.storeManagementMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.qtable.service.storeManagement.PaymentVO;
import com.itwillbs.qtable.vo.storeManagement.SubscribeVO;


/* 매장 구독권 구매 관렴 매퍼 */
@Mapper
public interface StoreSubscribe {
	
	/* 구독정보 찾아오는 쿼리문 */
	SubscribeVO selectSubscribe(@Param("member_idx") int member_idx);
	
	/* 구매 기록 남기는 쿼리문 */
	int insertNewPaylog(PaymentVO paymentVo);
		
	/* 구독권 있나 확인하는 쿼리문 */
	int checkSubscribe(@Param("member_idx") int member_idx);
	
	/* 사용자 QMONEY 들고오기 */
	int selectQmoneyByMemberidx(@Param("member_idx") int member_idx);
	
	/* QMONEY 갱신(감소) */
	int reduceUserQMoney(@Param("member_idx") int member_idx, @Param("cost") int cost);
	
	/* 새로운 구독 정보 삽입 */
	int insertNewSubscribe(SubscribeVO subscribeVo);

	/* 기존 구독 정보 갱신 */
	int updateSubscribe(SubscribeVO subscribeVo);
	
}
