package ch.heigvd.melmo.Client;

import java.io.*;
import java.net.*;

public class Client {
    private Socket socket;
    public int port = 1301;
    public String url = null;
    private PrintWriter out;
    private BufferedReader in;
    final private String splitChar = " ";
    final private String EOL = " \r\n";

    public Client() throws IOException {
        this.startConnection();
    }

    public Client(int port, String url) throws IOException {
        this.url = url;
        this.port = port;
        this.startConnection();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Client client = new Client(1301, "127.0.0.1");
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

    public void startConnection() throws IOException {
        this.socket = new Socket(this.url, this.port);
        this.out = new PrintWriter(this.socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    public void stopConnection() throws IOException {
        this.in.close();
        this.out.close();
        this.socket.close();
    }

    public String sendMessage(String msg) throws IOException {
        msg += this.EOL;
        System.out.print("Send : " + msg);
        this.out.print(msg);
        this.out.flush();
        String rawResponse = this.in.readLine();
        System.out.println("Received : " + rawResponse);
        return rawResponse;
    }
}
