package ba.unsa.etf.rpr;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;



public class PatientController {
    private DAO dao;
    private Patient patient;
    private ObservableList<Months> mjeseci= FXCollections.observableArrayList(Months.Januar,Months.Februar,Months.Mart,Months.April, Months.Maj, Months.Juni, Months.Juli, Months.August,Months.Septembar, Months.Oktobar, Months.Novembar, Months.Decembar);
    private ObservableList<Status> statusi=FXCollections.observableArrayList(Status.EMPLOYEE, Status.RETIREE, Status.STUDENT, Status.OTHER);
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
    /*private String getDate () {
        String day,month;
        if (daySpinner.getValue().toString().length()<2)
            day="0"+daySpinner.getValue();
        else
            day=daySpinner.getValue().toString();
        Integer m=mjeseci.indexOf(monthChoiceBox.getValue())+1;
        if (m<10)
            month="0"+m;
        else
            month=m.toString();
        String date=day+"-"+month+"-"+yearFld.getText();
        return date;
    }*/
    @FXML
    public void initialize() {
        monthChoiceBox.setItems(mjeseci);
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 31, 1);
        daySpinner.setValueFactory(valueFactory);
        statusChoiceBox.setItems(statusi);
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
        if (patient.getGender() == Gender.MUSKO)
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
        stage.close();
    }

    public void addPatient (ActionEvent actionEvent) {
        if (patient == null) patient = new Patient();
        patient.setFirstName(nameFld.getText());
        patient.setLastName(lastNameFld.getText());
        patient.setJMBG(JMBGFld.getText());
        patient.setBirthPlace(POBFld.getText());
        patient.setAddress(addressFld.getText());
        patient.setEmail(emailFld.getText());
        if (musko.isSelected()) patient.setGender(Gender.MUSKO);
        else if (zensko.isSelected()) patient.setGender(Gender.ZENSKO);
        patient.setStatus((Status) statusChoiceBox.getValue());
        /*DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        patient.setBirthDate(LocalDate.parse(getDate(),df));*/
        patient.setBirthDate(LocalDate.of(Integer.parseInt(yearFld.getText()),mjeseci.indexOf(monthChoiceBox.getValue())+1, Integer.parseInt(daySpinner.getValue().toString())));
        Stage stage = (Stage) nameFld.getScene().getWindow();
        stage.close();
    }

    public Patient getPatient() {
        return patient;
    }
}
