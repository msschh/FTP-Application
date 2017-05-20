import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.*;

public class FTPController {

    @FXML
    private Label nameLabel;
    public void pressPush(ActionEvent e) {
        nameLabel.setText(Controller.uName);
    }

    private DataInputStream in;
    private DataOutputStream out;


    public void pressLogout(ActionEvent e) throws IOException {

        String json = "{\"type\":\"logout\"," +
                "\"username\":\"" + Controller.uName + "\"" + "}";
        //out.writeUTF(json);
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

        //System.out.println(new File(path).length());

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

}
