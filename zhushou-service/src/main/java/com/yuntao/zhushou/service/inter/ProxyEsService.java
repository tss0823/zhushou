package com.yuntao.zhushou.service.inter;

import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.model.domain.ProxyContent;
import com.yuntao.zhushou.model.query.LogQuery;
import com.yuntao.zhushou.model.query.LogTextQuery;
import com.yuntao.zhushou.model.query.ProxyContentQuery;
import com.yuntao.zhushou.model.vo.LogWebVo;
import com.yuntao.zhushou.model.vo.ProxyContentVo;

import java.util.List;

/**
 * Created by shan on 2016/5/5.
 */
public interface ProxyEsService {

    Pagination<ProxyContentVo> selectByPage(ProxyContentQuery query);

    ProxyContentVo findById(String month, String model, String id);

    void addPorxyContent(ProxyContent proxyContent);
}
