import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class Controller {

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
        String username = userfield.getText();
        userfield.setText("");
        String password = passfield.getText();
        passfield.setText("");
        String encryptedPassword = Validate.vigenereEncryption(password);
        // if se afla in baza de date si parola e ok
        if (true) {
            w.FTPWindow();
            System.out.println(username);
            System.out.println(encryptedPassword);
        } else {
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
            System.out.println("Invalid firstname");
        }
        if (!valid.validName(lastName)) {
            ok = false;
            System.out.println("Invalid lastname");
        }
        if (!valid.validUsername(userName)) {
            ok = false;
            System.out.println("Invalid username");
        }
        if (!valid.validEmail(eMail)) {
            ok = false;
            System.out.println("Invalid email");
        }
        if (!valid.validPassword(pass)) {
            ok = false;
            System.out.println("Invalid password");
        }

        if (ok) {
            System.out.println("Cont Valid!!!");

        } else {
            Window w = new Window();
            w.alertBox();
        }
    }
}
