package tech.jiangtao.pushservice;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import tigase.server.AbstractMessageReceiver;
import tigase.server.Packet;
import tigase.xml.Element;

/**
 * Created by kevin on 28/04/2017.
 * XMPP推送组件
 * 并非在这儿建立链接，需要在初始化数据仓库的时候使用
 */
public class PushServiceComponent extends AbstractMessageReceiver{

  private static final String NODE = "http://jabber.org/protocol/push-server";
  private static final String XMLNS = NODE;
  private static final String DISCO_DESCRIPTION = "push-server";
  private static final Element top_feature =
      new Element("feature", new String[] {"var"}, new String[] {NODE});
  private static final String DISCOCATEGORYTYPE = "push-server";
  private static final List<Element> DISCO_FEATURES = Arrays.asList(top_feature);
  private static final Logger log = Logger.getLogger(AbstractMessageReceiver.class.getName());
  private static final String GROUP_REPO_CLASS_PROP_KEY = "push-repo-class";
  private static final String GROUP_REPO_URI_PROP_KEY = "push-repo-uri";
  private static final String MESSAGE_SENDER = "push-sender";
  private static final String SENDER_EXTENSION = "push:extension";
  private static final String SENDERVALUE = "value";
  private static final String channel = "channel";
  private Jedis jedis;

  public PushServiceComponent() {
    jedis = new Jedis("localhost");
    jedis.subscribe(new JedisPubSub() {
      @Override public void onMessage(String channel, String message) {
        super.onMessage(channel, message);
        System.out.println(message);
      }

      @Override public void onPSubscribe(String pattern, int subscribedChannels) {
        super.onPSubscribe(pattern, subscribedChannels);
      }
    }, channel);
  }

  @Override public void processPacket(Packet packet) {
    System.out.println(packet.toString());
  }
}
