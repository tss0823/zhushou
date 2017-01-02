package com.yuntao.zhushou.common.cache;

/**
 * Created by shan on 2016/8/19.
 */
public interface QueueService {

    void add(String key, String msg);

    String pop(String key);


}
