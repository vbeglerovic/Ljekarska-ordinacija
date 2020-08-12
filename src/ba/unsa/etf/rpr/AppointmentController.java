package ba.unsa.etf.rpr;

import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class AppointmentController {
    public ChoiceBox patientsChoiceBox;
    public ChoiceBox doctorsChoiceBox;
    public AppointmentController() {
    }
    public void closeAction (ActionEvent actionEvent) {
        Stage stage = (Stage) doctorsChoiceBox.getScene().getWindow();
        stage.close();
    }
}
