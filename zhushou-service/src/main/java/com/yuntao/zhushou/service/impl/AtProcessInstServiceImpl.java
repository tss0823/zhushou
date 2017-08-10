package com.yuntao.zhushou.service.impl;

import com.yuntao.zhushou.common.utils.BeanUtils;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.dal.mapper.AtProcessInstMapper;
import com.yuntao.zhushou.model.domain.AtProcessInst;
import com.yuntao.zhushou.model.query.AtProcessInstQuery;
import com.yuntao.zhushou.model.vo.AtProcessInstVo;
import com.yuntao.zhushou.service.inter.AtProcessInstService;
import com.yuntao.zhushou.service.inter.AtTemplateService;
import com.yuntao.zhushou.service.support.YTWebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 流程实例服务实现类
 *
 * @author admin
 * @2016-07-21 15
 */
@Service("atProcessInstService")
public class AtProcessInstServiceImpl implements AtProcessInstService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private AtProcessInstMapper atProcessInstMapper;

    @Autowired
    private AtTemplateService atTemplateService;

//    @Autowired
    private YTWebSocketServer YTWebSocketServer;

    @Override
    public List<AtProcessInst> selectList(AtProcessInstQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        return atProcessInstMapper.selectList(queryMap);
    }

    @Override
    public Pagination<AtProcessInstVo> selectPage(AtProcessInstQuery query) {
        Map<String, Object> queryMap = BeanUtils.beanToMap(query);
        long totalCount = atProcessInstMapper.selectListCount(queryMap);
        if (totalCount == 0) {
            return null;
        }
        Pagination<AtProcessInst> pagination = new Pagination<>(totalCount,
                query.getPageSize(), query.getPageNum());
        queryMap.put("pagination", pagination);
        List<AtProcessInst> dataList = atProcessInstMapper.selectList(queryMap);
        Pagination<AtProcessInstVo> newPageInfo = new Pagination<>(pagination);
        List<AtProcessInstVo> newDataList = new ArrayList<>(dataList.size());
        newPageInfo.setDataList(newDataList);
        pagination.setDataList(dataList);
        for (AtProcessInst atProcessInst : dataList) {
            AtProcessInstVo atProcessInstVo = BeanUtils.beanCopy(atProcessInst, AtProcessInstVo.class);
            newDataList.add(atProcessInstVo);
        }
        return newPageInfo;

    }

    @Override
    public AtProcessInst findById(Long id) {
        return atProcessInstMapper.findById(id);
    }


    @Override
    public int insert(AtProcessInst atProcessInst) {
        return atProcessInstMapper.insert(atProcessInst);
    }

    @Override
    public int updateById(AtProcessInst atProcessInst) {
        return atProcessInstMapper.updateById(atProcessInst);
    }

    @Override
    public int deleteById(Long id) {
        return atProcessInstMapper.deleteById(id);
    }



    public static void main(String[] args) {
        String[] strings = "data.records[0].id".split("\\.");
        System.out.printf("strings=" + strings);
    }

}
