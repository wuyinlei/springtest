package com.ruolan.springtest.config.redis;

import com.ruolan.springtest.cache.JedisPoolWriper;
import com.ruolan.springtest.cache.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfiguration {

    @Value("${redis.hostname}")
    private String hostname;

    @Value("${redis.port}")
    private int port;

    @Value("${redis.database}")
    private int database;

    @Value("${redis.pool.maxActive}")
    private int maxActive;

    @Value("${redis.pool.maxIdle}")
    private int maxIdle;

    @Value("${redis.pool.maxWait}")
    private long maxWait;

    @Value("${redis.pool.testOnBorrow}")
    private boolean testOnBorrow;

    @Autowired
    private JedisPoolConfig jedisPoolConfig;

    @Autowired
    private JedisPoolWriper jedisPoolWriper;

    @Autowired
    private JedisUtil jedisUtil;

    @Bean(name = "jedisPoolConfig")
    public JedisPoolConfig createJedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        //控制一个pool可以分配的多少个jedis实例
        jedisPoolConfig.setMaxTotal(maxActive);
        //连接池中最多可空闲的maxIdle个连接  这里取值20
        //表示即使没有数据库连接时候依然可以保持20个空闲的连接  而不被清除 随时处于待命状态
        jedisPoolConfig.setMaxIdle(maxIdle);
        //最大等待时间:当没有可以用的连接的时候  连接池等待连接被归还的最大时间(以毫秒技术)  超过时间则抛出异常
        jedisPoolConfig.setMaxWaitMillis(maxWait);
        //在获取连接的时候检查有效性
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        return jedisPoolConfig;
    }

    @Bean(name = "jedisPoolWriper")
    public JedisPoolWriper createJedisPoolWriper() {
        JedisPoolWriper jedisPoolWriper = new JedisPoolWriper(jedisPoolConfig, hostname, port);
        return jedisPoolWriper;
    }

    /**
     * 创建Redis工具类  封装好Redis的连接以进行相关操作
     *
     * @return
     */
    @Bean(name = "jedisUtil")
    public JedisUtil createJedisUtil() {
        JedisUtil jedisUtil = new JedisUtil();
        jedisUtil.setJedisPool(jedisPoolWriper);
        return jedisUtil;
    }

    @Bean(name = "jedisKeys")
    public JedisUtil.Keys createJedisKeys() {
        JedisUtil.Keys jedisKeys = jedisUtil.new Keys();
        return jedisKeys;
    }

    @Bean(name = "jedisStrings")
    public JedisUtil.Strings createJedisStrings() {
        JedisUtil.Strings jedisStrings = jedisUtil.new Strings();
        return jedisStrings;
    }

    @Bean(name = "jedisLists")
    public JedisUtil.Lists createJedisLists() {
        JedisUtil.Lists jedisLists = jedisUtil.new Lists();
        return jedisLists;
    }

    @Bean(name = "jedisSets")
    public JedisUtil.Sets createJedisSets() {
        JedisUtil.Sets jedisSets = jedisUtil.new Sets();
        return jedisSets;
    }

    @Bean(name = "jedisHash")
    public JedisUtil.Hash createJedisHash() {
        JedisUtil.Hash jedisHash = jedisUtil.new Hash();
        return jedisHash;
    }


}
