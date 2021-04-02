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
        System.out.println("Client Send : " + msg);
        this.out.println(msg);
        String rawResponse = this.in.readLine();
        System.out.println("Client Received : " + rawResponse);
//        String[] response = rawResponse.trim().split(splitChar);
        return rawResponse;
    }
}
