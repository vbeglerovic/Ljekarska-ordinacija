package ba.unsa.etf.rpr.projekat;

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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class AppointmentController implements ControllerInterface{

    private Appointment appointment;
    private DAO dao;
    private Office office;
    private ObservableList<Patient> patients;
    private ObservableList<Doctor> doctors;
    private ArrayList<LocalTime> allAppointments;

    public ChoiceBox<Patient> patientsChoiceBox;
    public ChoiceBox<Doctor> doctorsChoiceBox;
    public DatePicker datePicker;
    public CheckBox firstAppointmentCheckBox;
    public ListView<LocalTime> listView;
    public Button closeButton, addButton;
    //sljedeci atribut se koristi da se odredi koji prozor treba otvoriti nakon zatvaranja trenutnog
    private boolean edit;

    public AppointmentController(Appointment appointment, Office office, boolean edit) {
        this.appointment=appointment;
        this.office=office;
        this.edit=edit;
        dao=DAO.getInstance();
        patients=FXCollections.observableArrayList(dao.patients(office.getId()));
        doctors=FXCollections.observableArrayList(dao.doctors(office.getId()));
        //allAppointments cu koristiti da nadjem sve slobodne termine
        //svaki pregled traje pola sata
        LocalTime lt=LocalTime.of(8,0);
        allAppointments=new ArrayList<>();
        for (int i=0; i<28; i++) {
            allAppointments.add(lt);
            lt=lt.plusMinutes(30);
        }
    }

    @FXML
    public void initialize() {
        patientsChoiceBox.setItems(patients);
        doctorsChoiceBox.setItems(doctors);
        if (appointment != null) {
            doctorsChoiceBox.getSelectionModel().select(appointment.getDoctor());
            patientsChoiceBox.getSelectionModel().select(appointment.getPatient());
            datePicker.setValue(appointment.getDate());
            if (appointment.isFirstAppointment())
                firstAppointmentCheckBox.setSelected(true);
            listView.setItems(FXCollections.observableList(allAppointments));
            listView.getSelectionModel().select(appointment.getTime());
        }
        doctorsChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue)-> {
            if (datePicker.getValue()!=null) {
                try {
                    showListAction();
                } catch (NoFreeTerms noFreeTerms) {
                    String message;
                    if (Locale.getDefault().equals(new Locale("bs","BA")))
                        message = "Nema slobodnih termina na datum " + datePicker.getValue().toString() + " za doktora " + doctorsChoiceBox.getValue().toString() + "!";
                    else
                        message="There are no free terms on date "+datePicker.getValue().toString()+" for doctor "+doctorsChoiceBox.getValue().toString()+"!";
                    showDialog(message);
                    return;
                }
            }
        });
        datePicker.valueProperty().addListener((obs, oldValue, newValue)->{
            if (doctorsChoiceBox.getSelectionModel().getSelectedItem()!=null) {
                try {
                    showListAction();
                } catch (NoFreeTerms noFreeTerms) {
                    String message;
                    if (Locale.getDefault().equals(new Locale("bs","BA")))
                        message = "Nema slobodnih termina na datum " + datePicker.getValue().toString() + " za doktora " + doctorsChoiceBox.getValue().toString() + "!";
                    else
                        message="There are no free terms on date "+datePicker.getValue().toString()+" for doctor "+doctorsChoiceBox.getValue().toString()+"!";
                    showDialog(message);
                    return;
                }
            }
        });
        datePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) < 0 );
            }
        });
    }

    private void showDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showListAction () throws NoFreeTerms {
        if (doctorsChoiceBox.getValue()==null || datePicker.getValue()==null) return;
        ArrayList<LocalTime> free=new ArrayList<>();
        ArrayList<LocalTime> reservedAppointments=dao.getAppointments(doctorsChoiceBox.getValue(),datePicker.getValue(),office.getId());
        for (LocalTime s :allAppointments ) {
            if (!reservedAppointments.contains(s)) {
                if ((datePicker.getValue().equals(LocalDate.now()) && s.isAfter(LocalTime.now()))|| datePicker.getValue().isAfter(LocalDate.now()))
                    free.add(s);
            }
        }
        if (free.size()==0) {
            throw new NoFreeTerms("There are no free terms.");
        }
        listView.setItems(FXCollections.observableArrayList(free));
        listView.getSelectionModel().clearSelection();
        listView.setVisible(true);
    }

    private void openStageWithAllAppointments() {
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

    private void openMainStage() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        Parent root = null;
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/office.fxml"),bundle);
        OfficeController officeController = new OfficeController(office);
        loader.setController(officeController);
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Office");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.setResizable(true);
        stage.show();
    }

    public void closeAction (ActionEvent actionEvent) {
        appointment=null;
        if (edit) openStageWithAllAppointments();
        else openMainStage();
    }

    public void makeAppointmentAction (ActionEvent actionEvent) {
        if (appointment==null) appointment=new Appointment();
        if (patientsChoiceBox.getValue()==null || doctorsChoiceBox.getValue()==null || datePicker.getValue()==null || listView.getSelectionModel().getSelectedItem()==null ) {
            String message;
            if (Locale.getDefault().equals(new Locale("bs","BA")))
                message = "Popunite sve podatke!";
            else
                message="Enter all data!";
            showDialog(message);
            return;
        }
        appointment.setTime(listView.getSelectionModel().getSelectedItem());
        appointment.setDate(datePicker.getValue());
        appointment.setPatient(patientsChoiceBox.getValue());
        appointment.setDoctor(doctorsChoiceBox.getValue());
        if ( firstAppointmentCheckBox.isSelected())
               appointment.setFirstAppointment(true);
        else
            appointment.setFirstAppointment(false);
        if (edit) {
            dao.editAppointment(appointment);
            openStageWithAllAppointments();
        } else {
            dao.addAppointment(appointment,office.getId());
            openMainStage();
        }
    }


    public Appointment getAppointment() {
        return appointment;
    }
}
