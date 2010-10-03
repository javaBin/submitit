/*
 * Copyright 2009 javaBin
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.webapp.WebAppContext;

object LiftJettyStarter {

  def main(args: Array[String]) {
    val server = new Server();
    val connector = new SocketConnector();
    // Set some timeout options to make debugging easier.
    connector.setMaxIdleTime(1000 * 60 * 60);
    connector.setSoLingerTime(-1);
    connector.setPort(8080);
    server.setConnectors(Array(connector));

    val bb = new WebAppContext();
    bb.setServer(server);
    bb.setContextPath("/");
    bb.setWar("subliftit/src/main/webapp");

    // System.setProperty("submitit.properties", "src/main/resources/submitit.properties")

    server.addHandler(bb);

    try {
      System.out.println(">>> STARTING EMBEDDED JETTY SERVER, PRESS ANY KEY TO STOP");
      server.start();
      while (System.in.available() == 0) {
        Thread.sleep(5000);
      }
      server.stop();
      server.join();
    } catch {
      case e: Exception => {
        e.printStackTrace();
        System.exit(100);
      }
    }
  }
}
