package com.uumind.syslog.server.impl.event.zbus;

import java.io.IOException;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.graylog2.syslog4j.server.SyslogServerEventIF;
import org.graylog2.syslog4j.server.SyslogServerIF;
import org.graylog2.syslog4j.server.SyslogServerSessionEventHandlerIF;
import org.graylog2.syslog4j.util.SyslogUtility;
import org.zbus.broker.Broker;
import org.zbus.mq.Producer;
import org.zbus.mq.Protocol.MqMode;
import org.zbus.net.Sync.ResultCallback;
import org.zbus.net.http.Message;

public class ZBusSyslogServerEventHandler implements SyslogServerSessionEventHandlerIF {
  private static final long serialVersionUID = 1729936594161093697L;
  private static final SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
  private Producer producer = null;
  public ZBusSyslogServerEventHandler(Broker broker, String mq) {
    producer = new Producer(broker, mq, MqMode.MQ);
    try {
      producer.createMQ();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void initialize(SyslogServerIF syslogServer) {
    return;
  }

  @Override
  public void destroy(SyslogServerIF syslogServer) {
    return;
  }

  @Override
  public Object sessionOpened(SyslogServerIF syslogServer, SocketAddress socketAddress) {
    return null;
  }

  @Override
  public void event(Object session, SyslogServerIF syslogServer, SocketAddress socketAddress, SyslogServerEventIF event) {
    String date = dateParser.format(event.getDate() == null ? new Date() : event.getDate());
    String facility = SyslogUtility.getFacilityString(event.getFacility());
    String level = SyslogUtility.getLevelString(event.getLevel());
    String log = "{" + facility + "} " + date + " " + level + " " + event.getMessage();
    Message msg = new Message();
    msg.setBody(log);
    
    try {
      producer.sendAsync(msg, new ResultCallback<Message>() {
        public void onReturn(Message result) {
          if (!"200".equals(result.getResponseStatus())) {
            System.out.println(result.toString());
          }
        }
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void exception(Object session, SyslogServerIF syslogServer, SocketAddress socketAddress, Exception exception) {
  }

  @Override
  public void sessionClosed(Object session, SyslogServerIF syslogServer, SocketAddress socketAddress, boolean timeout) {
  }
  
}
