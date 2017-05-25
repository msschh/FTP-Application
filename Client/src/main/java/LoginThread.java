/*
        Window w = new Window();
        try {
            w.acceptBox();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (this.userAnswer.equals("wait")) {
            System.out.println("aici");
        }
*/

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class LoginThread implements Runnable{

    DataInputStream in;
    DataOutputStream out;

    public static String userAnswer;

    LoginThread(Socket socket){
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        }
        catch (Exception e){
            System.out.print(e);
        }
        this.userAnswer = "wait";
    }

    @Override
    public void run(){
        while(true) {
            try {
                String str = "nulll";
               // System.out.println("here");

                JSONObject jsonObject = new JSONObject(in.readUTF());
                //System.out.println("xxx");
                ///jsonObject = new JSONObject(in.readUTF());

                System.out.println(jsonObject);

                Scanner cin = new Scanner(System.in);
                String name = jsonObject.getString("name");
                String file = jsonObject.getString("file");

                System.out.println("Accepti fisierul " + file + " de la userul " + name + "? Yes/No");
                String s = cin.next();
                //System.out.println("out");
                out.writeUTF(s);
                //System.out.println("ok!!!?");
            }
            catch (Exception e){
                //System.out.print(e);
            }
        }
    }
}
