package com.itwillbs.qtable.mapper.pay;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.qtable.vo.myPage.PaymentVO;

@Mapper
public interface PaymentMapper {

	int insertPayment(PaymentVO paymentVo);
	
	int insertPortOne(PaymentVO paymentVo);

	List<PaymentVO> selectPaymentsByMember(int memberIdx);
	
    // 카드 결제
    List<PaymentVO> selectPaymentsByMerchantUid(String merchantUid);

    // 환불 상태 업데이트
    int updatePaymentCancelStatus(PaymentVO pay);
    
    // q-money 차감
    int decreaseQmoney(int memberIdx, int amount);

    // member 테이블 q_money
	int increaseQmoney(@Param("memberIdx") int memberIdx, @Param("amount") int amount);

    
	int selectQmoneyByMemberIdx(@Param("memberIdx") int memberIdx);
    
    
    
    
    
    

	
}
