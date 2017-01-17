package com.yuntao.zhushou.common.cache.impl;

import com.yuntao.zhushou.common.cache.CacheService;
import com.yuntao.zhushou.common.cache.QueueService;
import com.yuntao.zhushou.common.utils.AppConfigUtils;
import com.yuntao.zhushou.common.utils.SerializeNewUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shan on 2016/5/5.
 */
@Service("cacheService")
public class JedisCacheServiceImpl implements CacheService ,QueueService {

    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private int port;

    @Value("${redis.pwd}")
    private String pwd;

    @Value("${redis.namespace}")
    private String namespace;

    private ShardedJedisPool shardedJedisPool;

    @PostConstruct
    public void init() {
        List<JedisShardInfo> shards = new ArrayList<>();
        JedisShardInfo jedisShardInfo = new JedisShardInfo(host, port);
        jedisShardInfo.setPassword(pwd);
        shards.add(jedisShardInfo);
        JedisPoolConfig config = new JedisPoolConfig();
        //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
        //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
        config.setMaxTotal(500);
        //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
        config.setMaxIdle(5);
        //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
        config.setMaxWaitMillis(1000 * 30);
        //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
        config.setTestOnBorrow(true);
        shardedJedisPool = new ShardedJedisPool(new JedisPoolConfig(), shards);
    }

    @Override
    public void set(String key, Object value) {
        ShardedJedis jedis = null;
        try{
            String newKey = namespace+"_"+key;
            jedis = shardedJedisPool.getResource();
            byte[] bs = SerializeNewUtil.serialize(value);
            int period = 60 * 60 * 5;  //默认过期时间
            jedis.setex(newKey.getBytes(),period,bs);
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            shardedJedisPool.returnResource(jedis);
        }

    }

    @Override
    public void set(String key, int period, Object value) {
        ShardedJedis jedis = null;
        try{
            String newKey = namespace+"_"+key;
            jedis = shardedJedisPool.getResource();
            byte[] bs = SerializeNewUtil.serialize(value);
            jedis.setex(newKey.getBytes(),period,bs);
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            shardedJedisPool.returnResource(jedis);
        }

    }

    @Override
    public Object get(String key) {
        ShardedJedis jedis = null;
        try{
            String newKey = namespace+"_"+key;
            jedis = shardedJedisPool.getResource();
            byte [] bs = jedis.get(newKey.getBytes());
            Object value = SerializeNewUtil.unserialize(bs);
            return value;
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            shardedJedisPool.returnResource(jedis);
        }
    }

    @Override
    public void remove(String key) {
        ShardedJedis jedis = null;
        try{
            String newKey = namespace+"_"+key;
            jedis = shardedJedisPool.getResource();
            jedis.del(newKey.getBytes());
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            shardedJedisPool.returnResource(jedis);
        }

    }

    @Override
    public void add(String key, String msg) {
        ShardedJedis jedis = null;
        try{
            jedis = shardedJedisPool.getResource();
            key = namespace+"_"+key;
            jedis.lpush(key,msg);
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            shardedJedisPool.returnResource(jedis);
        }

    }

    @Override
    public String pop(String key) {
        ShardedJedis jedis = null;
        try{
            jedis = shardedJedisPool.getResource();
            key = namespace+"_"+key;
            return jedis.rpop(key);
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            shardedJedisPool.returnResource(jedis);
        }
    }
}
