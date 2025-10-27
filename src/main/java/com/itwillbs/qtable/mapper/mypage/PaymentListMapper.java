package com.itwillbs.qtable.mapper.mypage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentListMapper {

	// 결제 내역 조회
	List<Map<String, Object>> getMyPaymentList(Map<String, Object> params);

}
