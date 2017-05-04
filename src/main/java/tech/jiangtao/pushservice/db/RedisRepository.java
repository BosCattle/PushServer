package tech.jiangtao.pushservice.db;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import tigase.server.Packet;

/**
 * @class: RedisRepository </br>
 * @description: </br>
 * @creator: kevin </br>
 * @email: jiangtao103cp@gmail.com </br>
 * @date: 03/05/2017 04:38</br>
 * @version: 0.0.1 </br>
 **/
public interface RedisRepository {

  public Jedis init(String address);

  public Jedis get();

  public void subscribe(JedisPubSub jedisPubSub, String channel);

  public void unSubscribe(String channel,JedisPubSub jedisPubSub);
}
