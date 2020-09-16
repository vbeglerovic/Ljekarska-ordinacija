package ba.unsa.etf.rpr.projekat;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;


import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class PatientsController implements ControllerInterface {

    private DAO dao;
    private ObservableList<Patient> patientsList;
    private Office office;

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
    public Button closeButton;

    public PatientsController(Office office) {
        dao=DAO.getInstance();
        patientsList= FXCollections.observableArrayList(dao.patients(office.getId()));
        this.office=office;
    }

    @FXML
    public void initialize() {
        tableViewPatients.setItems(patientsList);
        colPatientName.setCellValueFactory(new PropertyValueFactory("firstName"));
        colPatientLastName.setCellValueFactory(new PropertyValueFactory("lastName"));
        colPatientJMBG.setCellValueFactory(new PropertyValueFactory("identityNumber"));
        colPatientGender.setCellValueFactory(new PropertyValueFactory("gender"));
        colPatientDOB.setCellValueFactory(new PropertyValueFactory("birthDate"));
        colPatientPOB.setCellValueFactory(new PropertyValueFactory("birthPlace"));
        colPatientAddress.setCellValueFactory(new PropertyValueFactory("address"));
        colPatientStatus.setCellValueFactory(new PropertyValueFactory("status"));
        colPatientEmail.setCellValueFactory(new PropertyValueFactory("email"));
        FilteredList<Patient> filteredData=new FilteredList<>(patientsList, b->true);
        searchFld.textProperty().addListener((observable,oldValue, newValue)->{
            filteredData.setPredicate(patient -> {
                if (newValue==null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter=newValue.toLowerCase();
                if (patient.getFirstName().toLowerCase().indexOf(lowerCaseFilter)!=-1) {
                    return  true;
                } else if (patient.getLastName().toLowerCase().indexOf(lowerCaseFilter)!=-1) {
                    return true;
                } else if (patient.getIdentityNumber().toLowerCase().indexOf(lowerCaseFilter)!=-1) {
                    return true;
                } else if ((patient.getFirstName().toLowerCase()+" "+patient.getLastName()).toLowerCase().indexOf(lowerCaseFilter)!=-1) {
                    return true;
                }else
                        return false;
            });
        });
        SortedList<Patient> sortedData=new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableViewPatients.comparatorProperty());
        tableViewPatients.setItems(sortedData);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void closeAction (ActionEvent actionEvent) {
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
        stage.setResizable(false);
        stage.show();
    }

    public void addPatientAction (ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        Parent root = null;
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("Translation");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/patient.fxml"),bundle);
            PatientController patientController = new PatientController(null,office,false);
            loader.setController(patientController);
            root = loader.load();
            stage.setTitle("Patient");
            stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editPatientAction (ActionEvent actionEvent) {
        Patient p = tableViewPatients.getSelectionModel().getSelectedItem();
        if (p == null) {
            String message;
            if (Locale.getDefault().equals(new Locale("bs","BA")))
                message = "Odaberite pacijenta čije podatke želite izmijeniti!";
            else
                message = "Select patient whose data you want to edit!";
            showAlert(message);
            return;
        }
        Stage stage =(Stage) closeButton.getScene().getWindow();
        Parent root = null;
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("Translation");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/patient.fxml"),bundle);
            PatientController patientController = new PatientController(p,office,true);
            loader.setController(patientController);
            root = loader.load();
            stage.setTitle("Patient");
            stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            stage.setResizable(false);
            stage.show();

            stage.setOnHiding( event -> {
                Patient patient = patientController.getPatient();
                if (patient != null) {
                    dao.editPatient(patient);
                    patientsList.setAll(dao.patients(office.getId()));
                }
            } );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deletePatientAction (ActionEvent actionEvent) {
        Patient patient = tableViewPatients.getSelectionModel().getSelectedItem();
        if (patient == null){
            String message;
            if (Locale.getDefault().equals(new Locale("bs","BA")))
                message = "Odaberite pacijenta kojeg želite ukloniti!";
            else
                message = "Select patient you want to remove!";
            showAlert(message);
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        String message;
        if (Locale.getDefault().equals(new Locale("bs","BA")))
            message = "Da li ste sigurni da želite obrisati pacijenta " + patient.getFirstName() + " " + patient.getLastName()+"?";
        else
            message ="Are you sure you want to delete patient " + patient.getFirstName() + " " + patient.getLastName()+"?";
        alert.setTitle("Confirmation");
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            dao.deletePatient(patient.getId());
            patientsList.setAll(dao.patients(office.getId()));
        }
    }

    public void printReportAction(ActionEvent actionEvent) {
        try {
            PatientsPrint patientsPrint =new PatientsPrint(office);
            Thread thread=new Thread(patientsPrint);
            thread.start();
            new PrintReport().showReport(DAO.getConn(),getClass().getResource("/reports/patientsReport.jrxml").getFile(),office.getId());
        } catch (JRException e1) {
            e1.printStackTrace();
        }
    }
}

