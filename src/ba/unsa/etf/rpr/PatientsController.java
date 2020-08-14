package ba.unsa.etf.rpr;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class PatientsController {
    private DAO dao;
    private ObservableList<Patient> patients;
    public PatientsController() {
        dao=DAO.getInstance();
        patients= FXCollections.observableArrayList(dao.patients());
    }
    public TextField searchFld;

    @FXML
    public void initialize() {
        
    }

    public void closeAction (ActionEvent actionEvent) {
        Stage stage = (Stage) searchFld.getScene().getWindow();
        stage.close();
    }

    public void addPatientAction (ActionEvent actionEvent) {
        Stage stage = new Stage();
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/formularPacijent.fxml"));
            PatientController patientController = new PatientController(null);
            loader.setController(patientController);
            root = loader.load();
            stage.setTitle("Patient");
            stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            stage.setResizable(true);
            stage.show();

            stage.setOnHiding( event -> {
                Patient patient = patientController.getPatient();
                if (patient != null) {
                    dao.addPatient(patient);
                    patients.setAll(dao.patients());
                }
            } );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

