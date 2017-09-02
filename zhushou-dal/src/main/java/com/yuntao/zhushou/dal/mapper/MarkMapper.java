package com.yuntao.zhushou.dal.mapper;


import com.yuntao.zhushou.model.domain.Mark;
import com.yuntao.zhushou.model.vo.mark.MarkTopNVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 马克Mapper
 * @author admin
 *
 * @2017-08-31 15
 */
public interface MarkMapper extends BaseMapper<Mark> {

    List<MarkTopNVo> selectTopN(@Param("type") Integer type, @Param("startPeriods") Integer startPeriods,
                                @Param("endPeriods") Integer endPeriods, @Param("ascOrDesc") String ascOrDesc,
                                @Param("topN") Integer topN);

    int selectLastLocation(@Param("endPeriods") Integer endPeriods,@Param("val") Integer val);

    int selectLastItemIndex(@Param("endPeriods") Integer endPeriods);


}
