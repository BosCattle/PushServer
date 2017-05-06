package tech.jiangtao.pushservice.db;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

/**
 * @class: RedisRepository </br>
 * @description: </br>
 * @creator: kevin </br>
 * @email: jiangtao103cp@gmail.com </br>
 * @date: 03/05/2017 04:38</br>
 * @version: 0.0.1 </br>
 **/
public abstract class RedisRepository {

  private static JedisPool jedisPool;
  private volatile JedisPoolConfig jedisPoolConfig;
  public JedisPool init(String address) {
    if (jedisPoolConfig==null){
      synchronized (this){
        jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(1024);
        jedisPoolConfig.setMaxIdle(100);
        jedisPoolConfig.setMaxWaitMillis(100);
        jedisPoolConfig.setTestOnBorrow(false);
        jedisPoolConfig.setTestOnReturn(true);
        jedisPool = new JedisPool(jedisPoolConfig, address, 6379, 2000);
      }
    }
    return jedisPool;
  }

  public static Jedis get() {
    return jedisPool.getResource();
  }

  public void subscribe(JedisPubSub jedisPubSub, String channel) {
    get().subscribe(jedisPubSub, channel);
  }

  public void unSubscribe(String channel, JedisPubSub jedisPubSub) {
    jedisPubSub.unsubscribe();
  }
}
