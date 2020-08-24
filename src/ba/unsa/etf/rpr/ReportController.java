package ba.unsa.etf.rpr;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
}
