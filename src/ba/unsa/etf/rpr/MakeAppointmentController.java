package ba.unsa.etf.rpr;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

import java.time.LocalTime;


import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class MakeAppointmentController {

    private Appointment appointment;
    private DAO dao;
    private Office office;
    private ObservableList<Patient> patients;
    private ObservableList<Doctor> doctors;

    public ChoiceBox<Patient> patientsChoiceBox;
    public ChoiceBox<Doctor> doctorsChoiceBox;
    public DatePicker datePicker;
    public Spinner hoursSpinner;
    public Spinner minutesSpinner;
    public CheckBox kontrolaCheckBox;

    public MakeAppointmentController(Appointment appointment,Office office) {
        this.appointment=appointment;
        this.office=office;
        dao=DAO.getInstance();
        patients=FXCollections.observableArrayList(dao.patients(office.getId()));
        doctors=FXCollections.observableArrayList(dao.doctors(office.getId()));
    }

    @FXML
    public void initialize() {
        patientsChoiceBox.setItems(patients);
        SpinnerValueFactory<Integer> valueFactory1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(8, 22, 1);
        hoursSpinner.setValueFactory(valueFactory1);
        SpinnerValueFactory<Integer> valueFactory2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,55, 0,5);
        minutesSpinner.setValueFactory(valueFactory2);
        doctorsChoiceBox.setItems(doctors);
       if (appointment != null) {
           patientsChoiceBox.getSelectionModel().select(appointment.getPatient());
           doctorsChoiceBox.getSelectionModel().select(appointment.getDoctor());
           datePicker.setValue(appointment.getDate());
       }
    }

    public void closeAction (ActionEvent actionEvent) {
        Stage stage = (Stage) patientsChoiceBox.getScene().getWindow();
        stage.close();
    }

    public void makeAppointmentAction (ActionEvent actionEvent) {
        if (appointment==null) appointment=new Appointment();
        appointment.setTime(LocalTime.of(Integer.parseInt(hoursSpinner.getValue().toString()),Integer.parseInt(minutesSpinner.getValue().toString())));
        appointment.setDate(datePicker.getValue());
        appointment.setPatient(patientsChoiceBox.getValue());
        appointment.setDoctor(doctorsChoiceBox.getValue());
        if (kontrolaCheckBox.isSelected()) appointment.setType("Kontrola");
        closeAction(null);
    }
    public void addPatientAction (ActionEvent actionEvent) {
        Stage stage = new Stage();
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/formularPacijent.fxml"));
            PatientController patientController = new PatientController(null,office);
            loader.setController(patientController);
            root = loader.load();
            stage.setTitle("Patient");
            stage.setScene(new Scene(root, 600, 400));
            stage.setResizable(true);
            stage.show();

            stage.setOnHiding(event -> {
                Patient patient = patientController.getPatient();
                if (patient != null) {
                    dao.addPatient(patient,office.getId());
                    patients=FXCollections.observableArrayList(dao.patients(office.getId()));
                    patientsChoiceBox.setItems(patients);

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Appointment getAppointment() {
        return appointment;
    }
}
