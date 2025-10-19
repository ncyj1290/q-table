package com.itwillbs.qtable.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itwillbs.qtable.entity.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

	// 회원 + 매장 + 날짜로 예약 조회
	Optional<Reservation> findByMemberIdxAndStoreIdxAndReserveDate(
		Integer memberIdx, Integer storeIdx, LocalDate reserveDate);

}
