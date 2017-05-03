package tech.jiangtao.pushservice.db;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/**
 * @class: RedisRepositoryImpl </br>
 * @description:  </br>
 * @creator: kevin </br>
 * @email: jiangtao103cp@gmail.com </br>
 * @date: 03/05/2017 04:52</br>
 * @version: 0.0.1 </br>
 **/
public class RedisRepositoryImpl implements RedisRepository {

  private Jedis mJedis;
  private JedisPubSub jedisPubSub;

  @Override public Jedis init(String address) {
    mJedis = new Jedis(address);
    return mJedis;
  }

  @Override public Jedis get() {
    return mJedis;
  }

  @Override public void subscribe(String channel) {
    jedisPubSub = new JedisPubSub() {
      @Override public void onMessage(String channel, String message) {
        super.onMessage(channel, message);
        excute(channel,message);
      }

      @Override public void onPSubscribe(String pattern, int subscribedChannels) {
        super.onPSubscribe(pattern, subscribedChannels);
      }
    };
    mJedis.subscribe(jedisPubSub,channel);
  }

  @Override public void excute(String channel, String message) {

  }

  @Override public void unSubscribe(String channel) {
    jedisPubSub.unsubscribe();
  }
}
