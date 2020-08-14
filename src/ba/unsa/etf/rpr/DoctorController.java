package ba.unsa.etf.rpr;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DoctorController {
    public TextField nameFld;
    private Office office;

    public DoctorController(Office office) {
        this.office=office;
    }

    public void closeAction (ActionEvent actionEvent) {
        Stage stage = (Stage) nameFld.getScene().getWindow();
        stage.close();
    }
}
