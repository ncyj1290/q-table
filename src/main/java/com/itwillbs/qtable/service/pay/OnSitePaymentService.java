package com.itwillbs.qtable.service.pay;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.qtable.entity.Image;
import com.itwillbs.qtable.entity.Member;
import com.itwillbs.qtable.entity.Payment;
import com.itwillbs.qtable.entity.Reservation;
import com.itwillbs.qtable.entity.Store;
import com.itwillbs.qtable.repository.ImageRepository;
import com.itwillbs.qtable.repository.MemberRepository;
import com.itwillbs.qtable.repository.PaymentRepository;
import com.itwillbs.qtable.repository.ReservationRepository;
import com.itwillbs.qtable.repository.StoreRepository;
import com.itwillbs.qtable.vo.storeManagement.ReservationVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OnSitePaymentService {

	private final PaymentRepository paymentRepository;
	private final StoreRepository storeRepository;
	private final ReservationRepository reservationRepository;
	private final MemberRepository memberRepository;
	private final ImageRepository imageRepository;

	// 매장명 조회
	public Map<String, Object> getStoreInfo(Integer storeIdx) {
		Store store = storeRepository.findById(storeIdx)
			.orElseThrow(() -> new RuntimeException("매장을 찾을 수 없습니다"));

		Map<String, Object> result = new HashMap<>();
		result.put("store_name", store.getStoreName());
		result.put("deposit", store.getDeposit());

		return result;
	}

	// 예약 정보 조회 (회원 + 매장 + 오늘 날짜)
	public ReservationVO getReservationInfo(Integer memberIdx, Integer storeIdx) {
		LocalDate today = LocalDate.now();

		Reservation reservation = reservationRepository
			.findByMemberIdxAndStoreIdxAndReserveDate(memberIdx, storeIdx, today)
			.orElseThrow(() -> new RuntimeException("오늘 예약이 없습니다"));

		return new ReservationVO(reservation);
	}
	
	// 사용자 큐머니 조회
	public int getMemberQMoney(Integer memberIdx) {
		
		Member member = memberRepository.findById(memberIdx).orElse(null);
		int qMoney = member.getQMoney();
		
		return qMoney;
	}

	// 매장 대표 이미지 조회
	public String getStoreImage(Integer storeIdx) {
		Image image = imageRepository.findByTargetTypeAndTargetIdx("imguse_01", storeIdx);
	    return (image == null || image.getImageUrl().isEmpty())
	        ? "/img/default-store.png"
	        : image.getImageUrl();
	}

	// 현장 결제 처리
	@Transactional
	public void processOnSitePayment(Integer memberIdx, Integer storeIdx, Integer amount) {
		// 1. 고객 조회 및 큐머니 잔액 확인

		// 2. 큐머니 차감/적립

		// 3. 결제 내역 저장
	}

}
