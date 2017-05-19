import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.xml.bind.util.ValidationEventCollector;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class FTPController {

    @FXML
    private Label nameLabel;
    public void pressPush(ActionEvent e) {
        nameLabel.setText(Controller.uName);
    }

    public void pressLogout(ActionEvent e) throws IOException {

        String json = "{\"type\":\"logout\"," +
                "\"username\":\"" + Controller.uName + "\"" + "}";
        out.writeUTF(json);
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
    public void pressUpload(ActionEvent e) throws IOException {

        String path = pathText.getText();
        String fileName = new String(Validate.getNameFromPath(path));

        String json = "{\"type\":\"upload\"," +
                "\"filename\":\"" + fileName + "\"" + "}";
        Socket client = new Socket("127.0.0.1", 40000);//93.115.17.244
        DataInputStream in = new DataInputStream(client.getInputStream());
        DataOutputStream out = new DataOutputStream(client.getOutputStream());
        System.out.println(json);
        out.writeUTF(json);
        String answer = in.readUTF();

    }

}
