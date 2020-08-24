package ba.unsa.etf.rpr;

import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ReportController {
    private Office office;
    public TextField nameField;
    public ReportController(Office office) {
        this.office=office;
    }

    public void closeAction (ActionEvent actionEvent) {
        Stage stage=(Stage) nameField.getScene().getWindow();
        stage.close();
    }
}
