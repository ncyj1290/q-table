package com.itwillbs.qtable.mapper.storeDetail;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StoreDetailMapper {

	// 매장의 owner(member_idx) 조회
	Integer getStoreMemberIdx(Integer storeIdx);

	// 매장 기본 정보 조회
	Map<String, Object> getStoreBasicInfo(Integer storeIdx);
	
	// 매장 프로필 이미지 조회
	List<String> getStoreProfileImage(Integer storeIdx);
	
	// 매장 이미지 조회
	List<String> getStoreImage(Integer storeIdx);

	// 매장 카테고리 조회
	List<String> getStoreCategories(Integer storeIdx);
	
	// 매장 휴일 조회
	List<String> getStoreHoliday(Integer storeIdx);

	// 매장 분위기 조회
	List<String> getStoreAtmosphere(Integer storeIdx);

	// 매장 편의시설 조회
	List<Map<String, Object>> getStoreAmenities(Integer storeIdx);
	
	// 매장 메뉴 조회
	List<Map<String, Object>> getMenu(@Param("storeIdx") Integer storeIdx, @Param("offset") Integer offset, @Param("size") Integer size);
	
	// 메뉴판 이미지 조회
	List<String> getMenuBoardImages (Integer storeIdx);
	
	// 메뉴 개수 조회
	int getMenuCount(Integer storeIdx);
	
	// 식자재 조회
	List<Map<String, Object>> getIngredients (Integer storeIdx);
 	
	// 리뷰 별점 정보 조회
	Map<String, Object> getReviewScoreInfo(Integer storeIdx);

	// 리뷰 별점 분포 조회
	List<Map<String, Object>> getScoreDistribution(Integer storeIdx);

	// 모든 예약가능 시간 조회
	List<String> getAllTimeCodes();

	// 영업 가능 시간 조회
	List<String> getTimeCodesBetween(@Param("openTime") String openTime, @Param("closeTime") String closeTime);

	// 매장 리뷰 조회
	List<Map<String, Object>> getReview(@Param("storeIdx") Integer storeIdx, @Param("sortType") String sortType, @Param("offset") Integer offset, @Param("size") Integer size, @Param("memberIdx") Integer memberIdx);

	// 리뷰 정렬 옵션 공통코드 조회
	List<Map<String, Object>> getReviewSortOptions();

	// 리뷰 개수 조회
	int getReviewCount(@Param("storeIdx") Integer storeIdx, @Param("sortType") String sortType);

	// 리뷰 추가
	void insertReview(Map<String, Object> reviewData);
	
	// 리뷰 이미지 추가
	void insertReviewImages(@Param("reviewIdx") Integer reviewIdx, @Param("imageList") List<String> imageList);
	
	// 스크랩 존재 여부 확인
	int checkScrapExists(@Param("storeIdx") Integer storeIdx, @Param("memberIdx") Integer memberIdx);
	
	// 스크랩 추가
	int insertScrap(@Param("storeIdx") Integer storeIdx, @Param("memberIdx") Integer memberIdx);
	
	// 스크랩 삭제
	int deleteScrap(@Param("storeIdx") Integer storeIdx, @Param("memberIdx") Integer memberIdx);
	
	// 리뷰 좋아요 존재 여부 확인
	int checkReviewLikeExists(@Param("reviewIdx") Integer reviewIdx, @Param("memberIdx") Integer memberIdx);

	// 리뷰 좋아요 삭제
	void deleteReviewLike(@Param("reviewIdx") Integer reviewIdx, @Param("memberIdx") Integer memberIdx);
	
	// 리뷰 좋아요 추가
	void insertReviewLike(@Param("reviewIdx") Integer reviewIdx, @Param("memberIdx") Integer memberIdx);
	
	// 리뷰 좋아요 개수 조회
	int getReviewLikeCount(Integer reviewIdx);

}
