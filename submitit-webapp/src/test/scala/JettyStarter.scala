/*
 * Copyright 2011 javaBin
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

import java.io.File
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.webapp.WebAppContext;

object JettyStarter {

  def main(args: Array[String]) {
    val server = new Server();
    val connector = new SocketConnector();
    // Set some timeout options to make debugging easier.
    connector.setMaxIdleTime(1000 * 60 * 60);
    connector.setSoLingerTime(-1);
    connector.setPort(8081);
    server.setConnectors(Array(connector));
  
    val bb = new WebAppContext();
    bb.setServer(server);
    bb.setContextPath("/");
    bb.setWar(webappDir.toString);
    
    System.setProperty("submitit.properties", new File(getBaseDir(getClass), "src/main/resources/submitit.properties").toString)
	
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

  private def webappDir : File = {
    val dir = new File(getBaseDir(getClass), "src/main/webapp")
    if (!dir.exists()) {
      throw new RuntimeException("Unable to find web application directory")
    }
    dir
  }

  private def getBaseDir(c: Class[_]): File = {
    val basedir = System.getProperty("basedir");
    if (basedir != null) {
      new File(basedir)
    } else {
      var file = new File(c.getProtectionDomain.getCodeSource.getLocation.getPath)
      if (!file.exists()) {
        throw new RuntimeException("Unable to find basedir")
      }
      while (!new File(file, "pom.xml").exists()) {
        file = file.getParentFile
        if (file == null) {
          throw new RuntimeException("Unable to find basedir")
        }
      }
      file;
    }
  }
}
