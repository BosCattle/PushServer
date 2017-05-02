package tech.jiangtao.pushservice.db;

import redis.clients.jedis.Jedis;

/**
 * @class: RedisRepository </br>
 * @description:  </br>
 * @creator: kevin </br>
 * @email: jiangtao103cp@gmail.com </br>
 * @date: 03/05/2017 04:38</br>
 * @version: 0.0.1 </br>
 **/
public interface RedisRepository {

  public Jedis init();

  public void subscribe(String channel);

  public void excute(String channel,String message);

  public void unSubscribe(String channel);
}
