package ba.unsa.etf.rpr;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

public class DoctorController {
    public TextField nameFld;
    public TextField lastNameFld;
    public TextField JMBGFld;
    public Spinner daySpinner1;
    public ChoiceBox monthChoiceBox1;
    public TextField yearFld1;
    public Spinner daySpinner2;
    public ChoiceBox monthChoiceBox2;
    public TextField yearFld2;
    public TextField POBFld;
    public TextField addressFld;
    public TextField specialtyFld;
    public TextField emailFld;

    private Office office;
    private Doctor doctor;
    private ObservableList<Months> mjeseci= FXCollections.observableArrayList(Months.Januar,Months.Februar,Months.Mart,Months.April, Months.Maj, Months.Juni, Months.Juli, Months.August,Months.Septembar, Months.Oktobar, Months.Novembar, Months.Decembar);


    public DoctorController(Office office) {
        this.office=office;
    }

    @FXML
    public void initialize() {
        monthChoiceBox1.setItems(mjeseci);
        monthChoiceBox2.setItems(mjeseci);
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 31, 1);
        daySpinner1.setValueFactory(valueFactory);
        daySpinner2.setValueFactory(valueFactory);
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
        } else {
            monthChoiceBox1.getSelectionModel().selectFirst();
            monthChoiceBox2.getSelectionModel().selectFirst();
        }
    }

    public void addDoctorACtion (ActionEvent actionEvent) {
        if (doctor == null) doctor= new Doctor();
        doctor.setFirstName(nameFld.getText());
        doctor.setLastName(lastNameFld.getText());
        doctor.setJMBG(JMBGFld.getText());
        doctor.setBirthPlace(POBFld.getText());
        doctor.setAddress(addressFld.getText());
        doctor.setEmail(emailFld.getText());
        doctor.setBirthDate(LocalDate.of(Integer.parseInt(yearFld1.getText()),mjeseci.indexOf(monthChoiceBox1.getValue())+1, Integer.parseInt(daySpinner1.getValue().toString())));
        doctor.setEmploymentDate(LocalDate.of(Integer.parseInt(yearFld2.getText()),mjeseci.indexOf(monthChoiceBox2.getValue())+1, Integer.parseInt(daySpinner2.getValue().toString())));
        doctor.setSpecialization(specialtyFld.getText());
        Stage stage = (Stage) nameFld.getScene().getWindow();
        stage.close();
    }
    public void closeAction (ActionEvent actionEvent) {
        Stage stage = (Stage) nameFld.getScene().getWindow();
        stage.close();
    }
}
