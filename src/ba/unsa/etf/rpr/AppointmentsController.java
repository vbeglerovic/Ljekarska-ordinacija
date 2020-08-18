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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public class AppointmentsController {
    private DAO dao;
    private ObservableList<Appointment> appointmentsList;
    private Office office;

    public TableColumn<Appointment, LocalDate> colAppointmentDate;
    public TableColumn<Appointment, LocalTime> colAppointmentTime;
    public TableColumn<Appointment,Patient> colAppointmentPatient;
    public TableColumn<Appointment,Doctor> colAppointmentDoctor;
    public TableColumn<Appointment,String> colAppointmentType;
    public TableColumn<Appointment,String> colAppointmentReport;

    public TextField searchFld;
    public TableView<Appointment> tableViewAppointments;

    public AppointmentsController(Office office) {
        dao=DAO.getInstance();
        this.office=office;
        appointmentsList= FXCollections.observableArrayList(dao.appointments(office.getId()));
    }

    @FXML
    public void initialize() {
        tableViewAppointments.setItems(appointmentsList);
        colAppointmentDate.setCellValueFactory(new PropertyValueFactory("date"));
        colAppointmentTime.setCellValueFactory(new PropertyValueFactory("time"));
        colAppointmentPatient.setCellValueFactory(new PropertyValueFactory("patient"));
        colAppointmentDoctor.setCellValueFactory(new PropertyValueFactory("doctor"));
        colAppointmentType.setCellValueFactory(new PropertyValueFactory("type"));
        colAppointmentReport.setCellValueFactory(new PropertyValueFactory("report"));

        FilteredList<Appointment> filteredData=new FilteredList<>(appointmentsList, b->true);
        searchFld.textProperty().addListener((observable,oldValue, newValue)->{
            filteredData.setPredicate(appointment -> {
                if (newValue==null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter=newValue.toLowerCase();
                if (appointment.getDate().toString().toLowerCase().indexOf(lowerCaseFilter)!=-1) {
                    return  true;
                } else if (appointment.getTime().toString().toLowerCase().indexOf(lowerCaseFilter)!=-1) {
                    return true;
               } else if (appointment.getPatient().toString().toLowerCase().indexOf(lowerCaseFilter)!=-1) {
                  return true;
                } else if (appointment.getDoctor().toString().toLowerCase().indexOf(lowerCaseFilter)!=-1) {
                    return true;
                }else
                    return false;
            });
        });
        SortedList<Appointment> sortedData=new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableViewAppointments.comparatorProperty());
        tableViewAppointments.setItems(sortedData);
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
        stage.setResizable(false);
        stage.show();
    }
    public void deleteAppointmentAction (ActionEvent actionEvent) {
        Appointment appointment = tableViewAppointments.getSelectionModel().getSelectedItem();
        if (appointment == null) return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potvrda otkazivanja");
        alert.setContentText("Da li ste sigurni da želite otkazati pregled? ");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            dao.deleteAppointment(appointment.getId());
            appointmentsList.setAll(dao.appointments(office.getId()));
        }
    }

    public void makeAppointmentAction (ActionEvent actionEvent) {
        Stage stage=new Stage();
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/appointment.fxml"));
            AppointmentController appointmentController = new AppointmentController(null, office);
            loader.setController(appointmentController);
            root = loader.load();
            stage.setTitle("Appointment");
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
            //stage.setResizable(true);
            stage.show();

            stage.setOnHiding( event -> {
                Appointment appointment = appointmentController.getAppointment();
                if (appointment != null) {
                    dao.addAppointment(appointment, office.getId());
                    //appointmentList.setAll(dao.appointments(office.getId()));
                }
            } );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editAppointmentAction (ActionEvent actionEvent) {
    }

}
