package com.yuntao.zhushou.common.utils;

import com.yuntao.zhushou.common.http.HttpNewUtils;
import com.yuntao.zhushou.common.http.RequestRes;
import com.yuntao.zhushou.common.http.ResponseRes;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Created by shan on 2016/8/20.
 */
public class SendMsgUtils {

    private static String accountId = "8a216da85a7d6742015a9e7013ae0d15";
    private static String token = "ecf5cd2c9da9404e9a45054d5dc0c2a9";
    private static String appId = "8a216da85a7d6742015a9e70166d0d1b";



    public static String sendSMS(final Long templateId, final String mobile, final List<String> msgList) {
        RequestRes requestRes = new RequestRes();
        Map<String, String> headers = new HashMap();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json;charset=utf-8");
        StringBuilder sb = new StringBuilder("https://app.cloopen.com:8883");
        String accountSid = accountId;
        StringBuilder sigSb = new StringBuilder(accountSid);
        sigSb.append(token);
        String time = DateUtil.getFmt(new Date().getTime(), "yyyyMMddHHmmss");
        sigSb.append(time);
        String sig = MD5Util.MD5Encode(sigSb.toString());
        sb.append("/2013-12-26/Accounts/" + accountSid + "/SMS/TemplateSMS?sig=");
        sb.append(sig);
        requestRes.setUrl(sb.toString());
        byte[] authBytes = Base64.encodeBase64(new String(accountSid + ":" + time).getBytes());
        headers.put("Authorization", new String(authBytes));
        requestRes.setHeaders(headers);
        String datas = "";
        if (CollectionUtils.isNotEmpty(msgList)) {
            datas = "\"" + StringUtils.join(msgList, "\",\"") + "\"";
        }
        String paramText = "{\"to\":\"" + mobile + "\",\"appId\":\"" + appId + "\",\"templateId\":\"" + templateId + "\",\"datas\":[" + datas + "]}";
        requestRes.setParamText(paramText);
        ResponseRes execute = HttpNewUtils.execute(requestRes);
        byte[] result = execute.getResult();
        return new String(result);
    }

    public static void main(String[] args) {
//                /2013-12-26/Accounts/abcdefghijklmnopqrstuvwxyz012345/SMS/TemplateSMS?sig=
//                C1F20E7A9733CE94F680C70A1DBABCDE
        accountId = "8a216da85a7d6742015a9e7013ae0d15";
        token = "ecf5cd2c9da9404e9a45054d5dc0c2a9";
        appId = "8a216da85a7d6742015a9e70166d0d1b";
        sendSMS(161318L, "15267164682", Arrays.asList("123456babc"));
    }
}
