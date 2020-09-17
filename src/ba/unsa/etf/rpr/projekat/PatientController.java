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
import java.time.Month;
import java.util.Locale;
import java.util.ResourceBundle;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;


public class PatientController implements ControllerInterface {

    private DAO dao;
    private Patient patient;
    private ObservableList<Month> months= FXCollections.observableArrayList(Month.JANUARY,Month.FEBRUARY, Month.MARCH, Month.APRIL, Month.MAY, Month.JUNE,
            Month.JULY, Month.AUGUST, Month.SEPTEMBER, Month.OCTOBER, Month.NOVEMBER, Month.DECEMBER);
    private ObservableList<Status> statusList=FXCollections.observableArrayList(Status.EMPLOYEE, Status.RETIREE, Status.STUDENT, Status.OTHER);

    public TextField nameFld;
    public TextField lastNameFld;
    public TextField JMBGFld;
    public RadioButton maleButton;
    public RadioButton femaleButton;
    public Spinner daySpinner;
    public ChoiceBox<Month> monthChoiceBox;
    public TextField yearFld;
    public TextField POBFld;
    public TextField addressFld;
    public ChoiceBox<Status> statusChoiceBox;
    public TextField emailFld;
    public Button addButton;
    private boolean edit;

    private Office office;

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public PatientController(Patient patient, Office office, boolean edit) {
        dao=DAO.getInstance();
        this.patient=patient;
        this.office=office;
        this.edit=edit;
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
            JMBGFld.setText(patient.getIdentityNumber());
            addressFld.setText(patient.getAddress());
            emailFld.setText(patient.getEmail());
            POBFld.setText(patient.getBirthPlace());
            yearFld.setText(String.valueOf(patient.getBirthDate().getYear()));
            monthChoiceBox.getSelectionModel().select(patient.getBirthDate().getMonthValue()-1);
            statusChoiceBox.getSelectionModel().select(patient.getStatus());
            daySpinner.getValueFactory().setValue(patient.getBirthDate().getDayOfMonth());
        if (patient.getGender() == Gender.MALE)
            maleButton.setSelected(true);
        else
            femaleButton.setSelected(true);
    } else {
            maleButton.setSelected(true);
            statusChoiceBox.getSelectionModel().selectFirst();
            monthChoiceBox.getSelectionModel().selectFirst();

        }
      yearFld.textProperty().addListener((obs,oldValue, newValue)->{
            try {
                if(Integer.parseInt(newValue)>0)
                yearFld.getStyleClass().removeAll("notOk");
                addButton.setDisable(false);
            } catch (NumberFormatException e) {
                yearFld.getStyleClass().add("notOk");
                addButton.setDisable(true);
            }
        });
    }
    private void open() {
        Stage stage=(Stage) statusChoiceBox.getScene().getWindow();
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
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();
    }

    public void closeAction (ActionEvent actionEvent) {
        patient=null;
        open();
    }

    public void addPatient (ActionEvent actionEvent) {
        if (patient == null) patient = new Patient();
        if (nameFld.getText().isEmpty() || lastNameFld.getText().isEmpty() || JMBGFld.getText().isEmpty() || POBFld.getText().isEmpty() || addressFld.getText().isEmpty()) {
            String message, title;
            if (Locale.getDefault().equals(new Locale("bs","BA")))
                message = "Morate unijeti sve podatke (email adresa nije obavezna)!";
            else
                message = "You have to enter all data (email address is optional)!!";
            showAlert(message);
            return;
        }
        patient.setFirstName(nameFld.getText());
        patient.setLastName(lastNameFld.getText());
        patient.setIdentityNumber(JMBGFld.getText());
        patient.setBirthPlace(POBFld.getText());
        patient.setAddress(addressFld.getText());
        patient.setEmail(emailFld.getText());
        if (maleButton.isSelected()) patient.setGender(Gender.MALE);
        else if (femaleButton.isSelected()) patient.setGender(Gender.FEMALE);
        patient.setStatus((Status) statusChoiceBox.getValue());
        patient.setBirthDate(LocalDate.of(Integer.parseInt(yearFld.getText()), monthChoiceBox.getValue().getValue(), Integer.parseInt(daySpinner.getValue().toString())));
        if (edit) dao.editPatient(patient);
        else dao.addPatient(patient,office.getId());
        open();
    }

    public Patient getPatient() {
        return patient;
    }
}
