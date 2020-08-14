package ba.unsa.etf.rpr;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class PatientsController {

    private DAO dao;
    private ObservableList<Patient> patientsList;

    public TableColumn<Patient, String> colPatientName;
    public TableColumn<Patient, String> colPatientLastName;
    public TableColumn<Patient,String> colPatientJMBG;
    public TableColumn<Patient,String> colPatientGender;
    public TableColumn<Patient,String> colPatientDOB;
    public TableColumn<Patient,String> colPatientPOB;
    public TableColumn<Patient,String> colPatientAddress;
    public TableColumn<Patient,Status> colPatientStatus;
    public TableColumn<Patient,String> colPatientEmail;

    public TextField searchFld;
    public TableView<Patient> tableViewPatients;

    public PatientsController() {
        dao=DAO.getInstance();
        patientsList= FXCollections.observableArrayList(dao.patients());
    }

    @FXML
    public void initialize() {
        tableViewPatients.setItems(patientsList);
        colPatientName.setCellValueFactory(new PropertyValueFactory("firstName"));
        colPatientLastName.setCellValueFactory(new PropertyValueFactory("lastName"));
        colPatientJMBG.setCellValueFactory(new PropertyValueFactory("JMBG"));
        colPatientGender.setCellValueFactory(new PropertyValueFactory("gender"));
        colPatientDOB.setCellValueFactory(new PropertyValueFactory("birthDate"));
        colPatientPOB.setCellValueFactory(new PropertyValueFactory("birthPlace"));
        colPatientAddress.setCellValueFactory(new PropertyValueFactory("address"));
        colPatientStatus.setCellValueFactory(new PropertyValueFactory("status"));
        colPatientEmail.setCellValueFactory(new PropertyValueFactory("email"));
    }

    public void closeAction (ActionEvent actionEvent) {
        Stage stage = (Stage) searchFld.getScene().getWindow();
        stage.close();
    }

    public void addPatientAction (ActionEvent actionEvent) {
        Stage stage = new Stage();
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/formularPacijent.fxml"));
            PatientController patientController = new PatientController(null);
            loader.setController(patientController);
            root = loader.load();
            stage.setTitle("Patient");
            stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            stage.setResizable(true);
            stage.show();

            stage.setOnHiding( event -> {
                Patient patient = patientController.getPatient();
                if (patient != null) {
                    dao.addPatient(patient);
                    patientsList.setAll(dao.patients());
                }
            } );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

