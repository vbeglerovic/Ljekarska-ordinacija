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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;


public class PatientController {
    private DAO dao;
    private Patient patient;
    private ObservableList<String> months= FXCollections.observableArrayList("January","February","March","April", "May", "June", "July", "August","September", "October", "November", "December");
    private ObservableList<Status> statusList=FXCollections.observableArrayList(Status.EMPLOYEE, Status.RETIREE, Status.STUDENT, Status.OTHER);
    public TextField nameFld;
    public TextField lastNameFld;
    public TextField JMBGFld;
    public RadioButton musko;
    public RadioButton zensko;
    public Spinner daySpinner;
    public ChoiceBox monthChoiceBox;
    public TextField yearFld;
    public TextField DOBFld;
    public TextField POBFld;
    public TextField addressFld;
    public ChoiceBox statusChoiceBox;
    public TextField emailFld;

    private Office office;



    public PatientController(Patient patient, Office office) {
        dao=DAO.getInstance();
        this.patient=patient;
        this.office=office;
    }
    @FXML
    public void initialize() {
        monthChoiceBox.setItems(months);
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 31, 1);
        daySpinner.setValueFactory(valueFactory);
        statusChoiceBox.setItems(statusList);
        if (patient != null) {
            nameFld.setText(patient.getFirstName());
            lastNameFld.setText(patient.getLastName());
            JMBGFld.setText(patient.getJMBG());
            addressFld.setText(patient.getAddress());
            emailFld.setText(patient.getEmail());
            POBFld.setText(patient.getBirthPlace());
            yearFld.setText(String.valueOf(patient.getBirthDate().getYear()));
            monthChoiceBox.getSelectionModel().select(patient.getBirthDate().getMonthValue()-1);
            statusChoiceBox.getSelectionModel().select(patient.getStatus());
            daySpinner.getValueFactory().setValue(patient.getBirthDate().getDayOfMonth());
        if (patient.getGender() == Gender.MALE)
            musko.setSelected(true);
        else
            zensko.setSelected(true);
    } else {
            statusChoiceBox.getSelectionModel().selectFirst();
            monthChoiceBox.getSelectionModel().selectFirst();
        }
    }

    public void closeAction (ActionEvent actionEvent) {
        Stage stage = (Stage) nameFld.getScene().getWindow();
        Parent root = null;
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/patients.fxml"),bundle);
        PatientsController patientsController = new PatientsController(office);
        loader.setController(patientsController);
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Patients");
        stage.setScene(new Scene(root, 600, 400));
        stage.setResizable(false);
        stage.show();
    }

    public void addPatient (ActionEvent actionEvent) {
        if (patient == null) patient = new Patient();
        patient.setFirstName(nameFld.getText());
        patient.setLastName(lastNameFld.getText());
        patient.setJMBG(JMBGFld.getText());
        patient.setBirthPlace(POBFld.getText());
        patient.setAddress(addressFld.getText());
        patient.setEmail(emailFld.getText());
        if (musko.isSelected()) patient.setGender(Gender.MALE);
        else if (zensko.isSelected()) patient.setGender(Gender.FEMALE);
        patient.setStatus((Status) statusChoiceBox.getValue());
        patient.setBirthDate(LocalDate.of(Integer.parseInt(yearFld.getText()),months.indexOf(monthChoiceBox.getValue())+1, Integer.parseInt(daySpinner.getValue().toString())));
        Stage stage=(Stage) nameFld.getScene().getWindow();
        stage.close();
    }

    public Patient getPatient() {
        return patient;
    }
}
