package tech.jiangtao.pushservice.db;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class RedisRepository {

  //可用连接实例的最大数目，默认值为8；
  //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
  public static final int MAX_ACTIVE = 1024;
  //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
  public static final int MAX_IDLE = 200;
  //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
  public static final int MAX_WAIT = 10000;
  public static final int TIMEOUT = 10000;
  public static final int RETRY_NUM = 5;
  private Logger logger = Logger.getLogger(this.getClass().getName());
  private static String ipAddress;
  private static int portAddress;

  private RedisRepository() {
  }

  private static class RedisUtilHolder {
    private static final RedisRepository instance = new RedisRepository();
  }

  public static RedisRepository getInstance() {
    return RedisUtilHolder.instance;
  }

  private static Map<String, JedisPool> maps = new HashMap<>();

  private static JedisPool getPool(String ip, int port) {
    String key = ip + ":" + port;
    ipAddress = ip;
    portAddress = port;
    JedisPool pool;
    if (!maps.containsKey(key)) {
      JedisPoolConfig config = new JedisPoolConfig();
      config.setMaxIdle(MAX_IDLE);
      config.setMaxTotal(MAX_ACTIVE);
      config.setMaxWaitMillis(MAX_WAIT);
      config.setTestOnBorrow(true);
      config.setTestOnReturn(true);
      pool = new JedisPool(config, ip, port, TIMEOUT);
      maps.put(key, pool);
    } else {
      pool = maps.get(key);
    }
    return pool;
  }

  public Jedis getJedis(String ip, int port) {
    Jedis jedis = null;
    int count = 0;
    do {
      try {
        jedis = getPool(ip, port).getResource();
      } catch (Exception e) {
        logger.log(Level.SEVERE, "get redis master1 failed!", e);
        getPool(ip, port).close();
      }
    }
    while (jedis == null);
    return jedis;
  }

  public void closeJedis(Jedis jedis, String ip, int port) {
    if (jedis != null) {
      getPool(ip, port).close();
    }
  }
}
