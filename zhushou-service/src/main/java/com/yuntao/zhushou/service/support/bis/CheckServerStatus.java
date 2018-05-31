package com.yuntao.zhushou.service.support.bis;

import com.yuntao.zhushou.common.utils.AppConfigUtils;
import com.yuntao.zhushou.common.utils.JsonUtils;
import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.common.web.ResponseObject;
import com.yuntao.zhushou.model.domain.App;
import com.yuntao.zhushou.model.domain.Host;
import com.yuntao.zhushou.service.inter.AppService;
import com.yuntao.zhushou.service.inter.HostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shan on 2016/2/23.
 */
//@Component("checkServerStatus")
public class CheckServerStatus {

    private final static Logger log = LoggerFactory.getLogger(CheckServerStatus.class);

    @Autowired
    private AppService appService;

    @Autowired
    private HostService hostService;

//    @Autowired
//    private YTWebSocketServer ytWebSocketServer;

//    @PostConstruct
    public void init(){
        while (true){
            check();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
        }
    }

    public void check() {
        List<App> appList = appService.selectAllList();
        String model = AppConfigUtils.getValue("profiles.active");
//        if (model.equals(LogModel.TEST.getCode())) {
//            return;
//        }
        Map<String,Map<String,ResponseObject>> resultMap = new HashMap<>();
        for (App app : appList) {
            List<Host> hosts = hostService.selectListByAppAndModel(app.getId(), model);
            Map<String,ResponseObject> statusMap = new HashMap<>();
            for (Host host : hosts) {
//                String checkUrl = "http://"+host.getEth0()+":"+app.getPort()+"/checkServerStatus" ;
//                List<String> lines = HttpUtils.reqGet(checkUrl);
//                String result = StringUtils.join(lines,",");
//                boolean checkServerStatusIsOK = StringUtils.equals(result, "checkServerStatusIsOK");
                ResponseObject responseObject = ResponseObjectUtils.buildResObject();
                responseObject.setSuccess(true);
                responseObject.setMessage("OK");
                statusMap.put(host.getName(),responseObject);

            }
            resultMap.put(app.getName(),statusMap);
        }
        String result = JsonUtils.object2Json(resultMap);

        //推送到前端
//        ytWebSocketServer.sendMessage(MsgConstant.ResponseBizType.SERVER_STATUS_CHECK,result);
    }
}
