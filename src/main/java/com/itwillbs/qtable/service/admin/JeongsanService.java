package com.itwillbs.qtable.service.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.qtable.mapper.admin.AdminMapper;
import com.itwillbs.qtable.mapper.admin.JeongsanMapper;
import com.itwillbs.qtable.vo.admin.JeongsanListVO;
import com.itwillbs.qtable.vo.admin.StoreListVO;

import jakarta.transaction.Transactional;

@Service
public class JeongsanService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private JeongsanMapper jeongsanMapper;


    //매주 실행될 자동 정산 요청 생성 로직
    @Transactional
    public void processAutoJeongsanRequests() {
    	
        // 매장 관리자 조회
    	List<StoreListVO> storeManagers = adminMapper.findStoreMembers();

        for (StoreListVO manager : storeManagers) {
            int currentQMoney = manager.getQ_money();

            // Q머니 잔액이 0보다 클 경우에만 정산 요청 생성
            if (currentQMoney > 0) {
                // jeongsan 테이블에 insert
                JeongsanListVO newJeongsan = new JeongsanListVO();
                newJeongsan.setMember_idx(manager.getMember_idx());
                newJeongsan.setJeongsan_amount(currentQMoney);
                newJeongsan.setCalculate_result("ctrt_02"); // '대기' 상태
                
                jeongsanMapper.insertJeongsan(newJeongsan);

                // member 테이블의 q_money를 0으로 UPDATE
                adminMapper.updateQMoney(manager.getMember_idx(), 0);

            }
        }
    }
}