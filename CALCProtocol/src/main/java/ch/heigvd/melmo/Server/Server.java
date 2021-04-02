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

    enum COMMANDS {
        HELLO,
        ACK,
        OPERATION,
        RESULT,
        ERROR,
        QUIT,
        UNKNOWN
    }

    enum OPERATIONS {
        ADD,
        SUB,
        MPY,
        DIV,
        UNKNOWN
    }

    private class Calculator implements Runnable{

        Socket clientSocket;
        BufferedReader in = null;
        PrintWriter out = null;
        final char SPLIT_CHAR = ' ';
        final String EOL = " \r\n";

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
            String line, errorMsg = null, resultMsg = null;
            boolean shouldRun = true;
            String[] request;
            boolean error = false, connectionOpened = false;
            COMMANDS command = null;
            OPERATIONS operation = null;

            try{
                while((shouldRun) && (line = in.readLine()) != null){
                    request = parseRequest(line);
                    try{
                        command = COMMANDS.valueOf(request[0]);
                    } catch (IllegalArgumentException ex) {
                        command = COMMANDS.UNKNOWN;
                    }
                    switch(command){
                        case HELLO:
                            connectionOpened = true;
                            resultMsg = COMMANDS.ACK.name();
                            break;
                        case OPERATION:
                            if(connectionOpened){
                                try{
                                    operation = OPERATIONS.valueOf(request[1]);
                                } catch (IllegalArgumentException ex) {
                                    operation = OPERATIONS.UNKNOWN;
                                }
                                switch(operation) {
                                    case ADD:
                                        resultMsg = COMMANDS.RESULT.name() + this.SPLIT_CHAR + (Integer.parseInt(request[2]) + Integer.parseInt(request[3]));
                                        break;
                                    case SUB:
                                        resultMsg = COMMANDS.RESULT.name() + this.SPLIT_CHAR + (Integer.parseInt(request[2]) - Integer.parseInt(request[3]));
                                        break;
                                    case MPY:
                                        resultMsg = COMMANDS.RESULT.name() + this.SPLIT_CHAR + (Integer.parseInt(request[2]) * Integer.parseInt(request[3]));
                                        break;
                                    case DIV: {
                                        int divider = Integer.parseInt(request[3]);
                                        if (divider == 0) {
                                            errorMsg = "DIVIDE_BY_0";
                                            error = true;
                                            break;
                                        }
                                        resultMsg = COMMANDS.RESULT.name() + this.SPLIT_CHAR + (Integer.parseInt(request[2]) / divider);
                                        break;
                                    }
                                    case UNKNOWN:
                                        errorMsg = "OPERATION_NOT_FOUND";
                                        error = true;
                                        break;
                                }
                            }else{
                                error = true;
                                errorMsg = "NO_CONNECTION";
                            }
                            break;
                        case QUIT:
                            connectionOpened = false;
                            shouldRun = false;
                            break;
                        case UNKNOWN:
                            error = true;
                            errorMsg = "UNKNOWN_COMMAND";
                            break;
                    }
                    if(error) {
                        out.print(COMMANDS.ERROR.name() + SPLIT_CHAR + errorMsg + this.EOL);
                        error = false;
                    } else if(shouldRun){
                        out.print(resultMsg + this.EOL);
                    }
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



