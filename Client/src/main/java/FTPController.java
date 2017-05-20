import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.json.JSONObject;

import javax.xml.bind.util.ValidationEventCollector;
import java.io.*;
import java.net.Socket;

import static java.lang.System.in;

public class FTPController {

    @FXML
    private Label nameLabel;
    public void pressPush(ActionEvent e) {
        nameLabel.setText(Controller.uName);
    }

    public void pressLogout(ActionEvent e) throws IOException {

        String json = "{\"type\":\"logout\"," +
                "\"username\":\"" + Controller.uName + "\"" + "}";
       // out.writeUTF(json);
    /*    Socket client = new Socket("127.0.0.1", 40000);//93.115.17.244
        DataInputStream in = new DataInputStream(client.getInputStream());
        DataOutputStream out = new DataOutputStream(client.getOutputStream());
        System.out.println(json);
        String answer = in.readUTF();

        System.out.println(json.toString());
    */
        Window.ftpStage.close();
    }

    @FXML
    private TextField pathText;
    public void pressUpload(ActionEvent e) throws Exception {

        String path = pathText.getText();
        String fileName = new String(Validate.getNameFromPath(path));

        String json = "{\"type\":\"upload\"," +
                "\"size\":\"" + new File(path).length() + "\"," +
                "\"filename\":\"" + fileName + "\"" + "}";

        DataInputStream in = new DataInputStream(Controller.client.getInputStream());
        DataOutputStream out = new DataOutputStream(Controller.client.getOutputStream());
        System.out.println(json);
        out.writeUTF(json);

        //System.out.println(path);
        FileInputStream fin = new FileInputStream(path);
        byte [] mybytearray  = new byte [16*1024];
        int count;
        while ((count = fin.read(mybytearray)) > 0) {
            out.write(mybytearray, 0, count);
        }
    }

    @FXML
    private TextField downPath;
    @FXML
    private TextField fileNameText;
    public void pressDownload(ActionEvent e) throws Exception {

        String path = downPath.getText();
        downPath.setText("...");
        String fileName = fileNameText.getText();
        fileNameText.setText("...");

        String json = "{\"type\":\"download\"," +
                "\"filename\":\"" + fileName + "\"" + "}";

        DataInputStream in = new DataInputStream(Controller.client.getInputStream());
        DataOutputStream out = new DataOutputStream(Controller.client.getOutputStream());
        System.out.println(json);
        out.writeUTF(json);

        JSONObject jsonObject = new JSONObject(in.readUTF());

        String answer = jsonObject.getString("type");
        String fileSize = jsonObject.getString("size");
        long size = Long.parseLong(fileSize);

        if (answer.equals("Yes")) {
            System.out.println("Merge!");
            System.out.println(size);


            path += "\\";
            path += fileName;
            FileOutputStream fout = new FileOutputStream(path);
            byte[] mybytearray = new byte[16 * 1024];
            int count;
            long length = 0;
            while ((count = in.read(mybytearray)) > 0 && length + count <= size) {
                fout.write(mybytearray, 0, count);
                length += count;
                if(length == size){
                    return;
                }
            }


        } else {
            System.out.println("---> Fisierul nu exista!");
        }
    }



}
