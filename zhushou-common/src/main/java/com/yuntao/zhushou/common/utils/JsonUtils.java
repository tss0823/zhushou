package com.yuntao.zhushou.common.utils;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    private static Logger log = LoggerFactory.getLogger(JsonUtils.class);
    private final static Logger bisLog = LoggerFactory.getLogger("bis");

    public static <T> T json2Object(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        T obj = null;
        try {
            obj = objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            bisLog.info("jsonToObj error,class="+clazz+",json="+json);
            try {
                obj = clazz.newInstance();
            } catch (Exception e1) {
            }
        }
        return obj;
    }

    public static <T> List<T> json2List(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        List<T> obj = null;
        try {
            obj = objectMapper.readValue(json, new ObjectMapper().getTypeFactory().constructParametricType(ArrayList.class, clazz));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            bisLog.info("jsonToList error,class="+clazz+",json="+json);
            try {
                obj = new ArrayList<>();
            } catch (Exception e1) {
            }
        }
        return obj;
    }

    public static String object2Json(Object object) {
        if (object == null) {
            return "";
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }

    public static String format(String source){
        ObjectMapper mapper = new ObjectMapper();
        try {
            Object obj = mapper.readValue(source, Object.class);
            String formatData = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            return formatData;
        } catch (IOException e) {
            log.error("format json failed,",e);
            return source;
        }
    }

    public static String compress(String source){
        ObjectMapper mapper = new ObjectMapper();
        try {
            Object obj = mapper.readValue(source, Object.class);
            String formatData = mapper.writeValueAsString(obj);
            return formatData;
        } catch (IOException e) {
            log.error("compress json failed,",e);
            return source;
        }
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY)
    static class UserBean {
        private String username;

        private String pwd;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }
    }

    public static void main(String[] args) {

    }


}
