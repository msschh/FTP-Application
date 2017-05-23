import java.io.*;
import java.net.Socket;

public class UploadThread implements Runnable{

    private String path;
    private DataInputStream in;
    private DataOutputStream out;

    public UploadThread(String path){
        this.path = path;
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

            String fileName = new String(Validate.getNameFromPath(path));

            json = "{\"type\":\"upload\"," +
                    "\"size\":\"" + new File(path).length() + "\"," +
                    "\"filename\":\"" + fileName + "\"" + "}";

            System.out.println(json);

            out.writeUTF(json);

            //System.out.println(path);
            FileInputStream fin = new FileInputStream(path);
            byte[] mybytearray = new byte[16 * 1024];
            int count;
            while ((count = fin.read(mybytearray)) > 0) {
                out.write(mybytearray, 0, count);
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
