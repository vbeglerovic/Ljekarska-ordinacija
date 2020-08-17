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


public class DoctorsController {

    private DAO dao;
    private ObservableList<Doctor> doctorsList;
    private Office office;

    public TableColumn<Doctor, String> colDoctorName;
    public TableColumn<Doctor, String> colDoctorLastName;
    public TableColumn<Doctor,String> colDoctorJMBG;
    public TableColumn<Doctor,String> colDoctorDOB;
    public TableColumn<Doctor,String> colDoctorPOB;
    public TableColumn<Doctor,String> colDoctorAddress;
    public TableColumn<Doctor,String> colDoctorEmail;
    public TableColumn<Doctor, String> colDoctorDOE;
    public TableColumn<Doctor,String> colDoctorSpecialty;

    public TextField searchFld;
    public TableView<Doctor> tableViewDoctors;

    public DoctorsController(Office office) {
        dao=DAO.getInstance();
        this.office=office;
        doctorsList= FXCollections.observableList(dao.doctors(office.getId()));
    }

    @FXML
    public void initialize() {
        tableViewDoctors.setItems(doctorsList);
        colDoctorName.setCellValueFactory(new PropertyValueFactory("firstName"));
        colDoctorLastName.setCellValueFactory(new PropertyValueFactory("lastName"));
        colDoctorJMBG.setCellValueFactory(new PropertyValueFactory("JMBG"));
        colDoctorDOB.setCellValueFactory(new PropertyValueFactory("birthDate"));
        colDoctorPOB.setCellValueFactory(new PropertyValueFactory("birthPlace"));
        colDoctorAddress.setCellValueFactory(new PropertyValueFactory("address"));
        colDoctorEmail.setCellValueFactory(new PropertyValueFactory("email"));
        colDoctorSpecialty.setCellValueFactory(new PropertyValueFactory("specialization"));
        colDoctorDOE.setCellValueFactory(new PropertyValueFactory("employmentDate"));
        FilteredList<Doctor> filteredData=new FilteredList<>(doctorsList, b->true);
        searchFld.textProperty().addListener((observable,oldValue, newValue)->{
            filteredData.setPredicate(doctor -> {
                if (newValue==null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter=newValue.toLowerCase();
                if (doctor.getFirstName().toLowerCase().indexOf(lowerCaseFilter)!=-1) {
                    return  true;
                } else if (doctor.getLastName().toLowerCase().indexOf(lowerCaseFilter)!=-1) {
                    return true;
                } else if (doctor.getJMBG().toLowerCase().indexOf(lowerCaseFilter)!=-1) {
                    return true;
                } else if ((doctor.getFirstName().toLowerCase()+" "+doctor.getLastName()).toLowerCase().indexOf(lowerCaseFilter)!=-1) {
                    return true;
                } else if (doctor.getSpecialization().toLowerCase().indexOf(lowerCaseFilter)!=-1) {
                    return true;
                }else
                    return false;
            });
        });
        SortedList<Doctor> sortedData=new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableViewDoctors.comparatorProperty());
        tableViewDoctors.setItems(sortedData);
    }

    public void closeAction (ActionEvent actionEvent) {
        Stage stage = (Stage) searchFld.getScene().getWindow();
        stage.close();
    }

    public void addDoctorAction (ActionEvent actionEvent) {
        Stage stage = new Stage();
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/formularDoktor.fxml"));
            DoctorController doctorController = new DoctorController(null,office);
            loader.setController(doctorController);
            root = loader.load();
            stage.setTitle("Doctor");
            stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            stage.setResizable(true);
            stage.show();

            stage.setOnHiding(event -> {
                Doctor doctor = doctorController.getDoctor();
                if (doctor != null) {
                    dao.addDoctor(doctor,office.getId());
                    doctorsList.setAll(dao.doctors(office.getId()));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editDoctorAction (ActionEvent actionEvent) {
        Doctor d = tableViewDoctors.getSelectionModel().getSelectedItem();
        if (d == null) return;

        Stage stage = new Stage();
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/formularDoktor.fxml"));
            DoctorController doctorController = new DoctorController(d,office);
            loader.setController(doctorController);
            root = loader.load();
            stage.setTitle("Doktor");
            stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            stage.setResizable(true);
            stage.show();

            stage.setOnHiding( event -> {
                Doctor doctor = doctorController.getDoctor();
                if (doctor!= null) {
                    dao.editDoctor(doctor);
                    doctorsList.setAll(dao.doctors(office.getId()));
                }
            } );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteDoctorAction (ActionEvent actionEvent) {
        Doctor doctor = tableViewDoctors.getSelectionModel().getSelectedItem();
        if (doctor == null) return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potvrda brisanja");
        alert.setHeaderText("Brisanje doktora " + doctor.getFirstName() + " " + doctor.getLastName());
        alert.setContentText("Da li ste sigurni da želite obrisati doktora " + doctor.getFirstName() + " " + doctor.getLastName()+"?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            dao.deleteDoctor(doctor.getJMBG());
            doctorsList.setAll(dao.doctors(office.getId()));
        }
    }
    public void searchPatientAction (ActionEvent actionEvent) {
        String [] p=searchFld.getText().split(" ");
        doctorsList=FXCollections.observableArrayList(dao.searchDoctors(office.getId(),p[0], p[1]));
        tableViewDoctors.setItems(doctorsList);
    }
}

