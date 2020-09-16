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


public class DoctorsController implements ControllerInterface {

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
    public Button closeButton;
    public TextField searchFld;
    public TableView<Doctor> tableViewDoctors;

    public DoctorsController(Office office) {
        dao=DAO.getInstance();
        this.office=office;
        doctorsList= FXCollections.observableList(dao.doctors(office.getId()));
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        tableViewDoctors.setItems(doctorsList);
        colDoctorName.setCellValueFactory(new PropertyValueFactory("firstName"));
        colDoctorLastName.setCellValueFactory(new PropertyValueFactory("lastName"));
        colDoctorJMBG.setCellValueFactory(new PropertyValueFactory("identityNumber"));
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
                } else if (doctor.getIdentityNumber().toLowerCase().indexOf(lowerCaseFilter)!=-1) {
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

    public void addDoctorAction (ActionEvent actionEvent) {
        Stage stage=(Stage) closeButton.getScene().getWindow();
        Parent root = null;
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("Translation");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/doctor.fxml"),bundle);
            DoctorController doctorController = new DoctorController(null,office,false);
            loader.setController(doctorController);
            root = loader.load();
            stage.setTitle("Doctor");
            stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editDoctorAction (ActionEvent actionEvent) {
        Doctor d = tableViewDoctors.getSelectionModel().getSelectedItem();
        if (d == null) {
            if (Locale.getDefault().equals(new Locale("bs","BA")))
                showAlert("Izaberite doktora čije podatke želite izmijeniti!");
            else
                showAlert("Select doctor whose data you want to change!");
            return;
        }
        Stage stage=(Stage) closeButton.getScene().getWindow();
        Parent root = null;
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("Translation");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/doctor.fxml"),bundle);
            DoctorController doctorController = new DoctorController(d,office, true);
            loader.setController(doctorController);
            root = loader.load();
            stage.setTitle("Doctor");
            stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            stage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteDoctorAction (ActionEvent actionEvent) {
        Doctor doctor = tableViewDoctors.getSelectionModel().getSelectedItem();
        if (doctor == null) {
            if (Locale.getDefault().equals(new Locale("bs","BA")))
            showAlert("Izaberite doktora kojeg želite ukloniti!");
            else
                showAlert("Select doctor you want to remove!");
            return;
        }
        String message;
        if (Locale.getDefault().equals(new Locale("bs","BA")))
            message="Da li ste sigurni da želite obrisati doktora "+ doctor.getFirstName() + " " + doctor.getLastName()+"?";
        else message="Are you sure you want to remove doctor " + doctor.getFirstName() + " " + doctor.getLastName()+"?";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            dao.deleteDoctor(doctor.getId());
            doctorsList.setAll(dao.doctors(office.getId()));
        }
    }

    public void printReportAction (ActionEvent actionEvent) {
        try {
            DoctorsPrint doctorsPrint =new DoctorsPrint(office);
            Thread thread=new Thread(doctorsPrint);
            thread.start();
            new PrintReport().showReport(DAO.getConn(),getClass().getResource("/reports/doctorsReport.jrxml").getFile(), office.getId());
        } catch (JRException e1) {
            e1.printStackTrace();
        }
    }
}

