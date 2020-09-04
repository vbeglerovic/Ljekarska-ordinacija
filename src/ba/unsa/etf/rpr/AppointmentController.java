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
import java.util.ResourceBundle;

public class AppointmentController {

    private Appointment appointment;
    private DAO dao;
    private Office office;
    private ObservableList<Patient> patients;
    private ObservableList<Doctor> doctors;
    private ArrayList<String> allAppointments;


    public ChoiceBox<Patient> patientsChoiceBox;
    public ChoiceBox<Doctor> doctorsChoiceBox;
    public DatePicker datePicker;
    public CheckBox controlCheckBox;
    public ListView<String> listView;

    public AppointmentController(Appointment appointment, Office office) {
        this.appointment=appointment;
        this.office=office;
        dao=DAO.getInstance();
        patients=FXCollections.observableArrayList(dao.patients(office.getId()));
        doctors=FXCollections.observableArrayList(dao.doctors(office.getId()));
        LocalTime lt=LocalTime.of(8,0);
        allAppointments=new ArrayList<>();
        for (int i=0; i<28; i++) {
            allAppointments.add(lt.toString());
            lt=lt.plusMinutes(30);
        }

    }
    private void showInfoDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setContentText("There are no free terms on date "+datePicker.getValue().toString()+" for doctor "+doctorsChoiceBox.getSelectionModel().getSelectedItem().toString()+".");

        alert.showAndWait();
    }


    @FXML
    public void initialize() {
        patientsChoiceBox.setItems(patients);
        doctorsChoiceBox.setItems(doctors);
       if (appointment != null) {
           doctorsChoiceBox.getSelectionModel().select(appointment.getDoctor());
           patientsChoiceBox.getSelectionModel().select(appointment.getPatient());
           datePicker.setValue(appointment.getDate());
           if (appointment.getType()!=null)
               controlCheckBox.setSelected(true);
       }
       doctorsChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue)-> {
           if (datePicker.getValue()!=null) {
               try {
                   showListAction();
               } catch (ThereAreNoFreeTerms thereAreNoFreeTerms) {
                   showInfoDialog();
                   return;
               }
           }
       });
       datePicker.valueProperty().addListener((obs, oldValue, newValue)->{
           if (doctorsChoiceBox.getSelectionModel().getSelectedItem()!=null) {
               try {
                   showListAction();
               } catch (ThereAreNoFreeTerms thereAreNoFreeTerms) {
                   showInfoDialog();
                   return;
               }
           }
       });

    }

    public void closeAction (ActionEvent actionEvent) {
        appointment=null;
        Stage stage = (Stage) patientsChoiceBox.getScene().getWindow();
        stage.close();
    }

    public void makeAppointmentAction (ActionEvent actionEvent) {
        if (appointment==null) appointment=new Appointment();
        if (patientsChoiceBox.getValue()==null || doctorsChoiceBox.getValue()==null || datePicker.getValue()==null || listView.getSelectionModel().getSelectedItem()==null ) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Morate popuniti sve podatke!");
            alert.showAndWait();
            return;
        }
            DateTimeFormatter tf=DateTimeFormatter.ofPattern("HH:mm");
            appointment.setTime(LocalTime.parse(listView.getSelectionModel().getSelectedItem(),tf));
            appointment.setDate(datePicker.getValue());
            appointment.setPatient(patientsChoiceBox.getValue());
            appointment.setDoctor(doctorsChoiceBox.getValue());
            if (controlCheckBox.isSelected()) appointment.setType("Kontrola");
            else appointment.setType("Prvi pregled");
        Stage stage = (Stage) patientsChoiceBox.getScene().getWindow();
        stage.close();

    }
    public void addPatientAction (ActionEvent actionEvent) {
        Stage stage = new Stage();
        Parent root = null;
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("Translation");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/patient.fxml"),bundle);
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
    private void showListAction () throws ThereAreNoFreeTerms {
        if (doctorsChoiceBox.getValue()==null || datePicker.getValue()==null) return;
        ArrayList<String> free=new ArrayList<>();
        ArrayList<String> reservedAppointments=dao.getAppointments(doctorsChoiceBox.getValue(),datePicker.getValue(),office.getId());
        for (String s :allAppointments ) {
            if (!reservedAppointments.contains(s))
                free.add(s);
        }
        if (free.size()==0) {
            throw new ThereAreNoFreeTerms("There are no free terms.");
        }
        listView.setItems(FXCollections.observableArrayList(free));
        listView.setVisible(true);

}
    public Appointment getAppointment() {
        return appointment;
    }
}
