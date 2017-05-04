package tech.jiangtao.pushservice.db;

import java.util.Map;
import tech.jiangtao.pushservice.model.TigPubsub;
import tigase.db.DBInitException;
import tigase.db.Repository;
import tigase.db.TigaseDBException;

/**
 * @class: PubSubRepository </br>
 * @description:  存储推送系统消息</br>
 * @creator: kevin </br>
 * @email: jiangtao103cp@gmail.com </br>
 * @date: 03/05/2017 04:47</br>
 * @version: 0.0.1 </br>
 **/
public class PubSubRepositoryImpl implements PubSubRepository{

  @Override public void release() {

  }

  @Override public int insertPubsubMessage(TigPubsub pubsub) throws TigaseDBException {
    return 0;
  }

  @Override public void initRepository(String s, Map<String, String> map) throws DBInitException {

  }
}
