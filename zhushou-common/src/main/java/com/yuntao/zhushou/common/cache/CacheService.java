package com.yuntao.zhushou.common.cache;

/**
 * Created by shan on 2016/3/25.
 */
public interface CacheService {

    void set(String key,Object value);

    void set(String key,int period, Object value);

    Object get(String key);

    void remove(String key);
}
