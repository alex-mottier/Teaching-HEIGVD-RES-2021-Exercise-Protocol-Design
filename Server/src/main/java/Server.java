import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    int port;
    public Server(int port){
        this.port = port;
    }

    public void serveClient(){
        new Thread(new ClientReception()).start();
    }

    public static void main(String[] args) {
        Server server = new Server(1301);
        server.serveClient();
    }

    private class ClientReception implements Runnable{
        @Override
        public void run() {
            ServerSocket socket;
            try {
                socket = new ServerSocket(port);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            while (true){
                try {
                    Socket clientSocket = socket.accept();
                    new Thread(new Calculator(clientSocket)).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class Calculator implements Runnable{

        Socket clientSocket;
        BufferedReader in = null;
        PrintWriter out = null;
        final char SPLIT_CHAR = ' ';

        public Calculator(Socket clientSocket){
            try {
                this.clientSocket = clientSocket;
                this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                this.out = new PrintWriter(clientSocket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String[] parseRequest(String request){
            return request.trim().split("\\s+");
        }

        @Override
        public void run() {
            String line;
            boolean shouldRun = true;

            try{
                while((shouldRun) && (line = in.readLine()) != null){
                    if(line.equalsIgnoreCase("quit"))
                        shouldRun = false;

                    String[] request = parseRequest(line);

                    out.println(line.toUpperCase());
                    out.flush();
                }

                clientSocket.close();
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}



