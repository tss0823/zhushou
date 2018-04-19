/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.model.domain.Attachment;
import com.yuntao.zhushou.model.query.AttachmentQuery;
import com.yuntao.zhushou.model.vo.AttachmentVo;

import java.util.List;


/**
 * 附件 服务接口
 * @author admin
 *
 * @2018-04-08 18
 */
public interface AttachmentService {
	
    /**
     * 查询列表
     *
     * @param query
     * @return
     */
    List<Attachment> selectList(AttachmentQuery query);

    /**
     * 查询对象
     *
     * @param query
     * @return
     */
    Attachment selectOne(AttachmentQuery query);


    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    Pagination<AttachmentVo> selectPage(AttachmentQuery query);

    /**
     * 根据id获得对象
     *
     * @param id
     * @return
     */
    Attachment findById(Long id);

    /**
     * 新增
     *
     * @param attachment
     * @return
     */
    int insert(Attachment attachment);

    /**
     * 根据id修改
     *
     * @param attachment
     * @return
     */
    int updateById(Attachment attachment);

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    int deleteById(Long id);


    

}

