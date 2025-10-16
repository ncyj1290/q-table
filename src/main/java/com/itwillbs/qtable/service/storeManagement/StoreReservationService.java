package com.itwillbs.qtable.service.storeManagement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.qtable.mapper.storeManagementMapper.StoreReservation;
import com.itwillbs.qtable.vo.storeManagement.ReservationVO;

@Service
public class StoreReservationService {

	@Autowired
	StoreReservation storeReservation;
	
	public int countReservationByStoreIdx(int store_idx) {
		return storeReservation.countReservationByStoreIdx(store_idx);
	}
	
	
	/* 해당 매장 idx로 예약 목록 가져오는 쿼리문*/
	public List<ReservationVO> selectReservationByStoreIdx(int store_idx, int start_row, int list_limit){
		return storeReservation.selectReservationByStoreIdx(store_idx, start_row, list_limit);
	}
	
	/* 예약 정보 디테일 가져오는 쿼리문 */
	public ReservationVO selectReservationDetail(int reserve_idx, int store_idx) {
		return storeReservation.selectReservationDetail(reserve_idx, store_idx);
	}
	

}
