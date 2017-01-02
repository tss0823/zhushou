package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.model.domain.ReqContent;
import com.yuntao.zhushou.model.query.ReqContentQuery;
import com.yuntao.zhushou.model.vo.ReqContentVo;
import com.yuntao.zhushou.common.web.Pagination;
import java.util.List;



/**
 * 请求内容服务接口
 * 
 * @author admin
 *
 * @2016-08-13 21
 */
public interface ReqContentService {

    /**
     * 查询列表
     * @param query
     * @return
     */
    List<ReqContent> selectList(ReqContentQuery query);


    /**
     * 分页查询
     * @param query
     * @return
     */
    Pagination<ReqContentVo> selectPage(ReqContentQuery query);

    /**
     * 根据id获得对象
     * @param id
     * @return
     */
    ReqContent findById(Long id);

    /**
     * 新增
     * @param reqContent
     * @return
     */
    int insert(ReqContent reqContent) ;

    /**
     * 根据id修改
     * @param reqContent
     * @return
     */
    int updateById(ReqContent reqContent);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int deleteById(Long id);



}
