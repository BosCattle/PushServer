package tech.jiangtao.pushservice.db;

import tech.jiangtao.pushservice.model.TigPubsub;
import tigase.db.DataRepository;
import tigase.db.Repository;
import tigase.db.TigaseDBException;
import tigase.db.UserRepository;

/**
 * @class: PubSubRepository </br>
 * @description:  存储推送系统消息</br>
 * @creator: kevin </br>
 * @email: jiangtao103cp@gmail.com </br>
 * @date: 03/05/2017 04:47</br>
 * @version: 0.0.1 </br>
 **/
public interface PubSubRepository extends Repository{

  void release();

  int insertPubsubMessage(TigPubsub pubsub) throws TigaseDBException;
}
