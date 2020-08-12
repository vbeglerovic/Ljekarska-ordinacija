package ba.unsa.etf.rpr;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class ZakaziPregledController {
    private DAO dao;
    public Button closeButton;
    public ChoiceBox patientsChoiceBox;
    public ChoiceBox doctorsChoiceBox;
    public DatePicker datePicker;
    public Spinner hoursSpinner;
    public Spinner minutesSpinner;


    public void closeAction (ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void makeAppointmentAction (ActionEvent actionEvent) {
        //Appointment appointment=new Appointment(datePicker.getValue());
    }
    public void addPatientAction (ActionEvent actionEvent) {
        Stage stage = new Stage();
        Parent root = null;
        //ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/formularPacijent.fxml"));
        PatientController patientController = new PatientController();
        loader.setController(patientController);
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Patient");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();
    }

}
