package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.model.domain.AtTemplate;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.model.query.AtTemplateQuery;
import com.yuntao.zhushou.model.vo.AtTemplateVo;

import java.util.List;



/**
 * 测试模板服务接口
 * 
 * @author admin
 *
 * @2016-07-21 15
 */
public interface AtTemplateService {

    /**
     * 查询列表
     * @param query
     * @return
     */
    List<AtTemplate> selectList(AtTemplateQuery query);


    /**
     * 分页查询
     * @param query
     * @return
     */
    Pagination<AtTemplateVo> selectPage(AtTemplateQuery query);

    /**
     * 根据id获得对象
     * @param id
     * @return
     */
    AtTemplate findById(Long id);

    /**
     * 新增
     * @param atTemplate
     * @return
     */
    int insert(AtTemplate atTemplate) ;

    /**
     * 根据id修改
     * @param atTemplate
     * @return
     */
    int updateById(AtTemplate atTemplate);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 保存模板
     * @param logIds
     * @param templateVo
     * @return
     */
    void save(AtTemplateVo templateVo, List<String> logIds);

    /**
     * 获取模板对象
     * @param templteId
     * @return
     */
    AtTemplateVo getTemplateVo(Long templteId);

    /**
     * 采集活动
     * @param id
     * @param appName
     * @param mobile
     * @param startTime
     * @param endTime
     * @param logStackIds
     */
    void collect(Long id,String companyKey, String model,Integer type, String appName, String mobile, String startTime,
                 String endTime,List<String> logStackIds,Integer orderIndex);

    /**
     * 保存活动排序
     * @param templateId
     * @param activeIds
     */
    void saveActiveSort(Long templateId, List<Long> activeIds);

    /**
     * 发起流程
     * @param id
     * @param user
     */
    void start(Long id,User user);

    void start2(Long id,User user);



}
