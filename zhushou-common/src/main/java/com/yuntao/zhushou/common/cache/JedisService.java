package com.yuntao.zhushou.common.cache;

import redis.clients.jedis.ShardedJedis;

/**
 * Created by shan on 2016/8/19.
 */
public interface JedisService {

    String getKey(String key);

    ShardedJedis getShardedJedis();

    void returnResource(ShardedJedis jedis);
}
