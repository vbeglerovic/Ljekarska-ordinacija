package ba.unsa.etf.rpr;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PatientController {
    private DAO dao;
    public TextField nameFld;

    public PatientController() {
        dao=DAO.getInstance();
    }

    public void closeAction (ActionEvent actionEvent) {
        Stage stage = (Stage) nameFld.getScene().getWindow();
        stage.close();
    }
}
