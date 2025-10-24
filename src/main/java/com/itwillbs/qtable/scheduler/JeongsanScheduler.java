package com.itwillbs.qtable.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.itwillbs.qtable.service.admin.JeongsanService;

@Component
public class JeongsanScheduler {

    @Autowired
    private JeongsanService jeongsanService;

    // 매주 수요일 새벽 2시에 실행 (cron = "초 분 시 일 월 요일")
    @Scheduled(cron = "0 0 2 * * WED")
    public void createWeeklyJeongsanRequests() {
    	
        try {
            // 정산 요청 생성 로직
            jeongsanService.processAutoJeongsanRequests();
            System.out.println("주간 자동 정산 요청 스케줄러 완료.");
        } catch (Exception e) {
            System.err.println("주간 자동 정산 요청 스케줄러 오류 발생: " + e.getMessage());
        }
    }
}
