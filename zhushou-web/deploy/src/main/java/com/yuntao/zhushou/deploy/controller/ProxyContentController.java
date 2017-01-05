package com.yuntao.zhushou.deploy.controller;

import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.dal.annotation.NeedLogin;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.model.query.ProxyContentQuery;
import com.yuntao.zhushou.model.vo.ProxyContentVo;
import com.yuntao.zhushou.common.web.Pagination;
import com.yuntao.zhushou.common.web.ResponseObject;
import com.yuntao.zhushou.service.inter.AppService;
import com.yuntao.zhushou.service.inter.ProxyContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by shan on 2016/5/5.
 */
@RestController
@RequestMapping("proxy")
public class ProxyContentController extends BaseController {


    @Autowired
    private ProxyContentService reqContentService;

    @Autowired
    private AppService appService;

    @RequestMapping("list")
    @NeedLogin
    public ResponseObject list(ProxyContentQuery query) {
//        User user = userService.getCurrentUser();
//        query.setCompanyId(user.getCompanyId());
//        query.setPageSize(30);  //固定30条
        Pagination<ProxyContentVo> pagination = reqContentService.selectPage(query);
//        if(CollectionUtils.isNotEmpty(pagination.getDataList())){
//            for (ProxyContentVo reqContentVo : pagination.getDataList()) {
//                String resData = reqContentVo.getResData();
//                //json 格式化
//                ObjectMapper mapper = new ObjectMapper();
//                try {
//                    Object json = mapper.readValue(resData, Object.class);
//                    resData = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
//                } catch (IOException e) {
//                }
//                reqContentVo.setResData(resData);
//                //end
//
//            }
//        }
        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
        responseObject.setData(pagination);
        return responseObject;
    }

//    @RequestMapping("getDataById")
//    @NeedLogin
//    public ResponseObject getDataById(@RequestParam Long id) {
//        ProxyContent reqContent = reqContentService.findById(id);
//        ResponseObject responseObject = ResponseObjectUtils.buildResObject();
//        responseObject.setData(reqContent);
//        return responseObject;
//    }



}
