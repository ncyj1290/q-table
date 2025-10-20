package com.itwillbs.qtable.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.itwillbs.qtable.entity.Member;
import com.itwillbs.qtable.repository.MemberRepository;
import com.itwillbs.qtable.service.admin.AdminService;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class MemberCleanupScheduler {

    @Autowired
    private MemberRepository memberRepository; // 회원 조회용

    @Autowired
    private AdminService adminService; // 회원 삭제 로직 재사용

    // 매일 새벽 4시에 실행 (cron = "초 분 시 일 월 요일")
    @Scheduled(cron = "0 0 4 * * ?")
    public void deleteOldWithdrawnMembers() {
        System.out.println("탈퇴 후 3개월 경과 회원 삭제 스케줄러 시작: " + LocalDateTime.now());

        // 1. 삭제 대상 조회: 탈퇴 상태이고, 탈퇴한 지 3개월이 지난 회원
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);
        // MemberRepository에 아래 쿼리 메소드 추가 필요
        List<Member> membersToDelete = memberRepository.findByMemberStatusAndLeaveAtBefore("mstat_02", threeMonthsAgo);

        // 2. 조회된 회원들을 순회하며 삭제 처리
        for (Member member : membersToDelete) {
            try {
                System.out.println("회원 삭제 시도: idx=" + member.getMemberIdx());
                // MemberService의 회원 삭제 메소드 호출 (연관 데이터 포함 삭제)
                adminService.deleteStoreMember(member.getMemberIdx());
                System.out.println("회원 삭제 완료: idx=" + member.getMemberIdx());
            } catch (Exception e) {
                // 개별 회원 삭제 실패 시 로그 남기고 계속 진행
                System.err.println("회원 삭제 오류 (idx=" + member.getMemberIdx() + "): " + e.getMessage());
            }
        }
        System.out.println("탈퇴 회원 삭제 스케줄러 종료.");
    }
}
