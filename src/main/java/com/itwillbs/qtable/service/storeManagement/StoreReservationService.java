package com.itwillbs.qtable.service.storeManagement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.qtable.mapper.storeManagementMapper.StoreReservation;
import com.itwillbs.qtable.vo.storeManagement.ReservationVO;

@Service
public class StoreReservationService {

	@Autowired
	StoreReservation storeReservation;
	
	/* 페이지네이션에 필요한 카운팅 */
	public int countReservationByStoreIdx(int store_idx, String filter) {
		return storeReservation.countReservationByStoreIdx(store_idx, filter);
	}
	
	
	/* 해당 매장 idx로 예약 목록 가져오는 서비스 */
	public List<ReservationVO> selectReservationByStoreIdx(int store_idx, int start_row, int list_limit, String filter){
		return storeReservation.selectReservationByStoreIdx(store_idx, start_row, list_limit, filter);
	}
	
	/* 예약 정보 디테일 가져오는 서비스 */
	public ReservationVO selectReservationDetail(int reserve_idx, int store_idx) {
		return storeReservation.selectReservationDetail(reserve_idx, store_idx);
	}
	
	/* 예약 결과 갱신 서비스 */
	@Transactional
	public int updateReservationResult(ReservationVO reservationVo) {
		return storeReservation.updateReservationResult(reservationVo);
	}
	
	/* 해당 회원 노쇼 + 1 */
	@Transactional
	public int updateNoShowCount(int member_idx) {
		return storeReservation.updateNoShowCount(member_idx);
	}

}
