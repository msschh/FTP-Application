import org.json.JSONObject;

import java.io.*;
import java.net.Socket;

public class DownloadThread implements Runnable{

    private String path;
    private String fileName;
    private DataInputStream in;
    private DataOutputStream out;

    public DownloadThread(String path, String fileName){
        this.path = path;
        this.fileName = fileName;
    }

    @Override
    public void run(){
        try {
            Socket client = new Socket(Controller.serverIP, 40000);

            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());
            String json = new String("{\"type\":\"login\",\"username\":\"" + Controller.uName + "\",\"password\":\"" + Controller.uPassword + "\"}");
            out.writeUTF(json);
            in.readUTF();


            json = "{\"type\":\"download\"," +
                    "\"filename\":\"" + fileName + "\"" + "}";

            System.out.println(json);
            out.writeUTF(json);

            JSONObject jsonObject = new JSONObject(in.readUTF());

            String answer = jsonObject.getString("type");

            if (answer.equals("Yes")) {
                System.out.println("Merge!");


                path += "\\";
                path += fileName;
                FileOutputStream fout = new FileOutputStream(path);
                byte[] mybytearray = new byte[16 * 1024];
                int count;
                while ((count = in.read(mybytearray)) >= 0) {
                    fout.write(mybytearray, 0, count);
                }
            } else {
                System.out.println("---> Fisierul nu exista!");
            }
        }
        catch (Exception e){
            System.out.print(e);
        }
        finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
            }
            catch (IOException e){
                System.out.print(e);
            }
        }
    }
}
