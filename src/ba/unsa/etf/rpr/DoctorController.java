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
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class DoctorController implements ControllerInterface{
    private DAO dao;
    private Office office;
    private Doctor doctor;
    private ObservableList<Month> months= FXCollections.observableArrayList(Month.JANUARY,Month.FEBRUARY, Month.MARCH, Month.APRIL, Month.MAY, Month.JUNE,
            Month.JULY, Month.AUGUST, Month.SEPTEMBER, Month.OCTOBER, Month.NOVEMBER, Month.DECEMBER);

    public TextField nameFld;
    public TextField lastNameFld;
    public TextField JMBGFld;
    public Spinner daySpinner1;
    public ChoiceBox<Month> monthChoiceBox1;
    public TextField yearFld1;
    public Spinner daySpinner2;
    public ChoiceBox<Month> monthChoiceBox2;
    public TextField yearFld2;
    public TextField POBFld;
    public TextField addressFld;
    public TextField specialtyFld;
    public TextField emailFld;
    public Button addButton;
    private boolean edit;

    public DoctorController(Doctor doctor, Office office, boolean edit) {
        dao=DAO.getInstance();
        this.doctor=doctor;
        this.office=office;
        this.edit=edit;
    }

    private void open() {
        Stage stage=(Stage) addButton.getScene().getWindow();
        Parent root = null;
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/doctors.fxml"),bundle);
        DoctorsController doctorsController = new DoctorsController(office);
        loader.setController(doctorsController);
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Doctors");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    public void initialize() {
        monthChoiceBox1.setItems(months);
        monthChoiceBox2.setItems(months);
        SpinnerValueFactory<Integer> valueFactory1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 31, 1);
        SpinnerValueFactory<Integer> valueFactory2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 31, 1);
        daySpinner1.setValueFactory(valueFactory1);
        daySpinner2.setValueFactory(valueFactory2);
        if (doctor != null) {
            nameFld.setText(doctor.getFirstName());
            lastNameFld.setText(doctor.getLastName());
            JMBGFld.setText(doctor.getJMBG());
            addressFld.setText(doctor.getAddress());
            emailFld.setText(doctor.getEmail());
            POBFld.setText(doctor.getBirthPlace());
            yearFld1.setText(String.valueOf(doctor.getBirthDate().getYear()));
            monthChoiceBox1.getSelectionModel().select(doctor.getBirthDate().getMonthValue()-1);
            daySpinner1.getValueFactory().setValue(doctor.getBirthDate().getDayOfMonth());
            yearFld2.setText(String.valueOf(doctor.getEmploymentDate().getYear()));
            monthChoiceBox2.getSelectionModel().select(doctor.getEmploymentDate().getMonthValue()-1);
            daySpinner2.getValueFactory().setValue(doctor.getEmploymentDate().getDayOfMonth());
            specialtyFld.setText(doctor.getSpecialization());
        } else {
            monthChoiceBox1.getSelectionModel().selectFirst();
            monthChoiceBox2.getSelectionModel().selectFirst();
        }
        yearFld1.textProperty().addListener((obs, oldValue, newValue)-> {
            try {
                Integer.parseInt(newValue);
                yearFld1.getStyleClass().removeAll("notOk");
                addButton.setDisable(false);
            } catch (NumberFormatException e) {
                yearFld1.getStyleClass().add("notOk");
                addButton.setDisable(true);
            }
        });
        yearFld2.textProperty().addListener((obs, oldValue, newValue)-> {
            try {
                Integer.parseInt(newValue);
                yearFld2.getStyleClass().removeAll("notOk");
            } catch (NumberFormatException e) {
                yearFld2.getStyleClass().add("notOk");
            }
        });
    }

    public void addDoctorAction (ActionEvent actionEvent) {
        if (doctor == null) doctor= new Doctor();
        if (nameFld.getText().isEmpty() || lastNameFld.getText().isEmpty() || JMBGFld.getText().isEmpty() || POBFld.getText().isEmpty()
                || addressFld.getText().isEmpty() || specialtyFld.getText().isEmpty() || yearFld1.getText().isEmpty() || yearFld2.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("You have to enter all information except email!");
            alert.showAndWait();
        } else {
            doctor.setFirstName(nameFld.getText());
            doctor.setLastName(lastNameFld.getText());
            doctor.setJMBG(JMBGFld.getText());
            doctor.setBirthPlace(POBFld.getText());
            doctor.setAddress(addressFld.getText());
            doctor.setEmail(emailFld.getText());
            doctor.setBirthDate(LocalDate.of(Integer.parseInt(yearFld1.getText()), monthChoiceBox1.getValue().getValue(), Integer.parseInt(daySpinner1.getValue().toString())));
            doctor.setEmploymentDate(LocalDate.of(Integer.parseInt(yearFld2.getText()), monthChoiceBox2.getValue().getValue(), Integer.parseInt(daySpinner2.getValue().toString())));
            doctor.setSpecialization(specialtyFld.getText());
            if (edit) dao.editDoctor(doctor);
            else  dao.addDoctor(doctor, office.getId());
            open();
        }
    }

    public void closeAction (ActionEvent actionEvent) {
        doctor=null;
        open();
    }

    public Doctor getDoctor () {
        return doctor;
    }
}
