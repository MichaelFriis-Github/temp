/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Server.ClientHandler;
import Utils.ProtocolStrings;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author frederikolesen
 */
public class ChatClient extends Thread implements ChatList {

    Socket socket;
    private int port;
    private InetAddress serverAddress;
    private Scanner input;
    private PrintWriter output;
    private String cusername;
    List<ChatList> listeners = new ArrayList();
    ChatList cl = new ChatList() {

        @Override
        public void messageArrived(String data) {
            System.out.println("Message arrived" + data);
        }

        @Override
        public void usernameArrived(String username) {
            System.out.println("Username Arrived" + username);
        }
    };

    public void connect(String address, String username, int port) throws UnknownHostException, IOException {
        cusername = username;
        this.port = port;
        serverAddress = InetAddress.getByName(address);
        socket = new Socket(serverAddress, port);
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);  //Set to true, to get auto flush behaviour
        start();
    }

    public void registerEchoListener(ChatList l) {
        listeners.add(l);
    }

    ;
  
  public void unRegisterEchoListener(ChatList l) {
        listeners.remove(l);
    }

    ;
  
  private void notifyListeners(String msg) {
        for (ChatList l : listeners) {
            l.messageArrived(msg);
        }

    }

    private void notifyUsernamesToList(String username) {
        for (ChatList l : listeners) {
            l.usernameArrived(username);
        }
    }

    public void send(String msg) {
        output.println(msg);
    }

    public void sendusername(String username) {
        output.println(username);
    }

    public void close() throws IOException {
        output.println(ProtocolStrings.STOP);
    }

    public String receive() {
        String msg = input.nextLine();
        if (msg.equals(ProtocolStrings.STOP)) {
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return msg;
    }

    public static void main(String[] args) {
        int port = 9090;
        String ip = "localhost";
        if (args.length == 2) {
            port = Integer.parseInt(args[0]);
            ip = args[1];
        }

    }

    public void run() {
        String msg = input.nextLine();
        String username = input.nextLine();
        while (!msg.equals(ProtocolStrings.STOP)) {
            notifyListeners(msg);

            msg = input.nextLine();
        }
        while (!username.equals(ProtocolStrings.STOP)) {
            notifyUsernamesToList(username);

            username = input.nextLine();
        }
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void closeConnection() {
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void usernameArrived(String username) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void messageArrived(String data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
