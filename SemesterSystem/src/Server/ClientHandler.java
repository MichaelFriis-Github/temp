package Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import Utils.ProtocolStrings;

/**
 *
 * @author frederikolesen
 */
public class ClientHandler extends Thread {

    private final Socket socket;
    private final Scanner input;
    private final Scanner input2;
    private final PrintWriter writer;
    private final PrintWriter writer2;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        input = new Scanner(socket.getInputStream());
        input2 = new Scanner(socket.getInputStream());
        writer = new PrintWriter(socket.getOutputStream(), true);
        writer2 = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            String message = input.nextLine(); //IMPORTANT blocking call
            Logger.getLogger(MainServer.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ", message));
            while (!message.equals(ProtocolStrings.STOP)) {
//                writer.println(message.toUpperCase());
                MainServer.send(message);
                Logger.getLogger(MainServer.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ", message.toUpperCase()));
                message = input.nextLine(); //IMPORTANT blocking call
            }
            writer.println(ProtocolStrings.STOP);
            writer2.println(ProtocolStrings.STOP);    //Echo the stop message back to the client for a nice closedown
            socket.close();
            Logger.getLogger(MainServer.class.getName()).log(Level.INFO, "Closed a Connection");
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void send(String msg) {
        writer.println(msg);
    }
    
    public void sendusername(String username){
        writer2.println(username);
    }
    
    
}
