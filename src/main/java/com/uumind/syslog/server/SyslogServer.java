package com.uumind.syslog.server;

import org.graylog2.syslog4j.server.SyslogServerMain;

public class SyslogServer {
  public static void main(String s[]) throws Exception {
    SyslogServerMain.main(new String[]{"-h","127.0.0.1", "-p", "9898", "-o", "a.log", "-a", "udp"});
  }
}
