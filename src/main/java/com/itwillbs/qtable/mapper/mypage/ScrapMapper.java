package com.itwillbs.qtable.mapper.mypage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ScrapMapper {

    // 스크랩 등록
    int insertScrap(@Param("memberIdx") int memberIdx, @Param("storeIdx") int storeIdx);

    // 스크랩 해제
    int deleteScrap(@Param("memberIdx") int memberIdx, @Param("storeIdx") int storeIdx);

    // 스크랩 여부 확인
    int existsScrap(@Param("memberIdx") int memberIdx, @Param("storeIdx") int storeIdx);

    // 스크랩 목록 조회
    List<Map<String, Object>> selectScrapList(@Param("member_idx") int memberIdx);


}
