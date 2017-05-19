import java.io.*;
import java.net.Socket;
import org.json.JSONObject;
import java.sql.*;

public class ServerThread implements Runnable{

    private DataInputStream in;
    private DataOutputStream out;
    private Socket client;
    private static Connection con;

    ServerThread(Socket client){
        this.client = client;
        try {
            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ftp-application");
        } catch(Exception e){
            System.out.println(e);
        }
    }

    @Override
    public void run(){
        try {
            /*Statement statement = con.createStatement();
            ResultSet re = statement.executeQuery("SELECT * FROM users");
            System.out.println(re);*/

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
            Server.usersOnline.add(username);
            while(true){
                JSONObject jsonObject2 = new JSONObject(in.readUTF());//Citim JSON
                String type = jsonObject2.getString("type");
                if(type.equals("upload")) {
                    upload(jsonObject2, username);
                }
                else if(type.equals("send")){
                    send(jsonObject2);
                }
                else if(type.equals("download")){
                    download(jsonObject2, username);
                }
                else{
                    Server.usersOnline.remove(username);
                    return;
                }
            }
        } else {
            out.writeUTF("Access denied!");
            if(in != null) in.close();
            if(out != null) out.close();
        }
    }

    private String getPassword(String username) throws Exception{
        String password = new String();
        Statement statement = con.createStatement();
        ResultSet re = statement.executeQuery("SELECT * FROM users WHERE username = '" + username + "';");
        re.next();
        return re.getString("password");
    }

    private void signup(JSONObject jsonObject) throws Exception{
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        String firstName = jsonObject.getString(("firstname"));
        String lastName = jsonObject.getString(("lastname"));
        String email = jsonObject.getString(("email"));

        if(unique(username, email)){
            out.writeUTF("Singup accepted!");
            boolean success = (new File(Server.pathToServer + "\\" + username)).mkdir();
            if (!success) {
                System.out.println("The directory: " + username + " was not created");
            }

            Statement statement = con.createStatement();
            statement.execute("INSERT INTO users (username, password, last_name, first_name, email) VALUES ('"
                    + username + "','" + password + "','" + lastName + "','" + firstName + "','" + email + "')");
        }
        else{
            out.writeUTF("Singup rejected!");
            if(in != null) in.close();
            if(out != null) out.close();
        }
    }

    private boolean unique(String username, String email) throws Exception{
        //Search DB
        Statement statement = con.createStatement();
        ResultSet re = statement.executeQuery("SELECT * FROM users");
        String name = new String();
        String mail = new String();
        while(re.next()){
            name = re.getString("username");
            mail = re.getString("email");
            if(name.equals(username) || mail.equals(email)){
                return false;
            }
        }
        return true;
    }

    private void upload(JSONObject jsonObject, String username) throws Exception{
        String filename = jsonObject.getString("filename");
        FileOutputStream fout = new FileOutputStream(Server.pathToServer + "\\" + username + "\\" + filename);
        byte [] mybytearray  = new byte [16*1024];
        int count;
        while ((count = in.read(mybytearray)) > 0) {
            fout.write(mybytearray, 0, count);
        }
        //Update DB
    }

    private void send(JSONObject jsonObject){

    }

    private void download(JSONObject jsonObject, String username) throws Exception{
        String filename = jsonObject.getString("filename");
        FileInputStream fin = new FileInputStream(new File(Server.pathToServer + "\\" + username + "\\" + filename));
        byte [] mybytearray  = new byte [16*1024];
        int count;
        while ((count = fin.read(mybytearray)) > 0) {
            out.write(mybytearray, 0, count);
        }
    }
}