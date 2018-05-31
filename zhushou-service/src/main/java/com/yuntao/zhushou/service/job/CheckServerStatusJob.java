package com.yuntao.zhushou.service.job;

import com.yuntao.zhushou.common.constant.MsgConstant;
import com.yuntao.zhushou.common.utils.AppConfigUtils;
import com.yuntao.zhushou.common.web.MsgResponseObject;
import com.yuntao.zhushou.common.web.ws.AppObject;
import com.yuntao.zhushou.common.web.ws.HostObject;
import com.yuntao.zhushou.model.domain.App;
import com.yuntao.zhushou.model.domain.Company;
import com.yuntao.zhushou.model.domain.Host;
import com.yuntao.zhushou.model.enums.LogModel;
import com.yuntao.zhushou.model.query.CompanyQuery;
import com.yuntao.zhushou.service.inter.AppService;
import com.yuntao.zhushou.service.inter.CompanyService;
import com.yuntao.zhushou.service.inter.HostService;
import com.yuntao.zhushou.service.support.deploy.DZMessageHelperServer;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
    private DZMessageHelperServer dzMessageHelperServer;

    @Autowired
    private CompanyService companyService;

//    private AtomicBoolean execRun = new AtomicBoolean(false);

    //    public AtomicInteger pushMsgNum = new AtomicInteger(0);
    public Map<String, AtomicBoolean> execMap = new ConcurrentHashMap<>();

    @Scheduled(cron = "*/5 * * * * ?")
//    @Profile("prod")
    public void check() {
        String model = AppConfigUtils.getValue("profiles.active");
        if (!model.equals(LogModel.PROD.getCode())) {
            return;
        }
        doCheck();
    }

    public void doCheck() {
        try {
//            List<App> appList = appService.selectAllList();
//            Map<String, Map<String, ResponseObject>> resultMap = new HashMap<>();
            //list all company
            List<Company> companyList = companyService.selectList(new CompanyQuery());
            for (Company company : companyList) {
                //get all app by company id
                String key = company.getKey();
                AtomicBoolean atomicBoolean = execMap.get(key);
                if (atomicBoolean == null) {
                    atomicBoolean = new AtomicBoolean(false);
                    execMap.put(key, atomicBoolean);
                }
                //没有执行完就跳过
                if (!atomicBoolean.compareAndSet(false, true)) {
                    continue;
                }

                Long companyId = company.getId();
                List<App> appList = appService.selectByCompanyId(companyId);

                //list all app
                List<AppObject> appObjectList = new ArrayList<>(appList.size());
                for (App app : appList) {
                    AppObject appObject = new AppObject();
                    appObjectList.add(appObject);
                    appObject.setName(app.getName());
                    appObject.setPort(app.getPort());

                    List<Host> hostList = hostService.selectListByAppId(app.getId());

                    List<HostObject> hostObjectList = new ArrayList<>(hostList.size());
                    appObject.setHostObjectList(hostObjectList);

                    for (Host host : hostList) {
                        HostObject hostObject = new HostObject();
                        hostObject.setName(host.getName());
                        hostObject.setHost(host.getEth0());
                        hostObjectList.add(hostObject);
                    }
                }
                //send to client ws
                //推送到cp core
                if (CollectionUtils.isNotEmpty(appObjectList)) {
                    MsgResponseObject msgResponseObject = new MsgResponseObject();
                    msgResponseObject.setType(MsgConstant.ReqResType.CORE);
                    msgResponseObject.setKey(company.getKey());
                    msgResponseObject.setCode(MsgConstant.ResponseCode.NORMAL);
                    msgResponseObject.setBizType(MsgConstant.ResponseBizType.SERVER_STATUS_CHECK);
                    msgResponseObject.setData(appObjectList);
                    dzMessageHelperServer.offerSendMsg(msgResponseObject);
                }
            }

        } catch (Exception e) {
            log.error("checkServerError", e);
//            YTWebSocketServer.sendMessage(MsgConstant.ResponseBizType.SERVER_STATUS_CHECK, "error,message=" + e.getMessage());
        } finally {
//            execRun.set(false);
        }

    }
}
