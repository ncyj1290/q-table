package com.itwillbs.qtable.service.pay;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.itwillbs.qtable.config.QtableUserDetails;
import com.itwillbs.qtable.mapper.pay.PaymentMapper;
import com.itwillbs.qtable.util.PortOneClient;
import com.itwillbs.qtable.vo.myPage.PortOneVO;
import com.itwillbs.qtable.vo.myPage.PaymentVO;

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

        PortOneVO.PaymentResponse response = resp.getResponse();
        
        // 응답 값 검증
        if (!resp.getResponse().getMerchant_uid().equals(merchantUid)) {
            return false;
        }
        // 금액 검증
        if (response.getAmount() != amount) {
            return false;
        }
        if (!"paid".equals(resp.getResponse().getStatus())) {
            return false;
        }
        
        System.out.println("PortOne Response: " + response);
        
        // DB 저장
        PaymentVO pay = new PaymentVO();
        pay.setMember_idx(qtable.getMember().getMemberIdx());
        pay.setPayment_amount(response.getAmount()); 
        pay.setPay_status("pyst_01");  // 상태 코드
        pay.setPay_way("pywy_02");
        pay.setPay_type("pyus_01");  // 결제 유형
        pay.setPay_reference(response.getMerchant_uid()); // Merchant UID
        pay.setExternal_transaction_idx(response.getImp_uid()); // IMP UID
        pay.setItem_name(response.getItem_name());

        // DB 저장
        paymentService.savePortOne(pay);
        System.out.println("Inserting Payment: " + pay);

        
        return true;
    }
}
