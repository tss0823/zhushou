package com.yuntao.zhushou.common.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Enumeration;

public class LogUtils {

    private final static Logger log = LoggerFactory.getLogger("RequestLog");

    public static void logRequest(HttpServletRequest req) {
        log.info(getRequestLog(req));
    }

    public static void logRequestWithResponse(HttpServletRequest req, Object responseObj, HttpServletResponse res) {
        log.info(getRequestLog(req) + " Response:" + JsonUtils.object2Json(responseObj) + appendHeader(res).toString());
    }

    public static String getRequestLog(HttpServletRequest req) {
        try {
            return "Request From:" + (StringUtils.isBlank(req.getHeader("X-real-ip")) ? req.getRemoteAddr() : req.getHeader("X-real-ip"))
                    + " To:" + req.getRequestURL() + " Method:" + req.getMethod() + appendHeader(req).toString() + appendParameter(req).toString();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }

    public static StringBuffer appendParameter(HttpServletRequest req) {
        StringBuffer sb = new StringBuffer(" PARAMETERS[");
        if (req == null) {
            sb.append("]");
            return sb;
        }
        Enumeration<String> parameterNames = req.getParameterNames();
        String contentType = req.getHeader("content-type");
        if (StringUtils.startsWith(contentType, "multipart/form-data")) {
            if (req instanceof DefaultMultipartHttpServletRequest) {
                parameterNames = req.getParameterNames();
            }
        }
        String parameterName = null;
        String[] parameterValues = null;
        while (parameterNames != null && parameterNames.hasMoreElements()) {
            parameterName = parameterNames.nextElement();
            parameterValues = req.getParameterValues(parameterName);
            if (parameterValues != null && parameterValues.length > 0) {
                for (String parameterValue : parameterValues) {
                    sb.append(parameterName + "=" + parameterValue + "^|^");
                }
            }
        }
        return sb.append("]");
    }

    public static StringBuffer appendHeader(HttpServletRequest req) {
        StringBuffer sb = new StringBuffer(" HEADERS[");
        if (req == null) {
            sb.append("]");
            return sb;
        }
        Enumeration<String> headerNames = req.getHeaderNames();
        String headName = null;
        Enumeration<String> headValues = null;
        while (headerNames != null && headerNames.hasMoreElements()) {
            headName = headerNames.nextElement();
            headValues = req.getHeaders(headName);
            while (headValues != null && headValues.hasMoreElements()) {
                sb.append(headName + "=" + headValues.nextElement() + "^|^");
            }
        }
        return sb.append("]");
    }

    public static StringBuffer appendHeader(HttpServletResponse res) {
        StringBuffer sb = new StringBuffer(" Response HEADERS[");
        if (res == null) {
            sb.append("]");
            return sb;
        }
        Collection<String> headerNames = res.getHeaderNames();
        String headName = null;
        Collection<String> headValue = null;
        if (CollectionUtils.isNotEmpty(headerNames)) {
            for (String name : headerNames) {
                headName = name;
                headValue = res.getHeaders(headName);
                if (CollectionUtils.isNotEmpty(headValue)) {
                    for (String val : headValue) {
                        sb.append(headName + "=" + val + "^|^");
                    }
                }
            }
        }
        return sb.append("]");
    }
}
