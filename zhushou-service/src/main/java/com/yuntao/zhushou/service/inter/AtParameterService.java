package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.model.domain.AtParameter;
import com.yuntao.zhushou.model.query.AtParameterQuery;
import com.yuntao.zhushou.model.vo.AtParameterVo;
import com.yuntao.zhushou.common.web.Pagination;
import java.util.List;



/**
 * 模板参数服务接口
 * 
 * @author admin
 *
 * @2016-07-21 15
 */
public interface AtParameterService {

    /**
     * 查询列表
     * @param query
     * @return
     */
    List<AtParameter> selectList(AtParameterQuery query);


    /**
     * 分页查询
     * @param query
     * @return
     */
    Pagination<AtParameterVo> selectPage(AtParameterQuery query);

    /**
     * 根据id获得对象
     * @param id
     * @return
     */
    AtParameter findById(Long id);

    /**
     * 新增
     * @param atParameter
     * @return
     */
    int insert(AtParameter atParameter) ;

    /**
     * 根据id修改
     * @param atParameter
     * @return
     */
    int updateById(AtParameter atParameter);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int deleteById(Long id);


    /**
     * 根据活动id 物理删除
     * @param activeId
     * @return
     */
    int deleteByActiveId(Long activeId);


    /**
     * 修改参数列表
     * @param activeId
     * @param parameterList
     * @return
     */
    int updateParamList(Long activeId,List<AtParameter> parameterList);

}
