package com.yuntao.zhushou.dal.mapper;

import com.yuntao.zhushou.model.domain.ProxyContent;
import com.yuntao.zhushou.model.domain.ReqContent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 代理内容Mappper
 * 
 * @author admin
 *
 * @2016-08-13 21
 */
public interface ProxyContentMapper extends BaseMapper<ProxyContent> {


    int insertBatch(@Param("list") List<ProxyContent> dataList);


}
