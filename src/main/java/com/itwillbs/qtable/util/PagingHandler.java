package com.itwillbs.qtable.util;

import java.util.function.IntSupplier;

import com.itwillbs.qtable.vo.PageVO;

/* 페이징 기능 구현 유틸 클래스 */
public class PagingHandler {
	
	/* 한 페이지당 표시할 게시물 수 */
	public final static int LIST_LIMIT = 10;
	/* 한 페이지에 표시할 목록 갯수 */
	public final static int PAGE_LIST_LIMIT = 10;
	
	public static PageVO pageHandler(int pageNum, IntSupplier count) {
		
		/* 공식은 나도 몰루? 그냥 잘 가져다 쓰세요 */
		int startRow = (pageNum - 1) * LIST_LIMIT;
		int countRes = count.getAsInt();
		
		System.out.println("Page List Count: " + countRes);
		
		int maxPage = countRes / LIST_LIMIT + (countRes % LIST_LIMIT > 0? 1: 0); 
		if(maxPage == 0) maxPage = 1;
		
		/* 현 페이지에서 보여줄 시작 페이지 번호 연산 */
		int startPage = (pageNum - 1) / PAGE_LIST_LIMIT * PAGE_LIST_LIMIT + 1;
		/* 현 페이지에서 보여줄 마지막 페이지 번호 계산 */
		int endPage = startPage + PAGE_LIST_LIMIT - 1;
		/* 마지막 페이지 번호 값이 최대 페이지 번호보다 클 경우, 마지막 페이지 번호를 최대 페이지 번호로 교체 */
		if(endPage > maxPage) endPage = maxPage;
		
		return new PageVO(countRes, PAGE_LIST_LIMIT, startRow, LIST_LIMIT, maxPage, startPage, endPage, pageNum);
	}

}
