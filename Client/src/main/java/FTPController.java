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
        DataInputStream in = new DataInputStream(Controller.client.getInputStream());
        DataOutputStream out = new DataOutputStream(Controller.client.getOutputStream());
        out.writeUTF(json);
        Socket client = new Socket(Controller.serverIP, 40000);//93.115.17.244
        System.out.println(json);

        System.out.println(json.toString());

        Window.ftpStage.close();
    }

    @FXML
    private TextField pathText;
    public void pressUpload(ActionEvent e) throws Exception {

        String path = pathText.getText();
        UploadThread up = new UploadThread(path);
        Thread th = new Thread(up);
        th.start();
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
        DownloadThread down = new DownloadThread(path, fileName);
        Thread th = new Thread(down);
        th.start();
    }

    @FXML
    private TextField searchUserText;
    @FXML
    private TextField fileText;
    public void pressSend(ActionEvent e) throws IOException {

        String username = searchUserText.getText();
        String filename = fileText.getText();

        Socket client = new Socket(Controller.serverIP, 40000);

        DataInputStream in = new DataInputStream(client.getInputStream());
        DataOutputStream out = new DataOutputStream(client.getOutputStream());

        String json = new String("{\"type\":\"login\",\"username\":\"" +
                Controller.uName + "\",\"password\":\"" + Controller.uPassword + "\"}");
        out.writeUTF(json);
        String ans = in.readUTF();

        String json2 = new String("{\"type\":\"send\",\"user\":\"" +
                username + "\",\"file\":\"" + filename + "\"}");

        out.writeUTF(json2);
        String answer = in.readUTF();
        System.out.println(answer);


    }



}
