package ba.unsa.etf.rpr;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Optional;

public class ReportController {
    private Office office;
    private Appointment appointment;

    public TextField patientFld;
    public TextField genderFld;
    public TextField DOBFld;
    public TextField doctorFld;
    public TextField dateTimeFld;
    public TextField jmbFld;
    public TextField addressFld;
    public TextArea anamnesisFld;
    public TextArea diagnosisFld;
    public TextArea recommendationFld;

    public ReportController(Office office, Appointment appointment) {
        this.appointment=appointment;
        this.office=office;
    }

    @FXML
    public void initialize() {
        if (appointment!=null) {
            patientFld.setText(appointment.getPatient().toString());
            doctorFld.setText(appointment.getDoctor().toString());
            genderFld.setText(appointment.getPatient().getGender().toString());
            DOBFld.setText(appointment.getPatient().getBirthDate().toString());
            dateTimeFld.setText(appointment.getDate()+" "+appointment.getTime().toString());
            jmbFld.setText(appointment.getPatient().getJMBG());
            addressFld.setText(appointment.getPatient().getAddress());
        }
    }

    public void closeAction (ActionEvent actionEvent) {
        Stage stage=(Stage) patientFld.getScene().getWindow();
        stage.close();
    }

    public void addReportAction (ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setContentText("Are you sure you want add a report?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            appointment.setAnamnesis(anamnesisFld.getText());
            appointment.setDiagnosis((diagnosisFld.getText()));
            appointment.setRecommendation(recommendationFld.getText());
            Stage stage=(Stage) patientFld.getScene().getWindow();
            stage.close();
        }
    }

    public Appointment getAppointment () {
        return appointment;
    }
}
