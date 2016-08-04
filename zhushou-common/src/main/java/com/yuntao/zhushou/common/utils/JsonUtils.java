package com.yuntao.zhushou.common.utils;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    private static Logger log = LoggerFactory.getLogger(JsonUtils.class);

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
