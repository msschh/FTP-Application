import javafx.fxml.FXML;

import java.awt.*;

public class WinController {

    @FXML
    private TextField answerText;
    public void pressOk() {

        String answer = answerText.getText();
        if (answer.equals("da") || answer.equals("Da") || answer.equals("DA")) {
            LoginThread.userAnswer = "Yes";
        } else {
            LoginThread.userAnswer = "No";
        }

        Window.acceptWindow.close();

    }

}
