package ba.unsa.etf.rpr;


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


import java.io.IOException;
import java.util.Optional;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class PatientsController {

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
        colPatientJMBG.setCellValueFactory(new PropertyValueFactory("JMBG"));
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
                } else if (patient.getJMBG().toLowerCase().indexOf(lowerCaseFilter)!=-1) {
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

    public void closeAction (ActionEvent actionEvent) {
        Stage stage = (Stage) searchFld.getScene().getWindow();
        Parent root = null;
        //ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/office.fxml"));
        OfficeController officeController = new OfficeController(office);
        loader.setController(officeController);
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Office");
        stage.setScene(new Scene(root, 600, 400));
        //stage.setResizable(false);
        stage.show();
    }

    public void addPatientAction (ActionEvent actionEvent) {
        Stage stage = new Stage();
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/formularPacijent.fxml"));
            PatientController patientController = new PatientController(null,office);
            loader.setController(patientController);
            root = loader.load();
            stage.setTitle("Patient");
            stage.setScene(new Scene(root, 600, 400));
            //stage.setResizable(true);
            stage.show();

            stage.setOnHiding(event -> {
                Patient patient = patientController.getPatient();
                if (patient != null) {
                    dao.addPatient(patient,office.getId());
                    patientsList.setAll(dao.patients(office.getId()));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        public void editPatientAction (ActionEvent actionEvent) {
            Patient p = tableViewPatients.getSelectionModel().getSelectedItem();
            if (p == null) return;

            Stage stage = new Stage();
            Parent root = null;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/formularPacijent.fxml"));
                PatientController patientController = new PatientController(p,office);
                loader.setController(patientController);
                root = loader.load();
                stage.setTitle("Patient");
                stage.setScene(new Scene(root, 600, 400));
                //stage.setResizable(true);
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
            if (patient == null) return;
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potvrda brisanja");
            alert.setHeaderText("Brisanje pacijenta " + patient.getFirstName() + " " + patient.getLastName());
            alert.setContentText("Da li ste sigurni da želite obrisati pacijenta " + patient.getFirstName() + " " + patient.getLastName()+"?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                dao.deletePatient(patient.getJMBG());
                patientsList.setAll(dao.patients(office.getId()));
            }
        }
        public void searchPatientAction (ActionEvent actionEvent) {
         String [] p=searchFld.getText().split(" ");
            patientsList=FXCollections.observableArrayList(dao.searchPatients(office.getId(),p[0], p[1]));
            tableViewPatients.setItems(patientsList);
        }
}

