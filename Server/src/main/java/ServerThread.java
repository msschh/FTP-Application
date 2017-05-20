import java.io.*;
import java.net.Socket;
import org.json.JSONObject;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ftp-application", "Ftp", "cisco12345");
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
            //if(in != null) in.close();
            //if(out != null) out.close();
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
            //if(in != null) in.close();
            //if(out != null) out.close();
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

    private void upload(JSONObject jsonObject, String username)throws Exception{
        FileOutputStream fout = null;
        try {
            String filename = jsonObject.getString("filename");
            long size = jsonObject.getLong("size");
            if(size == 0){
                return;
            }
            Statement statement = con.createStatement();
            ResultSet re = statement.executeQuery("SELECT id FROM users WHERE username = '" + username + "';");
            re.next();
            String id_user = re.getString("id");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            statement.execute("INSERT INTO files (id_user, name, dimension, add_date) VALUES ('"
                    + id_user + "','" + filename + "','" + size + "','" + now + "')");
            fout = new FileOutputStream(Server.pathToServer + "\\" + username + "\\" + filename);
            byte[] mybytearray = new byte[16 * 1024];
            int count;
            long length = 0;
            while ((count = in.read(mybytearray)) > 0 && length + count <= size) {
                fout.write(mybytearray, 0, count);
                length += count;
                System.out.println(length + " " + size);
                if(length == size){
                    return;
                }
            }
            System.out.println(length + " " + size);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    private void send(JSONObject jsonObject){

    }

    private void download(JSONObject jsonObject, String username) throws Exception{
        String filename = jsonObject.getString("filename");

        long size = fileDimension(filename, username);
        if(size != -1){
            String json = new String("{\"type\":\"Yes\"," +
                    "\"size\":\"" + size + "\"" + "}");
            out.writeUTF(json);
        }
        else{
            String json = new String("{\"type\":\"No\";}");
            out.writeUTF(json);
            return;
        }

        FileInputStream fin = new FileInputStream(new File(Server.pathToServer + "\\" + username + "\\" + filename));
        byte [] mybytearray  = new byte [16*1024];
        int count;
        while ((count = fin.read(mybytearray)) > 0) {
            out.write(mybytearray, 0, count);
        }
    }

    private long fileDimension(String name, String username) throws Exception{
        Statement statement = con.createStatement();
        ResultSet re = statement.executeQuery("SELECT id FROM users WHERE username = '" + username + "';");
        re.next();
        String id_user = re.getString("id");
        ResultSet res = statement.executeQuery("SELECT name, dimension FROM files WHERE id_user = '" + id_user + "';");
        String filename = new String();
        long size;
        while(res.next()){
            filename = res.getString("name");
            if(filename.equals(name)){
                size = res.getLong("dimension");
                return size;
            }
        }
        return -1;
    }
}