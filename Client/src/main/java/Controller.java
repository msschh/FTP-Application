import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.json.JSONObject;
import sample.*;

import java.io.*;
import java.net.Socket;

public class Controller{

    public void pressSignup(ActionEvent e) throws IOException {
        Window w = new Window();
        w.AccountWindow();
    }

    @FXML
    private TextField userfield;
    @FXML
    private PasswordField passfield;
    public void pressLogin(ActionEvent e) throws IOException {
        Window w = new Window();
        //Read username and password
        String username = userfield.getText();
        userfield.setText("");
        String password = passfield.getText();
        passfield.setText("");
        String encryptedPassword = Validate.vigenereEncryption(password);

        //Create JSON string
        String json = new String("{\"type\":\"login\",\"username\":\"" + username + "\",\"password\":\"" + encryptedPassword + "\"}");
        //System.out.println(json);

        //Send the JSON to the server through a socket, then wait for it's response.
        Socket client = new Socket("93.115.17.244", 40000);
        DataInputStream in = new DataInputStream(client.getInputStream());
        DataOutputStream out = new DataOutputStream(client.getOutputStream());
        out.writeUTF(json);
        String answer = in.readUTF();

        if (answer.equals("Access granted!")) {
            w.FTPWindow();
            Main.stage.close();
            System.out.println(username);
            System.out.println(encryptedPassword);
        } else {
            System.out.println(answer);
            w.alertBox();
        }
    }

    @FXML
    private TextField fname, lname, email, username;
    @FXML
    private PasswordField password;
    public void pressCreate(ActionEvent e) throws IOException {
        String firstName = fname.getText(); fname.setText("");
        String lastName = lname.getText(); lname.setText("");
        String eMail =  email.getText(); email.setText("");
        String userName = username.getText(); username.setText("");
        String pass = password.getText(); password.setText("");

        Validate valid = new Validate();
        boolean ok = true;
        if (!valid.validName(firstName)) {
            ok = false;
        }
        if (!valid.validName(lastName)) {
            ok = false;
        }
        if (!valid.validUsername(userName)) {
            ok = false;
        }
        if (!valid.validEmail(eMail)) {
            ok = false;
        }
        if (!valid.validPassword(pass)) {
            ok = false;
        }

        if (ok) {
            String json = "{\"type\":\"signup\"," +
                    "\"firstname\":\"" + firstName + "\"," +
                    "\"lastname\":\"" + lastName + "\"," +
                    "\"email\":\"" + eMail + "\"," +
                    "\"username\":\"" + userName + "\"," +
                    "\"password\":\"" + pass + "\"" + "}";
            Socket client = new Socket("93.115.17.244", 40000);
            DataInputStream in = new DataInputStream(client.getInputStream());
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            out.writeUTF(json);
            String answer = in.readUTF();

            Window w = new Window();
            if (answer.equals("Singup accepted!")) {
                w.FTPWindow();
            } else {
                w.alertBox();
            }

        } else {
            Window w = new Window();
            w.alertBox();
        }
    }
}
