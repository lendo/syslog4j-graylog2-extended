package com.uumind.syslog.server;

import org.graylog2.syslog4j.server.SyslogServer;
import org.graylog2.syslog4j.server.SyslogServerConfigIF;
import org.graylog2.syslog4j.server.SyslogServerEventHandlerIF;
import org.graylog2.syslog4j.server.SyslogServerIF;
import org.graylog2.syslog4j.server.impl.net.tcp.TCPNetSyslogServerConfigIF;
import org.graylog2.syslog4j.util.SyslogUtility;
import org.zbus.broker.Broker;
import org.zbus.broker.BrokerConfig;
import org.zbus.broker.SingleBroker;

import com.uumind.syslog.server.impl.event.zbus.ZBusSyslogServerEventHandler;

public class ExtendedSyslogServerMain {
  public static void main(String[] args) throws Exception {
    SyslogServerIF syslogServer = SyslogServer.getInstance("udp");

    SyslogServerConfigIF syslogServerConfig = syslogServer.getConfig();
    syslogServerConfig.setHost("127.0.0.1");
    syslogServerConfig.setPort(514);
    
    if (syslogServerConfig instanceof TCPNetSyslogServerConfigIF) {
      ((TCPNetSyslogServerConfigIF) syslogServerConfig).setTimeout(20);

    }
    
    BrokerConfig config = new BrokerConfig();
    config.setBrokerAddress("127.0.0.1:15555");
    Broker broker = new SingleBroker(config);
    SyslogServerEventHandlerIF eventHandler = new ZBusSyslogServerEventHandler(broker, "SYSLOG");
    syslogServerConfig.addEventHandler(eventHandler);

    SyslogServer.getThreadedInstance("udp");

    while (true) {
      SyslogUtility.sleep(1000);
    }
  }
}
