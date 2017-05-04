package tech.jiangtao.pushservice.db;

import java.sql.SQLException;
import java.util.Map;
import tech.jiangtao.pushservice.model.TigPubsub;
import tigase.db.DBInitException;
import tigase.db.DataRepository;
import tigase.db.Repository;
import tigase.db.RepositoryFactory;
import tigase.db.TigaseDBException;

/**
 * @class: PubSubRepository </br>
 * @description:  存储推送系统消息</br>
 * @creator: kevin </br>
 * @email: jiangtao103cp@gmail.com </br>
 * @date: 03/05/2017 04:47</br>
 * @version: 0.0.1 </br>
 **/
@Repository.Meta(supportedUris = {"jdbc:[^:]+:.*"})
public class PubSubRepositoryImpl implements PubSubRepository{

  private static final String TABLE_PUBSUB = "tig_pubsub";
  private static final String INSERT_MESSAGE = "";
  private DataRepository dataRepository = null;

  @Override public void release() {

  }

  @Override public int insertPubsubMessage(TigPubsub pubsub) throws TigaseDBException {
    return 0;
  }

  @Override public void initRepository(String resource_uri, Map<String, String> params) throws DBInitException {
    try {
      dataRepository = RepositoryFactory.getDataRepository(null,resource_uri,params);
      if (dataRepository.checkTable(TABLE_PUBSUB)) {
        dataRepository.initPreparedStatement(INSERT_MESSAGE, INSERT_MESSAGE);
      }
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
      e.printStackTrace();
    }
  }
}
