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
                System.out.println("here");

                JSONObject jsonObject = null;
                System.out.println("xxx");
                ///jsonObject = new JSONObject(in.readUTF());
                str = in.readUTF();
                System.out.println(str);
                out.writeUTF("Yes");
                //String name = jsonObject.getString("name");
                //String file = jsonObject.getString("file");

                System.out.println(userAnswer);


                System.out.println("aici!");

            }
            catch (Exception e){
                System.out.print(e);
            }
        }
    }
}
