package com.itwillbs.qtable.mapper.mypage;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.qtable.vo.myPage.ScrapVO;

@Mapper
public interface ScrapMapper {

    // 스크랩 등록
    int insertScrap(@Param("memberIdx") int memberIdx, @Param("storeIdx") int storeIdx);

    // 스크랩 해제
    int deleteScrap(@Param("memberIdx") int memberIdx, @Param("storeIdx") int storeIdx);

    // 스크랩 여부 확인
    int existsScrap(@Param("memberIdx") int memberIdx, @Param("storeIdx") int storeIdx);

    // 스크랩 목록 조회
    List<ScrapVO> selectScrapList(@Param("memberIdx") int memberIdx);

}
