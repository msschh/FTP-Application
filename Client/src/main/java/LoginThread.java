import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class LoginThread implements Runnable{

    DataInputStream in;
    DataOutputStream out;

    LoginThread(Socket client){
        try {
            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());
        }
        catch (Exception e){
            System.out.print(e);
        }
    }

    @Override
    public void run(){
        //Wait for request
    }
}
