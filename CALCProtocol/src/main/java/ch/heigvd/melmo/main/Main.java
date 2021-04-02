package ch.heigvd.melmo.main;

import java.io.IOException;
import ch.heigvd.melmo.Server.Server;
import ch.heigvd.melmo.Client.Client;

public class Main {
    public static void main(String[] args) throws IOException {
        Server server = new Server(1301);
        server.serveClient();
        Client client = new Client();
        client.sendMessage("HELLO");
        client.sendMessage("QUIT");
    }

}
