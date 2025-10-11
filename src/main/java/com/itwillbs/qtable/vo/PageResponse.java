package com.itwillbs.qtable.vo;

import java.util.List;

/**
 * 페이지네이션 공통 응답 클래스
 *
 * 사용 예시:
 * <pre>
 * List<ReviewVO> reviews = reviewService.getReviews(page, size);
 * int totalCount = reviewService.getTotalCount();
 * return new PageResponse<>(reviews, page, size, totalCount);
 * </pre>
 *
 * @param <T> 데이터 타입
 */
public class PageResponse<T> {

	private List<T> data;           // 실제 데이터 목록
	private int currentPage;        // 현재 페이지 (1부터 시작)
	private int pageSize;           // 페이지당 개수
	private long totalCount;        // 전체 데이터 개수
	private int totalPages;         // 전체 페이지 수
	private boolean hasNext;        // 다음 페이지 존재 여부
	private boolean hasPrevious;    // 이전 페이지 존재 여부

	/**
	 * 페이지네이션 응답 생성
	 * @param data 데이터 목록
	 * @param currentPage 현재 페이지 (1부터 시작)
	 * @param pageSize 페이지당 개수
	 * @param totalCount 전체 데이터 개수
	 */
	public PageResponse(List<T> data, int currentPage, int pageSize, long totalCount) {
		this.data = data != null ? data : List.of();
		this.currentPage = Math.max(1, currentPage);  // 최소값 1
		this.pageSize = Math.max(1, pageSize);  // 최소값 1 (나누기 0 방지)
		this.totalCount = Math.max(0, totalCount);  // 최소값 0

		this.totalPages = (int) Math.ceil((double) this.totalCount / this.pageSize);  // 전체 페이지 수 계산
		this.hasPrevious = this.currentPage > 1;  // 이전 페이지 여부 계산
		this.hasNext = this.currentPage < this.totalPages;  // 다음 페이지 여부 계산
	}

	// 빈 페이지 응답 생성
	public static <T> PageResponse<T> empty() {
		return new PageResponse<>(List.of(), 1, 10, 0);
	}

	// Getter
	public List<T> getData() {
		return data;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public boolean isHasNext() {
		return hasNext;
	}

	public boolean isHasPrevious() {
		return hasPrevious;
	}

	// 시작 인덱스 계산 (LIMIT offset 용)
	public static int getOffset(int page, int size) {
		return (page - 1) * size;
	}
}
