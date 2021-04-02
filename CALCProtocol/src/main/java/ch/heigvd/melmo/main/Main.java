package ch.heigvd.melmo.main;

import java.io.IOException;
import ch.heigvd.melmo.Server.Server;
import ch.heigvd.melmo.Client.Client;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = new Server(1301);
        server.serveClient();
        Thread.sleep(2000);
        Client client = new Client();
        client.sendMessage("COUCOU LA MIF");
        Thread.sleep(2000);
        client.sendMessage("OPERATION ADD 1 3");
        Thread.sleep(2000);
        client.sendMessage("HELLO");
        Thread.sleep(2000);
        client.sendMessage("OPERDFksdjf ");
        Thread.sleep(2000);
        client.sendMessage("OPERATION dkjf deowr");
        Thread.sleep(2000);
        client.sendMessage("OPERATION SUB 5 3249");
        Thread.sleep(2000);
        client.sendMessage("OPERATION DIV 1 0");
        Thread.sleep(2000);
        client.sendMessage("OPERATION DIV 4 2");
        Thread.sleep(2000);
        client.sendMessage("OPERATION MPY 3 2");
        Thread.sleep(2000);
        client.sendMessage("QUIT");
    }

}
