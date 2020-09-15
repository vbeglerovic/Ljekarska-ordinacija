package ba.unsa.etf.rpr;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class ReportController implements ControllerInterface {

    private Office office;
    private Appointment appointment;
    private DAO dao;

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
    public Button closeButton;

    public ReportController(Office office, Appointment appointment) {
        dao=DAO.getInstance();
        this.appointment=appointment;
        this.office=office;
    }

    private void open() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        Parent root = null;
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/appointments.fxml"),bundle);
        AppointmentsController appointmentsController = new AppointmentsController(office);
        loader.setController(appointmentsController);
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Appointments");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    public void initialize() {
        if (appointment!=null) {
            patientFld.setText(appointment.getPatient().toString());
            doctorFld.setText(appointment.getDoctor().toString());
            genderFld.setText(appointment.getPatient().getGender().toString());
            DOBFld.setText(appointment.getPatient().getBirthDate().toString());
            dateTimeFld.setText(appointment.getDate()+" "+appointment.getTime().toString());
            jmbFld.setText(appointment.getPatient().getIdentityNumber());
            addressFld.setText(appointment.getPatient().getAddress());
        }
    }

    public void closeAction (ActionEvent actionEvent) {
        appointment=null;
       open();
    }

    public void addReportAction (ActionEvent actionEvent) {
        String message;
        if (Locale.getDefault().equals(new Locale("bs","BA")))
            message = "Da li ste sigurni da želite dodati izvještaj?";
        else
            message = "Are you sure you want to add report?";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            appointment.setAnamnesis(anamnesisFld.getText());
            appointment.setDiagnosis((diagnosisFld.getText()));
            appointment.setRecommendation(recommendationFld.getText());
            dao.addReport(appointment);
            open();
        }
    }

    public Appointment getAppointment () {
        return appointment;
    }
}
