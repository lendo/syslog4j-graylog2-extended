package com.uumind.syslog.server;

import org.graylog2.syslog4j.Syslog;
import org.graylog2.syslog4j.SyslogIF;

public class SyslogClient {
  public static void main(String s[]) {
    SyslogIF syslog = Syslog.getInstance("udp");
    syslog.getConfig().setHost("127.0.0.1");
    syslog.getConfig().setPort(514);
    syslog.log(0, "中国人2");
  }
}
