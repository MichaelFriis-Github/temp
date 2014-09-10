
package Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import Utils.Utils;


public class MainServer {

  private static boolean keepRunning = true;
  private static ServerSocket serverSocket;
  private static final Properties properties = Utils.initProperties("server.properties");
  
  private static List<ClientHandler> clients = new ArrayList<>();
 

  public static void stopServer() {
    keepRunning = false;
  }
  
  public static void send(String msg)
  {
      for(ClientHandler c : clients)
          c.send(msg);
  }
  
  public static void removeHandler(ClientHandler ch)
  {
      clients.remove(ch);
  }

   public static void main(String[] args) {
    int port = Integer.parseInt(properties.getProperty("port"));
    String ip = properties.getProperty("serverIp");
    String logFile = properties.getProperty("logFile");
    Logger.getLogger(MainServer.class.getName()).log(Level.INFO, "Sever started");
    try {
      Utils.setLogFile(logFile, MainServer.class.getName());
      serverSocket = new ServerSocket();
      serverSocket.bind(new InetSocketAddress(ip, port));
      do {
        Socket socket = serverSocket.accept(); //Important Blocking call
        Logger.getLogger(MainServer.class.getName()).log(Level.INFO, "Connected to a client");        
        ClientHandler clientHandler = new ClientHandler(socket);
        clients.add(clientHandler);
        new Thread(clientHandler).start();
      } while (keepRunning);
    } catch (IOException ex) {
      Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
    }
    finally {
        Utils.closeLogger(MainServer.class.getName());
    }
  }
}
