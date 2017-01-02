package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.model.domain.IdocParam;
import com.yuntao.zhushou.model.query.IdocParamQuery;
import com.yuntao.zhushou.model.vo.IdocParamVo;
import com.yuntao.zhushou.common.web.Pagination;
import java.util.List;



/**
 * 接口请求参数服务接口
 * 
 * @author admin
 *
 * @2016-07-30 20
 */
public interface IdocParamService {

    /**
     * 查询列表
     * @param query
     * @return
     */
    List<IdocParam> selectList(IdocParamQuery query);


    /**
     * 分页查询
     * @param query
     * @return
     */
    Pagination<IdocParamVo> selectPage(IdocParamQuery query);

    /**
     * 根据id获得对象
     * @param id
     * @return
     */
    IdocParam findById(Long id);

    /**
     * 新增
     * @param idocParam
     * @return
     */
    int insert(IdocParam idocParam) ;

    /**
     * 根据id修改
     * @param idocParam
     * @return
     */
    int updateById(IdocParam idocParam);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int deleteById(Long id);


    /**
     * 根据父id删除
     * @param id
     * @return
     */
    int deleteByParentId(Long id);

    /**
     * 查询根据父id
     * @return
     */
    List<IdocParam> selectByParentId(Long parentId);
}
