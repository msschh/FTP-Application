import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.Socket;

public class Controller{

    static public String uName;
    static public String uPassword;
    static public String serverIP = "localhost";//"93.115.17.244";
    static public Socket client;

    public void pressSignup(ActionEvent e) throws IOException {
        Window w = new Window();
        w.AccountWindow();
        w.stage.close();
    }

    @FXML
    private TextField userfield;
    @FXML
    private PasswordField passfield;
    public void pressLogin(ActionEvent e) throws IOException {
        Window w = new Window();
        //Read username and password
        String username = userfield.getText();
        uName = username;
        userfield.setText("");
        String password = passfield.getText();
        passfield.setText("");
        String encryptedPassword = Validate.vigenereEncryption(password);
        uPassword = encryptedPassword;

        //Create JSON string
        String json = new String("{\"type\":\"login\",\"username\":\"" + username + "\",\"password\":\"" + encryptedPassword + "\"}");
        System.out.println(username);
        System.out.println(encryptedPassword);

        //Send the JSON to the server through a socket, then wait for it's response.
        client = new Socket(serverIP, 40000);//93.115.17.244
        DataInputStream in2 = new DataInputStream(client.getInputStream());
        DataOutputStream out2 = new DataOutputStream(client.getOutputStream());
        out2.writeUTF(json);
        String answer = in2.readUTF();

        System.out.println("ok");

        System.out.print(answer);
        if (answer.equals("Access granted!")) {
            w.FTPWindow();
            w.stage.close();
            System.out.println(username);
            System.out.println(encryptedPassword);

            /*Socket client = new Socket(serverIP, 40000);//93.115.17.244
            DataInputStream in = new DataInputStream(client.getInputStream());
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            out.writeUTF(json);
            in.readUTF();*/
            LoginThread login = new LoginThread(client);
            Thread th = new Thread(login);
            th.start();

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
                    "\"password\":\"" + Validate.vigenereEncryption(pass) + "\"" + "}";
            System.out.println(Validate.vigenereEncryption(pass));
            Socket client = new Socket(serverIP, 40000);//93.115.17.244
            DataInputStream in = new DataInputStream(client.getInputStream());
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            System.out.println(json);
            out.writeUTF(json);
            String answer = in.readUTF();

            System.out.println("ok");

            Window w = new Window();
            if (answer.equals("Singup accepted!")) {
                //w.FTPWindow();
                w.stage.show();
                w.acWindow.close();
            } else {
                w.alertBox();
            }

        } else {
            Window w = new Window();
            w.alertBox();
        }
    }
}
