package com.itwillbs.qtable.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.itwillbs.qtable.service.storeManagement.StoreSubscribeService;

/* 매일 정각에 만료된 구독권 삭제하는 스케줄러 */
@Component
public class SubscribeScheduler {

	@Autowired
	StoreSubscribeService storeSubscribeService;
	
	/* 실제 운영용, 하루 한 번 정각에 */
	@Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
	public void delExpiredSubscribes() {
		System.out.println("Start Subscribe Managing Scheduler");
		int result = storeSubscribeService.deleteExpiredSubscribe();
		System.out.printf("Subscribe Delete Done, %d Subscribe Removed", result);
	}
	
	/* 테스트용, 10초에 한번씩 */
//	@Scheduled(cron = "*/10 * * * * *", zone = "Asia/Seoul")
//	public void delExpiredSubscribesTest() {
//		System.out.println("Start Subscribe Managing Scheduler Tester");
//		int result = storeSubscribeService.deleteExpiredSubscribe();
//		System.out.printf("(Tester)Subscribe Delete Done, %d Subscribe Removed", result);
//	}
	
}
