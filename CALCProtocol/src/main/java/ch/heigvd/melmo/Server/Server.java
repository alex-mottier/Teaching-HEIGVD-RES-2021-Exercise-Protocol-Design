package ch.heigvd.melmo.Server;

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

    enum OPERATIONS {
        ADD,
        SUB,
        MPY,
        DIV,
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
            String line, errorMsg = null;
            boolean shouldRun = true;
            int MAX_OPERANDE = 4;
            String[] request;
            int result = 0;
            boolean error = false;

            try{
                while((shouldRun) && (line = in.readLine()) != null){
                    if(line.equalsIgnoreCase("quit"))
                        shouldRun = false;

                    request = parseRequest(line);
                    switch(OPERATIONS.valueOf(request[1])){
                        case ADD:
                            result = Integer.parseInt(request[2]) + Integer.parseInt(request[3]);
                            break;
                        case SUB:
                            result = Integer.parseInt(request[2]) - Integer.parseInt(request[3]);
                            break;
                        case MPY:
                            result = Integer.parseInt(request[2]) * Integer.parseInt(request[3]);
                            break;
                        case DIV:{
                            int divider = Integer.parseInt(request[3]);
                            if(divider == 0){
                                errorMsg = "DIVIDE_BY_0";
                                error = true;
                                break;
                            }
                            result = Integer.parseInt(request[2]) / divider;
                            break;
                        }

                        default:
                            errorMsg = "OPERATION_NOT_FOUND";
                            error = true;
                            break;
                    }
                    if(error)
                        out.println("ERROR " + errorMsg);
                    else
                        out.println("RESULT " + result);
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



