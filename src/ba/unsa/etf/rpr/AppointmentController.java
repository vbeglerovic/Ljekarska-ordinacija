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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AppointmentController {

    private Appointment appointment;
    private DAO dao;
    private Office office;
    private ObservableList<Patient> patients;
    private ObservableList<Doctor> doctors;
    private ArrayList<String> termini;
    //private ObservableList<String> appointments;

    public ChoiceBox<Patient> patientsChoiceBox;
    public ChoiceBox<Doctor> doctorsChoiceBox;
    public DatePicker datePicker;
    public Spinner hoursSpinner;
    public ChoiceBox<String> minutesChoiceBox;
    public CheckBox kontrolaCheckBox;
    public ListView<String> listView;
    public Label labelDoctor, labelDate;
    public Button button;

    public AppointmentController(Appointment appointment, Office office) {
        this.appointment=appointment;
        this.office=office;
        dao=DAO.getInstance();
        patients=FXCollections.observableArrayList(dao.patients(office.getId()));
        doctors=FXCollections.observableArrayList(dao.doctors(office.getId()));
        LocalTime lt=LocalTime.of(8,0);
        termini=new ArrayList<>();
        for (int i=0; i<28; i++) {
            termini.add(lt.toString());
            lt=lt.plusMinutes(30);
        }

    }

    @FXML
    public void initialize() {
        patientsChoiceBox.setItems(patients);
        SpinnerValueFactory<Integer> valueFactory1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(8, 22, 1);
        hoursSpinner.setValueFactory(valueFactory1);
        minutesChoiceBox.setItems(FXCollections.observableArrayList("00","30"));
        minutesChoiceBox.getSelectionModel().selectFirst();
        doctorsChoiceBox.setItems(doctors);
        listView.setVisible(false);
        labelDate.setVisible(false);
        labelDoctor.setVisible(false);
        button.setDisable(true);
       if (appointment != null) {
           hoursSpinner.getValueFactory().setValue(appointment.getTime().getHour());
           if (appointment.getTime().getMinute()==0)
               minutesChoiceBox.getSelectionModel().selectFirst();
           else
               minutesChoiceBox.getSelectionModel().selectLast();
           patientsChoiceBox.getSelectionModel().select(appointment.getPatient());
           doctorsChoiceBox.getSelectionModel().select(appointment.getDoctor());
           datePicker.setValue(appointment.getDate());
           labelDoctor.setText(doctorsChoiceBox.getValue().toString());
           labelDate.setText(datePicker.getValue().toString());
           if (appointment.getType().toLowerCase().equals("kontrola"))
               kontrolaCheckBox.setSelected(true);
       }
       doctorsChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue )->{
           if (newValue!=null && datePicker.getValue()!=null) button.setDisable(false);
           else if (newValue==null) button.setDisable(true);
       });
        datePicker.valueProperty().addListener((observable, oldValue, newValue)->{
            if (newValue!=null && doctorsChoiceBox.getValue()!=null) button.setDisable(false);
            else if (newValue==null) button.setDisable(true);
        });
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)->{
            hoursSpinner.getValueFactory().setValue(LocalTime.parse(newValue).getHour());
            DateTimeFormatter tf=DateTimeFormatter.ofPattern("HH:mm");
            if (LocalTime.parse(newValue,tf).getMinute()==0)
                minutesChoiceBox.getSelectionModel().selectFirst();
            else
                minutesChoiceBox.getSelectionModel().selectLast();
        });
    }

    public void closeAction (ActionEvent actionEvent) {
        Stage stage = (Stage) patientsChoiceBox.getScene().getWindow();
        stage.close();
    }

    public void makeAppointmentAction (ActionEvent actionEvent) {
        if (appointment==null) appointment=new Appointment();
        appointment.setTime(LocalTime.of(Integer.parseInt(hoursSpinner.getValue().toString()),Integer.parseInt(minutesChoiceBox.getValue())));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/patient.fxml"));
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
public void showListAction (ActionEvent actionEvent) {
        if (doctorsChoiceBox.getValue()==null || datePicker.getValue()==null) return;
        ArrayList<String> free=new ArrayList<>();
        ArrayList<String> zauzeti=dao.getAppointments(doctorsChoiceBox.getValue(),datePicker.getValue(),office.getId());
        for (String s :termini ) {
            if (!zauzeti.contains(s))
                free.add(s);
        }
        labelDoctor.setText(doctorsChoiceBox.getValue().toString());
        labelDoctor.setVisible(true);
        labelDate.setText(datePicker.getValue().toString());
        labelDate.setVisible(true);
        listView.setItems(FXCollections.observableArrayList(free));
        listView.setVisible(true);

}
    public Appointment getAppointment() {
        return appointment;
    }
}
