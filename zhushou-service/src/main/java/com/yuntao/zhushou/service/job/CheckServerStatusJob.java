package com.yuntao.zhushou.service.job;

import com.yuntao.zhushou.common.utils.AppConfigUtils;
import com.yuntao.zhushou.common.utils.HttpUtils;
import com.yuntao.zhushou.common.utils.JsonUtils;
import com.yuntao.zhushou.common.utils.ResponseObjectUtils;
import com.yuntao.zhushou.model.constant.AppConstant;
import com.yuntao.zhushou.model.domain.App;
import com.yuntao.zhushou.model.domain.Host;
import com.yuntao.zhushou.model.enums.LogModel;
import com.yuntao.zhushou.model.web.ResponseObject;
import com.yuntao.zhushou.service.inter.AppService;
import com.yuntao.zhushou.service.inter.HostService;
import com.yuntao.zhushou.service.support.YTWebSocketServer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by shan on 2016/2/23.
 */
@Component
public class CheckServerStatusJob {

    private final static Logger log = LoggerFactory.getLogger(CheckServerStatusJob.class);

    @Autowired
    private AppService appService;

    @Autowired
    private HostService hostService;

    @Autowired
    private YTWebSocketServer ytWebSocketServer;

    private AtomicBoolean execRun = new AtomicBoolean(false);

    @Scheduled(cron = "*/5 * * * * ?")
//    @Profile("prod")
    public void check() {
        if (!execRun.compareAndSet(false, true)) {
            return;
        }
        try {
            String model = AppConfigUtils.getValue("profiles.active");
            if (model.equals(LogModel.TEST.getCode())) {
                return;
            }
            List<App> appList = appService.selectAllList();
            Map<String, Map<String, ResponseObject>> resultMap = new HashMap<>();
            for (App app : appList) {
                List<Host> hosts = hostService.selectListByAppAndModel(app.getId(), model);
                Map<String, ResponseObject> statusMap = new HashMap<>();
                for (Host host : hosts) {
                    String checkUrl = "http://" + host.getEth0() + ":" + app.getPort() + "/checkServerStatus";
                    String result = "";
                    try {
                        List<String> lines = HttpUtils.reqGet(checkUrl);
                        result = StringUtils.join(lines, ",");
                    } catch (Exception e) {
                        result = e.getMessage();
                    }
                    boolean checkServerStatusIsOK = StringUtils.equals(result, "checkServerStatusIsOK");
                    ResponseObject responseObject = ResponseObjectUtils.buildResObject();
                    responseObject.setSuccess(checkServerStatusIsOK);
                    responseObject.setMessage(result);
                    statusMap.put(host.getName(), responseObject);

                }
                resultMap.put(app.getName(), statusMap);
            }
            String result = JsonUtils.object2Json(resultMap);

            //推送到前端
            ytWebSocketServer.sendMessage(AppConstant.ResponseType.SERVER_STATUS_CHECK, result);
        } catch (Exception e) {
            log.error("checkServerError", e);
            ytWebSocketServer.sendMessage(AppConstant.ResponseType.SERVER_STATUS_CHECK, "error,message=" + e.getMessage());
        } finally {
            execRun.set(false);
        }


    }
}
