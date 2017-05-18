import netscape.javascript.JSObject;

import java.io.*;
import java.net.Socket;
import org.json.JSONObject;

public class ServerThread implements Runnable{

    private DataInputStream in;
    private DataOutputStream out;
    private Socket client;

    ServerThread(Socket client){
        this.client = client;
        try {
            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());
        } catch(Exception e){
            System.out.println(e);
        }
    }

    @Override
    public void run(){
        try {
            JSONObject jsonObject = new JSONObject(in.readUTF());//Citim JSON

            String type = jsonObject.getString("type");
            if(type.equals("login")){
                login(jsonObject);
            }
            else{
                signup(jsonObject);
            }

        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    private void login(JSONObject jsonObject) throws Exception{
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");

        //Querying the database to find if the username&password combination is correct.
        String answer = getPassword(username);
        if (password.equals(answer)) {
            out.writeUTF("Access granted!");
        } else {
            out.writeUTF("Access denied!");
            if(in != null) in.close();
            if(out != null) out.close();
        }
    }

    private String getPassword(String username){
        String password = new String("syg");
        return password;
    }

    private void signup(JSONObject jsonObject){

    }
}