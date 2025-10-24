package com.itwillbs.qtable.mapper.memberjoin;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.itwillbs.qtable.vo.commonCode.CommonCodeVO;

@Mapper
public interface MemerJoin {
    @Select("SELECT * FROM common_code")
    List<CommonCodeVO> selectAllCodes();

    // 특정 그룹코드 기준 조회
    @Select("SELECT * FROM common_code WHERE group_code = #{groupCode}")
    List<CommonCodeVO> selectByGroupCode(String groupCode);
}
