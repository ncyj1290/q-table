package com.itwillbs.qtable.service.pay;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.itwillbs.qtable.config.QtableUserDetails;
import com.itwillbs.qtable.mapper.pay.PaymentMapper;
import com.itwillbs.qtable.util.PortOneClient;
import com.itwillbs.qtable.vo.myPage.PortOneVO;
import com.itwillbs.qtable.vo.myPage.payment;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PortOneService {

    private final PortOneClient portOneClient;  // 포트원 API 클라이언트
    private final PaymentMapper paymentMapper;
    private final PaymentService paymentService;

    public boolean verifyPayment(String impUid, String merchantUid, long amount, QtableUserDetails qtable) {
        // 포트원 API로 거래 정보 조회
    	PortOneVO resp = portOneClient.getPaymentByImpUid(impUid);

        if (resp == null || resp.getResponse() == null) {
            return false;
        }

        // 응답 값 검증
        if (!resp.getResponse().getMerchant_uid().equals(merchantUid)) {
            return false;
        }
        if (resp.getResponse().getAmount() != amount) {
            return false;
        }
        if (!"paid".equals(resp.getResponse().getStatus())) {
            return false;
        }

        // DB 저장
        PortOneVO.PaymentResponse response = resp.getResponse();
        QtableUserDetails qtables = (QtableUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        payment pay = new payment();
        pay.setMember_idx(qtables.getMember().getMemberIdx());
        pay.setPayment_amount((int) response.getAmount());
        pay.setPay_status("pyst_01");  // 상태 코드
        pay.setPay_way("pywy_01");
        pay.setPay_type("pyus_01");  // 결제 유형
        pay.setPay_reference(response.getMerchant_uid()); // Merchant UID
        pay.setExternal_transaction(response.getImp_uid()); // IMP UID
        pay.setItem_name(response.getItem_name());

        // DB 저장
        paymentService.savePortOne(pay);


        return true;
    }
}
