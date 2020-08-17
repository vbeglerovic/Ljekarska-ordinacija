package ba.unsa.etf.rpr;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;

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
        stage.close();
    }
}
