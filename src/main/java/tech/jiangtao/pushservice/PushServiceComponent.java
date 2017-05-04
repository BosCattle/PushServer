package tech.jiangtao.pushservice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import redis.clients.jedis.JedisPubSub;
import tech.jiangtao.pushservice.db.RedisRepository;
import tech.jiangtao.pushservice.db.RedisRepositoryImpl;
import tigase.conf.ConfigurationException;
import tigase.db.RepositoryFactory;
import tigase.server.AbstractMessageReceiver;
import tigase.server.Packet;
import tigase.stats.StatisticsList;
import tigase.xml.Element;
import tigase.xmpp.JID;

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
  private static final String MESSAGE_SENDER = "push-sender";
  private static final String SENDER_EXTENSION = "push:extension";
  private static final String SENDERVALUE = "value";
  private static final String channel = "channel";
  private RedisRepository redisRepository;

  @Override public void processPacket(Packet packet) {
    System.out.println(packet.toString());
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
      if (uri != null) {
        redisRepository = new RedisRepositoryImpl();
        redisRepository.init(uri);
      } else {
        log.log(Level.SEVERE, "repository uri is NULL!");
        redisRepository.init("localhost");
      }
      new Thread(new Runnable() {
        @Override public void run() {
          redisRepository.subscribe(new JedisPubSub() {
            @Override public void onMessage(String channel, String message) {
              super.onMessage(channel, message);
              System.out.println("得到消息为：" + message);
            }
          }, channel);
        }
      }).start();
    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }
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
}
