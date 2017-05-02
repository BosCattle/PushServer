package tech.jiangtao.pushservice.db;

import redis.clients.jedis.Jedis;

/**
 * @class: RedisRepositoryImpl </br>
 * @description:  </br>
 * @creator: kevin </br>
 * @email: jiangtao103cp@gmail.com </br>
 * @date: 03/05/2017 04:45</br>
 * @version: 0.0.1 </br>
 **/
public class RedisRepositoryImpl implements RedisRepository {
  @Override public Jedis init() {
    return null;
  }

  @Override public void subscribe(String channel) {

  }

  @Override public void excute(String channel, String message) {

  }

  @Override public void unSubscribe(String channel) {

  }
}
