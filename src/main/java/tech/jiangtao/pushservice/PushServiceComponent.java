package tech.jiangtao.pushservice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import redis.clients.jedis.JedisPubSub;
import tech.jiangtao.pushservice.db.PubSubRepository;
import tech.jiangtao.pushservice.db.PubSubRepositoryImpl;
import tech.jiangtao.pushservice.db.RedisRepository;
import tech.jiangtao.pushservice.db.RedisRepositoryImpl;
import tech.jiangtao.pushservice.model.TigPubsub;
import tigase.conf.ConfigurationException;
import tigase.db.DBInitException;
import tigase.db.DataRepository;
import tigase.db.RepositoryFactory;
import tigase.db.TigaseDBException;
import tigase.db.UserRepository;
import tigase.db.UserRepositoryMDImpl;
import tigase.db.UserRepositoryPool;
import tigase.server.AbstractMessageReceiver;
import tigase.server.Message;
import tigase.server.Packet;
import tigase.stats.StatisticsList;
import tigase.util.TigaseStringprepException;
import tigase.xml.Element;
import tigase.xmpp.BareJID;
import tigase.xmpp.JID;
import tigase.xmpp.StanzaType;

/**
 * Created by kevin on 28/04/2017.
 * XMPP推送组件
 * 并非在这儿建立链接，需要在初始化数据仓库的时候使用
 */
public class PushServiceComponent extends AbstractMessageReceiver {

  private static final String NODE = "http://jabber.org/protocol/push-server";
  private static final String XMLNS = NODE;
  private static final String DISCO_DESCRIPTION = "push-server";
  private static final Element top_feature =
      new Element("feature", new String[] {"var"}, new String[] {NODE});
  private static final String DISCOCATEGORYTYPE = "push-server";
  private static final List<Element> DISCO_FEATURES = Arrays.asList(top_feature);
  private static final Logger log = Logger.getLogger(AbstractMessageReceiver.class.getName());
  private static final String PUSH_REPO_CLASS_PROP_KEY = "push-repo-class";
  private static final String PUSH_REPO_URI_PROP_KEY = "push-repo-uri";
  private static final String PUSH_REDIS_URI = "push-redis-uri";
  private static final String MESSAGE_SENDER = "push-sender";
  private static final String SENDER_EXTENSION = "push:extension";
  private static final String SENDERVALUE = "value";
  private static final String channel = "channel";
  private RedisRepository redisRepository;
  private PubSubRepository pubSubRepository;
  private UserRepository userRepository;
  private static int result = 0;

  @Override public void processPacket(Packet packet) {
    System.out.println(packet.toString());
    // 客户端收到消息后，返回确认消息
    // 将用户从redis中除去
    RedisRepository.get().srem(packet.getPacketFrom().getBareJID().toString());
  }

  @Override public int processingInThreads() {
    return super.processingInThreads();
  }

  @Override public int processingOutThreads() {
    return super.processingOutThreads();
  }

  @Override public int hashCodeForPacket(Packet packet) {
    if (packet.getStanzaTo() != null) {
      return packet.getStanzaTo().hashCode();
    }
    if (packet.getStanzaFrom() != null) {
      return packet.getStanzaFrom().hashCode();
    }
    return super.hashCodeForPacket(packet);
  }

  /**
   * 每小时执行的操作
   */
  @Override public synchronized void everyHour() {
    super.everyHour();
  }

  /**
   * 每分钟执行的操作
   */
  @Override public synchronized void everyMinute() {
    super.everyMinute();
  }

  /**
   * 每秒钟执行的操作
   */
  @Override public synchronized void everySecond() {
    super.everySecond();
  }

  /**
   * 服务发现
   */
  @Override public String getDiscoDescription() {
    return DISCO_DESCRIPTION;
  }

  /**
   * 服务发现
   */
  @Override public String getDiscoCategoryType() {
    return DISCOCATEGORYTYPE;
  }

  @Override
  public List<Element> getDiscoFeatures(JID from) {
    return DISCO_FEATURES;
  }

  @Override public List<Element> getDiscoItems(String node, JID jid, JID from) {
    log.log(Level.FINEST, node);
    log.log(Level.FINEST, jid.toString());
    log.log(Level.FINEST, from.toString());
    return super.getDiscoItems(node, jid, from);
  }

  /**
   * 获取统计信息
   *
   * @param list is a <code>StatistcsList</code>
   */
  @Override public void getStatistics(StatisticsList list) {
    super.getStatistics(list);
  }

  /**
   * 可配置init.properties
   *
   * @throws ConfigurationException
   */
  @Override public void setProperties(Map<String, Object> props)
      throws ConfigurationException {
    try {
      super.setProperties(props);
      Map<String, String> repoProps = new HashMap<>(4);
      for (Map.Entry<String, Object> entry : props.entrySet()) {
        if ((entry.getKey() == null) || (entry.getValue() == null)) {
          continue;
        }
        repoProps.put(entry.getKey(), entry.getValue().toString());
      }
      String uri = (String) props.get(PUSH_REPO_URI_PROP_KEY);
      String redis_Uri = (String) props.get(PUSH_REDIS_URI);
      if (uri != null) {
        Class<? extends PubSubRepository> pubsubClass;
        try {
          pubsubClass = RepositoryFactory.getRepoClass(PubSubRepositoryImpl.class, uri);
          userRepository = RepositoryFactory.getUserRepository(null, uri, repoProps);
          pubSubRepository = pubsubClass.newInstance();
          pubSubRepository.initRepository(uri, repoProps);
        } catch (DBInitException | ClassNotFoundException e) {
          e.printStackTrace();
        }
      }
      // 线程走了两次或者三次
      ++result;
      if (result <= 1) {
        new Thread(() -> {
          redisRepository = new RedisRepositoryImpl();
          if (redis_Uri != null) {
            redisRepository.init(redis_Uri);
          } else {
            redisRepository.init("localhost");
          }
          startSubscribe();
        }).start();
      }
    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  private void startSubscribe() {
    String type = "type";
    String body = "body";
    String msg = "msg";
    redisRepository.subscribe(new JedisPubSub() {
      @Override public void onMessage(String channel, String message) {
        super.onMessage(channel, message);
        System.out.println("得到消息为：" + message);
        JSONObject object = new JSONObject(message);
        TigPubsub pubsub = new TigPubsub();
        pubsub.setPushType(object.getString(type));
        pubsub.setBody(object.getString(body));
        pubsub.setMessage(object.getString(msg));
        try {
          pubSubRepository.insertPubsubMessage(pubsub);
          List<BareJID> users = userRepository.getUsers();
          System.out.println("打印出所有的用户" + users.size());
          for (BareJID user : users) {
            // redis.clients.jedis.exceptions.JedisDataException: value sent to redis cannot be null
            System.out.println(user);
            System.out.println(user.toString());
            RedisRepository.get().sadd(pubsub.getChannelName(), user.toString());
          }
          for (BareJID user : users) {
            Packet push_message = Message.getMessage(JID.jidInstance(
                "admin@" + DISCO_DESCRIPTION + "." + "dc-a4b8eb92-xmpp.jiangtao.tech."),
                JID.jidInstance(user.toString()),
                StanzaType.chat, pubsub.getBody(), null, null, null);
            System.out.println(push_message.toString());
            addOutPacket(push_message);
          }
        } catch (TigaseDBException | TigaseStringprepException e) {
          e.printStackTrace();
        }
      }
    }, channel);
  }

  @Override public Map<String, Object> getDefaults(Map<String, Object> params) {
    Map<String, Object> defs = super.getDefaults(params);
    String db_uri = (String) params.get(RepositoryFactory
        .USER_REPO_URL_PROP_KEY);
    if (db_uri == null) {
      db_uri = (String) params.get("--" + RepositoryFactory
          .GEN_USER_DB_URI_PROP_KEY);
    }
    if (db_uri != null) {
      defs.put(PUSH_REPO_CLASS_PROP_KEY, db_uri);
    }
    return defs;
  }

  @Override public void release() {
    super.release();
  }

  private class RedisThread implements Runnable {
    @Override public void run() {

    }
  }
}
