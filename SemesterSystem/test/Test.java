

import Client.ChatClient;
import Server.MainServer;
import java.io.IOException;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;


public class Test {

    public Test() {
    }

    @BeforeClass
    public static void setUpClass() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MainServer.main(null);
            }
        }).start();
    }

    @AfterClass
    public static void tearDownClass() {
        MainServer.stopServer();
    }

    @Before
    public void setUp() {
    }

    public void send() throws IOException {
        ChatClient client = new ChatClient();
        client.connect("localhost", 9090);
        client.send("Hello");
        assertEquals("HELLO", client.receive());
    }

}
