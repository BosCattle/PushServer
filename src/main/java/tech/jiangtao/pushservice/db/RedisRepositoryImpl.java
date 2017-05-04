package tech.jiangtao.pushservice.db;

import redis.clients.jedis.Jedis;
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
public class RedisRepositoryImpl implements RedisRepository {

  private Jedis mJedis;

  @Override public Jedis init(String address) {
    mJedis = new Jedis(address);
    return mJedis;
  }

  @Override public Jedis get() {
    return mJedis;
  }

  @Override public void subscribe(JedisPubSub jedisPubSub, String channel) {
    mJedis.subscribe(jedisPubSub, channel);
  }

  @Override public void unSubscribe(String channel, JedisPubSub jedisPubSub) {
    jedisPubSub.unsubscribe(channel);
  }
}
