package com.itwillbs.qtable.service.storeManagement;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.qtable.mapper.storeManagementMapper.StoreStatistic;

@Service
public class StoreStatisticService {

	@Autowired
	StoreStatistic storeStatistic;

	/* 매장 통계 정보 들고오는 서비스 로직 */
	public List<Map<String, Object>> selectStatisticDate(int store_idx, String s_cat) {
		
		/* 
		 	------------------------------------------------------------------
			srss_01	예약 횟수
			srss_04	방문 완료
			srss_02	노쇼 횟수
			srss_03	예약 취소 횟수
			srss_05	예약 거부 횟수
			------------------------------------------------------------------
			rsrt_01	방문 완료	예약 결과 - 방문 완료를/을 뜻하는 공통코드 입니다.
			rsrt_02	노쇼	예약 결과 - 노쇼를/을 뜻하는 공통코드 입니다.
			rsrt_03	예약 취소	예약 결과 - 예약 취소를/을 뜻하는 공통코드 입니다.
			rsrt_04	예약 거부	예약 결과 - 예약 거부를/을 뜻하는 공통코드 입니다.
			------------------------------------------------------------------
		 */
		
		String code = "";
		
		 switch (s_cat) {
			/* 노쇼 횟수 */
			case "srss_02": code = "rsrt_02"; break;
			/* 예약 취소 횟수 */
			case "srss_03": code = "rsrt_03"; break;
			/* 방문 완료 횟수 */
			case "srss_04": code = "rsrt_01"; break;
			/* 예약 거부 횟수 */
			case "srss_05": code = "rsrt_04"; break;
			/* 전체(srss_01) */
			case null: code = null;
			/* 추가 방어 */
			default: code = null; break;
		 }

		return storeStatistic.selectStatisticDate(store_idx, code);
	}

}
