import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

public class Window {

    static Stage acWindow;
    static Stage ftpStage;
    static Stage stage;
    static boolean create = false;

    public void AccountWindow() throws IOException {
        Stage window = new Stage();
        this.create = true;
        Parent rootnew = FXMLLoader.load(getClass().getResource("accountwindow.fxml"));

        Scene scene = new Scene(rootnew);
        window.setResizable(false);
        window.setTitle("Create Account");
        window.setScene(scene);
        this.acWindow = window;
        window.show();

    }

    public void FTPWindow() throws IOException {
        Stage window = new Stage();

        Parent rootnew = FXMLLoader.load(getClass().getResource("ftpwindow.fxml"));
        Scene scene = new Scene(rootnew);
        window.setResizable(false);
        window.setTitle("FTP");
        window.setScene(scene);
        this.ftpStage = window;
        stage.close();
        if (create) {
            System.out.println(create);
            this.acWindow.close();
            create = false;
        }
        window.show();

    }

    public void alertBox() throws IOException {
        Stage window = new Stage();

        Parent rootnew = FXMLLoader.load(getClass().getResource("alertbox.fxml"));
        Scene scene = new Scene(rootnew);
        window.setResizable(false);
        window.setTitle("Alert!");
        window.setScene(scene);
        window.show();

    }

}
