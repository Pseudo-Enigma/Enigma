package enigma;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class outputController {
    @FXML
    private TextArea ta_output;
    private String out;

    

    public void getOuput(String output) {
        out = output;
    }
}
