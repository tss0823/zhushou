package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.model.domain.IdocParam;
import com.yuntao.zhushou.model.domain.IdocUrl;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.model.param.IdocDataParam;
import com.yuntao.zhushou.model.query.IdocUrlQuery;
import com.yuntao.zhushou.model.vo.IdocUrlVo;
import com.yuntao.zhushou.model.web.Pagination;
import java.util.List;



/**
 * 接口主体服务接口
 * 
 * @author admin
 *
 * @2016-07-30 20
 */
public interface IdocUrlService {

    /**
     * 查询列表
     * @param query
     * @return
     */
    List<IdocUrl> selectList(IdocUrlQuery query);


    /**
     * 分页查询
     * @param query
     * @return
     */
    Pagination<IdocUrlVo> selectPage(IdocUrlQuery query);

    /**
     * 根据id获得对象
     * @param id
     * @return
     */
    IdocUrl findById(Long id);

    /**
     * 根据url and verion获得对象
     * @param url
     * @param ver
     * @return
     */
    IdocUrl findDocByUrlAndVer(String appName,String url,String ver);

    IdocUrl findEnumByUrl(String appName,String url);


    /**
     * 新增
     * @param idocUrl
     * @return
     */
    int insert(IdocUrl idocUrl) ;

    /**
     * 根据id修改
     * @param idocUrl
     * @return
     */
    int updateById(IdocUrl idocUrl);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 绑定
     */
    IdocUrlVo bind(String month, String model, String stackId);

    /**
     * 提交json格式文档
     */
    void submitDoc(String jsonDoc,User user);

    /**
     * 获取idocVo
     * @param id
     * @return
     */
    IdocUrlVo getIdocUrlVoById(Long id) ;

    /**
     * 保存
     */
    void save(IdocDataParam idocDataParam, User user);

    /**
     *修改
     */
    void update(IdocDataParam idocDataParam, User user);


    /**
     * 同步创建枚举
     * @param appName
     */
    void syncNew(String appName);


    /**
     * 同步更新枚举
     * @param appName
     * @param id
     */
    void syncUpdate(String appName, Long id);

    /**
     * 提交或修改枚举
     * @param appName
     */
    void submitEnum(Long companyId,String appName,String emunJson);
}
