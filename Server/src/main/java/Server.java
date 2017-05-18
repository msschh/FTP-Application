import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    private Integer port;
    private ServerSocket MyService;
    private List<Socket> clients;

    Server(Integer port){
        this.port = port;
        clients = new ArrayList<>();
    }

    public void startServer(){
        try{
            ServerSocket socket = new ServerSocket(port);
            while(true){
                Socket client = socket.accept();
                clients.add(client);
                ServerThread th = new ServerThread(client);
                Thread thread = new Thread(th);
                thread.start();
            }
        } catch(IOException e){
            System.out.println(e);
        }
    }
}
