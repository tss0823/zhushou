package com.yuntao.zhushou.service.support.deploy;

import com.yuntao.zhushou.common.cache.CacheService;
import com.yuntao.zhushou.common.cache.QueueService;
import com.yuntao.zhushou.common.constant.CacheConstant;
import com.yuntao.zhushou.common.constant.MsgConstant;
import com.yuntao.zhushou.common.utils.ExceptionUtils;
import com.yuntao.zhushou.common.utils.JsonUtils;
import com.yuntao.zhushou.common.utils.SystemUtils;
import com.yuntao.zhushou.common.web.MsgRequestObject;
import com.yuntao.zhushou.common.web.MsgResponseObject;
import com.yuntao.zhushou.model.domain.Company;
import com.yuntao.zhushou.model.domain.User;
import com.yuntao.zhushou.service.inter.CompanyService;
import com.yuntao.zhushou.service.inter.WsMsgHandlerService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Created by shan on 2016/8/19.
 */
@Component
public class DZMessageHelperServer  {


    private final static Logger bisLog = org.slf4j.LoggerFactory.getLogger("bis");

    private final static Logger log = org.slf4j.LoggerFactory.getLogger(DZMessageHelperServer.class);

    @Autowired
    private DZWebSocketServer dzWebSocketServer;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private QueueService queueService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private WsMsgHandlerService wsMessageService;


//    @Autowired
//    private MessageService messageService;

//    @Autowired
//    private MsgSendService msgSendService;

//    @Autowired
//    private UserActiveTaskDataService userActiveTaskDataService;

//    @Autowired
//    private UserService userService;

//    private String key;

//    @Value("${msg.batchStoreSize}")
    private Integer batchStoreSize = 100;

    /**
     * 用户webSocket容器
     */
//    private Map<Long, FTWebSocket> userWebSocketMap = new HashMap<>();

    /**
     * 同一个用户,多个客户端webSocket容器
     */
//    private Map<String, FTWebSocket> multiWebScoketMap = new HashMap<>();

    /**
     * 系统webSocket容器
     */
    private Map<String, DZWebSocket> sysWebSocketMap = new HashMap<>();

//    /**
//     * 系统服务批次号
//     */
//    private String batchNo = SystemUtil.getIp() + "_" + DateUtil.getFmt(new Date().getTime(), "yyyy-MM-dd HH:mm");

    /**
     * 是否可以处理消息
     */
//    private AtomicBoolean canProcessMsg = new AtomicBoolean(true);
    private boolean canProcessMsg = true;

    private boolean finishProcessMsg = false;


    public void sendFeedbackErrorMsg(DZWebSocket fTWebSocket, String message, String code, String descMsg) {
        bisLog.info(message);
        bisLog.info(descMsg);
        MsgResponseObject responseObject = new MsgResponseObject();
        responseObject.setSuccess(false);
        responseObject.setBizType(MsgConstant.ResponseBizType.NORMAL);
        responseObject.setCode(code);
        responseObject.setMessage(message);
        String json = JsonUtils.object2Json(responseObject);
        if (fTWebSocket.getWebSocket().getReadyState().name().equals(WebSocket.READYSTATE.OPEN.name())) {
            fTWebSocket.getWebSocket().send(json);
        }
    }

    public void sendFeedbackSuccessMsg(DZWebSocket fTWebSocket, String message, String code, String descMsg) {
        bisLog.info(message);
        bisLog.info(descMsg);
        MsgResponseObject responseObject = new MsgResponseObject();
        responseObject.setSuccess(true);
        responseObject.setBizType(MsgConstant.ResponseBizType.NORMAL);
        if (StringUtils.isEmpty(code)) {
            code = MsgConstant.ResponseCode.NORMAL;
        }
        responseObject.setCode(code);
        responseObject.setMessage(message);
        String json = JsonUtils.object2Json(responseObject);
        if (fTWebSocket.getWebSocket().getReadyState().name().equals(WebSocket.READYSTATE.OPEN.name())) {
            fTWebSocket.getWebSocket().send(json);
        }
    }

    public void open(DZWebSocket fTWebSocket, String path) {
        WebSocket conn = fTWebSocket.getWebSocket();
        //
        sysWebSocketMap.put(fTWebSocket.getId(), fTWebSocket);

        //url校验合法
        if (StringUtils.isEmpty(path)) {
            String errMsg = "path is null,key=" + fTWebSocket.getKey();
            this.sendFeedbackErrorMsg(fTWebSocket, "连接失败【参数不合法】", MsgConstant.ResponseCode.SYSTEM_ERROR, errMsg);
            return;
        }
        String paramPath = path.substring(path.indexOf("?") + 1);
        if (StringUtils.isEmpty(paramPath)) {
            String errMsg = "path is null,path=" + path + ",key=" + fTWebSocket.getKey();
            this.sendFeedbackErrorMsg(fTWebSocket, "连接失败【参数不合法】", MsgConstant.ResponseCode.SYSTEM_ERROR, errMsg);
            return;
        }

        if (!canProcessMsg) {  //已连接上，拒绝处理 //TODO
            writeSystemMsg("服务器升级处理,拒绝连接", fTWebSocket.toString());
            conn.close(1, "服务器升级处理");  //关闭
            return;
        }

        List<NameValuePair> list = URLEncodedUtils.parse(paramPath, Charset.defaultCharset());
        Map<String, String> paramMap = new HashMap();
        for (NameValuePair nameValuePair : list) {
            String name = nameValuePair.getName();
            String value = nameValuePair.getValue();
            paramMap.put(name, value);
        }
        String token = paramMap.get("token");
        if (StringUtils.isEmpty(token)) {
            String errMsg = "token is empty,path=" + path + ",key=" + fTWebSocket.getKey();
            this.sendFeedbackErrorMsg(fTWebSocket, "连接失败【token不能为空】", MsgConstant.ResponseCode.SYSTEM_ERROR, errMsg);
            return;
        }
        String platform = paramMap.get("platform");
        if (StringUtils.isEmpty(token)) {
            String errMsg = "platform is empty,path=" + path + ",key=" + fTWebSocket.getKey();
            this.sendFeedbackErrorMsg(fTWebSocket, "连接失败【platform不能为空】", MsgConstant.ResponseCode.SYSTEM_ERROR, errMsg);
            return;
        }

        if (!platform.equals(MsgConstant.ReqResType.CORE) && !platform.equals(MsgConstant.ReqResType.USER)) {
            String errMsg = "platform is not validate,path=" + path + ",key=" + fTWebSocket.getKey();
            this.sendFeedbackErrorMsg(fTWebSocket, "连接失败【platform不合法】", MsgConstant.ResponseCode.SYSTEM_ERROR, errMsg);
            return;
        }

        //bis validate
        if (StringUtils.equals(platform, MsgConstant.ReqResType.USER)) { //用户端,校验userId
            Object value = cacheService.get("sid_"+token);
            if(value == null){
                String errMsg = "token is not validate,path=" + path + ",key=" + fTWebSocket.getKey();
                this.sendFeedbackErrorMsg(fTWebSocket, "连接失败【token不合法】", MsgConstant.ResponseCode.NOT_AUTHORITY, errMsg);
                return;
            }
            Long userId = (Long) value;
            //从cache中获取
            User user = (User) cacheService.get("login_user_"+userId);
            if(user == null){
                String errMsg = "user is offline ,path=" + path + ",key=" + fTWebSocket.getKey();
                this.sendFeedbackErrorMsg(fTWebSocket, "连接失败【用户已退出】", MsgConstant.ResponseCode.NOT_LOGIN, errMsg);
                return;
            }
            fTWebSocket.setUserId(userId);
            Company company = companyService.findById(user.getCompanyId());
            fTWebSocket.setCpKey(company.getKey());
        }else{  //core 端
            Company company = companyService.findByKey(token);
            if (company == null) {
                String errMsg = "token is not validate,path=" + path + ",key=" + fTWebSocket.getKey();
                this.sendFeedbackErrorMsg(fTWebSocket, "连接失败【token不合法】", MsgConstant.ResponseCode.NOT_AUTHORITY, errMsg);
                return;
            }
            fTWebSocket.setCpKey(token);
        }

        //
        fTWebSocket.setAuthStatus(1);
        fTWebSocket.setStatus(1);
        fTWebSocket.setPlatform(platform);

        //连接成功 TODO
        this.sendFeedbackSuccessMsg(fTWebSocket, "连接成功", MsgConstant.ResponseCode.NORMAL, fTWebSocket.toString());

    }

    /**
     * 客户端发送消息，无需处理状态
     *
     * @param conn
     * @param message
     */
    public void onMessage(WebSocket conn, String message) {
        String id = conn.getRemoteSocketAddress().toString();
        DZWebSocket fTWebSocket = sysWebSocketMap.get(id);
        if (!canProcessMsg) {  //拒绝发送消息
            writeSystemMsg("服务器升级处理,拒绝消息发送", fTWebSocket.toString());
            conn.close(1, "服务器升级处理,拒绝消息发送");  //关闭
            return;
        }
        String key = fTWebSocket.getKey();
        if (fTWebSocket.getStatus().intValue() == 0) {//已下线
            String errMsg = "connection is not validate，message=" + message + ",key=" + key;
            this.sendFeedbackErrorMsg(fTWebSocket, "当前连接已下线", MsgConstant.ResponseCode.CONN_OFFLINE, errMsg);
            return;
        }
        if (fTWebSocket.getAuthStatus().intValue() == 0) {//没有权限
            String errMsg = "connection is not validate，message=" + message + ",key=" + key;
            this.sendFeedbackErrorMsg(fTWebSocket, "发送消息没有权限", MsgConstant.ResponseCode.NOT_AUTHORITY, errMsg);
            return;
        }
        MsgRequestObject requestObject = JsonUtils.json2Object(message, MsgRequestObject.class);
        if (requestObject == null) {
            writeSystemMsg("解析消息出错", "desc:" + message);
            return;
        }

        //add to queue
        if (StringUtils.equals(requestObject.getType(), MsgConstant.ReqResType.CORE)) {  //内核
            //如果单独给平台的,则自己消化
            if (StringUtils.equals(requestObject.getBizType(), MsgConstant.ReqCoreBizType.UPDATE_ADDRESS)) {
                //更新 core http host and ip
                Company company = companyService.findByKey(fTWebSocket.getCpKey());
                String reqMsg = requestObject.getMessage();
                company.setIp(reqMsg.split(":")[0]);
                company.setPort(Integer.valueOf(reqMsg.split(":")[1]));
                companyService.updateById(company);
            }else{
                //如果是退给用户的,放到队列中,准备推送给用户端
                String listKey = CacheConstant.Msg.readyMsgList + "_" + SystemUtils.getIp();
                MsgResponseObject responseObject = new MsgResponseObject();
                responseObject.setType(MsgConstant.ReqResType.USER);  //发送给用户端
                responseObject.setBizType(requestObject.getBizType());
                responseObject.setCode(MsgConstant.ResponseCode.NORMAL);
                responseObject.setKey(fTWebSocket.getCpKey());
                responseObject.setUserId(fTWebSocket.getUserId());
                responseObject.setData(requestObject.getMessage());
                String resMsg = JsonUtils.object2Json(responseObject);
                queueService.add(listKey, resMsg);

            }

        } else if (StringUtils.equals(requestObject.getType(), MsgConstant.ReqResType.USER)) { //用户
            //TODO 暂停发布,other

        } else {
            writeSystemMsg("接收消息类型出错", "desc:" + message);
            return;
        }


    }

    /**
     * 发送消息
     */
    public void offerSendMsg(MsgResponseObject responseObject) {
        String json = JsonUtils.object2Json(responseObject);
        String listKey = CacheConstant.Msg.readyMsgList + "_" + SystemUtils.getIp();
        queueService.add(listKey, json);
    }


    /**
     * 服务端发送消息，
     */
//    @Override
    private void doSendMessage() {
        final long startMainTime = System.currentTimeMillis();
        ExecutorService ec = new ThreadPoolExecutor(4, 4, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>()) {
            @Override
            protected void terminated() {
                super.terminated();
                System.out.println("finish, take time = " + (System.currentTimeMillis() - startMainTime));
            }
        };
        String listKey = CacheConstant.Msg.readyMsgList + "_" + SystemUtils.getIp();
        while (canProcessMsg) { //
            //消息处理开始
            try {
                final String message = queueService.pop(listKey);
                if (message == null) {  //没有消息，歇一会儿
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }
                    continue;
                }
                ec.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            messageSendTask(message);
                        } catch (Exception e) {
                            writeSystemErrorMsg("发送单个消息任务处理出错", ExceptionUtils.getPrintStackTrace(e));
                        }
                    }
                });

            } catch (Exception e) {
                writeSystemErrorMsg("发送消息任务处理出错", ExceptionUtils.getPrintStackTrace(e));
            }
        }
        //结束处理
        ec.shutdown();
    }

    private void messageSendTask(String message) {
        MsgResponseObject responseObject = JsonUtils.json2Object(message, MsgResponseObject.class);

        //处理消息业务拦截
        wsMessageService.sendHandler(responseObject);
        //end

        String key = responseObject.getKey();
        Set<Map.Entry<String, DZWebSocket>> entrySet = sysWebSocketMap.entrySet();
        for (Map.Entry<String, DZWebSocket> entry : entrySet) {
            DZWebSocket dzWebSocket = entry.getValue();

            if (dzWebSocket.getStatus() == 0) {
                continue;
            }
            if (dzWebSocket.getAuthStatus() == 0) {
                continue;
            }
            if (!StringUtils.equals(dzWebSocket.getPlatform(), responseObject.getType())) {  //不对应平台
                continue;
            }
            if (!StringUtils.equals(dzWebSocket.getCpKey(), key)) {  //不对应cp
                continue;
            }
            WebSocket webSocket = dzWebSocket.getWebSocket();
            if (!webSocket.getReadyState().name().equals(WebSocket.READYSTATE.OPEN.name())) {
                continue;
            }
            webSocket.send(message);

            this.writeSystemMsg("push data to platform deploy", message);
            String sentMsgListKey = CacheConstant.Msg.sentMsgList + "_" + SystemUtils.getIp();
            queueService.add(sentMsgListKey, message);
        }

    }

    private void doStoreMessage() {
        String listKey = CacheConstant.Msg.sentMsgList + "_" + SystemUtils.getIp();
//        List<MessageDataVo> dataList = new ArrayList<>(350);
//        List<UserActiveTaskData> dataList = new ArrayList<>(350);
        while (canProcessMsg) { //
            //消息处理开始
            try {
                final String message = queueService.pop(listKey);
                if (message == null) {  //没有消息，歇一会儿
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }
                    continue;
                }
                //TODO
//                MessageDataVo msgBean = JsonUtils.json2Object(message, MessageDataVo.class);
//                if (msgBean.getType() == 1) {  //不存储导师端
//                    continue;
//                }
//                if (dataList.size() >= batchStoreSize) {
//                    int size = dataList.size();
//                    userActiveTaskDataService.insertBatch(dataList);
//                    writeSystemMsg(getServerBatchNo(), "存储已发送数据成功,size=" + size, "");
//                    dataList.clear();
//                }
            } catch (Exception e) {
//                writeSystemErrorMsg("doStoreMessage_"+System.currentTimeMillis(), "数据存储任务处理出错", ExceptionUtils.getPrintStackTrace(e));
            }
        }

    }

    public void onClose(WebSocket conn) {
        Set<Map.Entry<String, DZWebSocket>> entrySet = sysWebSocketMap.entrySet();
        for (Map.Entry<String, DZWebSocket> entry : entrySet) {
            if (conn.toString().equals(entry.getValue().getWebSocket().toString())) {
//                Long userId = entry.getKey();
                sysWebSocketMap.remove(entry.getValue().getId());

//                //移除用户初始化信息 String msgLoginUserKey = CacheConstant.Msg.msgLoginUser + "_" + userId;
//                cacheService.remove(msgLoginUserKey);
                break;
            }
        }

    }

    public void backgroundTaskProcess() {
        ExecutorService ec = Executors.newFixedThreadPool(4);

        //消息发送处理
        ec.execute(new Runnable() {
            @Override
            public void run() {
                doSendMessage();
            }
        });

//
//        //单点登录关闭前一个连接
//        ec.execute(new Runnable() {
//            @Override
//            public void run() {
//                doClosePrevConn();
//            }
//        });

        //发送消息存储处理
        ec.execute(new Runnable() {
            @Override
            public void run() {
                doStoreMessage();
            }
        });
        ec.shutdown();
        //所有后台任务结束
        finishProcessMsg = true;

    }

    public void destory() {
        writeSystemMsg("服务器升级处理,资源释放", "");
        canProcessMsg = false;  //停止处理消息

        //客户端发给服务端，不必要关心消息是否成功
        //服务端必须保证发送完成
        //客户端发送不成功，服务端要通知客户端

    }

    public boolean isDestory() {
        return finishProcessMsg;
    }

    public Map<String, DZWebSocket> getSysWebSocketMap() {
        return sysWebSocketMap;
    }


    public void writeSystemErrorMsg(String message, String errMsg) {
        bisLog.info(message);
        log.error(message);
        if (StringUtils.isNotEmpty(errMsg)) {
            log.error(errMsg);
        }
    }

    private void writeSystemMsg(String message, String desMsg) {
        bisLog.info(message);
        if (StringUtils.isNotEmpty(desMsg)) {
            bisLog.info(desMsg);
        }
    }

    public void start() throws Exception {
        //ws启动
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    dzWebSocketServer.run();
                }
            }).start();

            //后台任务处理
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DZMessageHelperServer.this.backgroundTaskProcess();
                }
            }).start();

            //记录服务器一条主日志
            this.writeSystemMsg("客户端ws已启动", "");


            //end
        } catch (Exception e) {
            this.writeSystemErrorMsg("客服端ws启动失败", ExceptionUtils.getPrintStackTrace(e));
        }

    }
}
