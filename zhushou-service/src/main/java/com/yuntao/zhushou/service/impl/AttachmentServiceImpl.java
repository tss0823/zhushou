/*
 * 
 * 
 * 
 * 
 */

package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.dal.mapper.AttachmentMapper;
import com.yuntao.zhushou.model.domain.Attachment;
import com.yuntao.zhushou.model.query.AttachmentQuery;
import com.yuntao.zhushou.model.vo.AttachmentVo;
import com.yuntao.zhushou.service.inter.AttachmentService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class AttachmentServiceImpl extends AbstService implements AttachmentService {


    @Autowired
    private AttachmentMapper attachmentMapper;

    @Override
    public List<Attachment> selectList(AttachmentQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        return attachmentMapper.selectList(queryMap);
    }

    @Override
    public Attachment selectOne(AttachmentQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        List<Attachment> attachments = attachmentMapper.selectList(queryMap);
        if (CollectionUtils.isNotEmpty(attachments)) {
            return attachments.get(0);
        }
        return null;
    }

    @Override
    public Pagination<AttachmentVo> selectPage(AttachmentQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = attachmentMapper.selectListCount(queryMap);
        Pagination<AttachmentVo> pagination = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        if (totalCount == 0) {
            return pagination;
        }
        queryMap.put("pagination", pagination);
        List<Attachment> dataList = attachmentMapper.selectList(queryMap);
        List<AttachmentVo> newDataList = new ArrayList<>(dataList.size());
        pagination.setDataList(newDataList);
        for (Attachment attachment : dataList) {
            AttachmentVo attachmentVo = BeanUtils.beanCopy(attachment, AttachmentVo.class);
            newDataList.add(attachmentVo);
        }
        return pagination;
    }

    @Override
    public Attachment findById(Long id) {
        return attachmentMapper.findById(id);
    }


    @Override
    public int insert(Attachment attachment) {
        return attachmentMapper.insert(attachment);
    }

    @Override
    public int updateById(Attachment attachment) {
        return attachmentMapper.updateById(attachment);
    }

    @Override
    public int deleteById(Long id) {
        return attachmentMapper.deleteById(id);
    }


}