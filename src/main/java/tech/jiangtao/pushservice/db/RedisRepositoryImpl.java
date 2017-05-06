package tech.jiangtao.pushservice.db;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;
import tigase.server.Packet;

/**
 * @class: RedisRepositoryImpl </br>
 * @description: </br>
 * @creator: kevin </br>
 * @email: jiangtao103cp@gmail.com </br>
 * @date: 03/05/2017 04:52</br>
 * @version: 0.0.1 </br>
 **/
public class RedisRepositoryImpl extends RedisRepository {

  @Override public JedisPool init(String address) {
    return super.init(address);
  }

  @Override public void subscribe(JedisPubSub jedisPubSub, String channel) {
    super.subscribe(jedisPubSub,channel);
  }

  @Override public void unSubscribe(String channel, JedisPubSub jedisPubSub) {
    super.unSubscribe(channel,jedisPubSub);
  }
}
