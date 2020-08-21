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
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class AppointmentsController {
    private DAO dao;
    private ObservableList<Appointment> appointmentsList;
    private Office office;
    private ObservableList<Patient> patientsList;
    private List<Appointment> appointments;

    public TableColumn<Appointment, LocalDate> colAppointmentDate;
    public TableColumn<Appointment, LocalTime> colAppointmentTime;
    public TableColumn<Appointment,Patient> colAppointmentPatient;
    public TableColumn<Appointment,Doctor> colAppointmentDoctor;
    public TableColumn<Appointment,String> colAppointmentType;
    public TableColumn<Appointment,String> colAppointmentReport;

    public TextField d1Fld;
    public TextField d2Fld;
    public TextField patientFld;
    public TextField doctorFld;

    public TextField searchFld;
    public TableView<Appointment> tableViewAppointments;

    public AppointmentsController(Office office) {
        dao=DAO.getInstance();
        this.office=office;
        appointmentsList= FXCollections.observableArrayList(dao.appointments(office.getId()));
        patientsList=FXCollections.observableArrayList(dao.patients(office.getId()));
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

        /*FilteredList<Appointment> filteredData=new FilteredList<>(appointmentsList, b->true);

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
        tableViewAppointments.setItems(sortedData);*/
        /*FilteredList<Appointment> filteredData=new FilteredList<>(tableViewAppointments.getItems(), b->true);
        patientFld.textProperty().addListener((observable,oldValue, newValue)->{
            filteredData.setPredicate(appointment -> {
                if (newValue==null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter=newValue.toLowerCase();
                if (appointment.getPatient().getFirstName().toLowerCase().indexOf(lowerCaseFilter)!=-1) {
                    return  true;
                } else if (appointment.getPatient().getLastName().toLowerCase().indexOf(lowerCaseFilter)!=-1) {
                    return true;
               } else if ((appointment.getPatient().getFirstName()+" "+appointment.getPatient().getLastName()).toLowerCase().indexOf(lowerCaseFilter)!=-1) {
                  return true;
                }else
                    return false;
            });
        });
        SortedList<Appointment> sortedData=new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableViewAppointments.comparatorProperty());
        tableViewAppointments.setItems(sortedData);

        FilteredList<Appointment> filteredData2=new FilteredList<>(tableViewAppointments.getItems(), b->true);
        doctorFld.textProperty().addListener((observable,oldValue, newValue)->{
            filteredData2.setPredicate(appointment -> {
                if (newValue==null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter=newValue.toLowerCase();
                if (appointment.getDoctor().getFirstName().toLowerCase().indexOf(lowerCaseFilter)!=-1) {
                    return  true;
                } else if (appointment.getDoctor().getLastName().toLowerCase().indexOf(lowerCaseFilter)!=-1) {
                    return true;
                } else if ((appointment.getDoctor().getFirstName()+" "+appointment.getDoctor().getLastName()).toLowerCase().indexOf(lowerCaseFilter)!=-1) {
                    return true;
                }else
                    return false;
            });
        });
        SortedList<Appointment> sortedData2=new SortedList<>(filteredData2);
        sortedData.comparatorProperty().bind(tableViewAppointments.comparatorProperty());
        tableViewAppointments.setItems(sortedData2);

        /*FilteredList<Appointment> filteredData3=new FilteredList<>(tableViewAppointments.getItems(), b->true);
        d1Fld.textProperty().addListener((observable,oldValue, newValue)->{
            filteredData2.setPredicate(appointment -> {
                if (newValue==null || newValue.isEmpty()) {
                    return true;
                }
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate d=LocalDate.parse(newValue,df);
                if (appointment.getDate().isAfter(d) || appointment.getDate().equals(d)) {
                    return  true;
                }else
                    return false;
            });
        });
        SortedList<Appointment> sortedData3=new SortedList<>(filteredData3);
        sortedData.comparatorProperty().bind(tableViewAppointments.comparatorProperty());
        tableViewAppointments.setItems(sortedData3);*/




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


    public void editAppointmentAction (ActionEvent actionEvent) {
        Appointment a = tableViewAppointments.getSelectionModel().getSelectedItem();
        if (a == null) return;
        Stage stage=new Stage();
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/appointment.fxml"));
            AppointmentController appointmentController = new AppointmentController(a, office);
            loader.setController(appointmentController);
            root = loader.load();
            stage.setTitle("Appointment");
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
            //stage.setResizable(true);
            stage.show();

            stage.setOnHiding( event -> {
                Appointment appointment = appointmentController.getAppointment();
                if (appointment != null) {
                    dao.editAppointment(appointment);
                    appointmentsList.setAll(dao.appointments(office.getId()));
                }
            } );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<Appointment> filterAppointment(Function<Appointment,Boolean> comp) {
        ArrayList<Appointment> result = new ArrayList<>();
            for (Appointment a : appointments) {
                if (comp.apply(a)) {
                    result.add(a);
                }
            }
        return result;
    }

    public List<Appointment> afterDate(LocalDate localDate) {
        return filterAppointment((Appointment a) -> { return a.getDate().isAfter(localDate) || a.getDate().equals(localDate);});
    }
    public List<Appointment> beforeDate(LocalDate localDate) {
        return filterAppointment((Appointment a) -> { return a.getDate().isBefore(localDate) || a.getDate().equals(localDate);});
    }
    public List<Appointment> afterAndBeforeDate(LocalDate localDate1, LocalDate localDate2) {
        return filterAppointment((Appointment a) -> { return (a.getDate().isBefore(localDate2) && a.getDate().isAfter(localDate1))|| a.getDate().equals(localDate2);});
    }
    public List<Appointment> appointmentsOfPatient (String patient) {
        return filterAppointment((Appointment a) -> { return a.getPatient().toString().equals(patient);});
    }
    public List<Appointment> appointmentsOfDoctor (String doctor) {
        return filterAppointment((Appointment a) -> { return a.getDoctor().toString().equals(doctor);});
    }

public void search (ActionEvent actionEvent) {
        boolean ok=true;
    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate d1=LocalDate.of(1999,1,1);
    LocalDate d2=LocalDate.of(1999,1,1);
    try {
            d1 = LocalDate.parse(d1Fld.getText(), df);
    } catch (DateTimeParseException e) {
        ok=false;
    }
    try {
        d2 = LocalDate.parse(d2Fld.getText(), df);
    } catch (DateTimeParseException e) {
        ok=false;
    }
    /*if (ok) {
        tableViewAppointments.setItems(FXCollections.observableArrayList(afterAndBeforeDate(d1,d2)));
    } else if (d1Fld.getText().equals(""))
    tableViewAppointments.setItems(FXCollections.observableArrayList(beforeDate(d2)));
    else if (d2Fld.getText().equals(""))
        tableViewAppointments.setItems(FXCollections.observableArrayList(afterDate(d1)));
    System.out.println("Ovdje sam");*/
            /*else if (d2Fld.getText()!=null)
                tableViewAppointments.setItems(FXCollections.observableArrayList(beforeDate(d2)));*/
            appointments=dao.appointments(office.getId());
            if (!patientFld.getText().isEmpty())
                 appointments=appointmentsOfPatient(patientFld.getText());
            if (!doctorFld.getText().isEmpty())
                appointments=appointmentsOfDoctor(doctorFld.getText());
            if (!d1Fld.getText().isEmpty())
                appointments=afterDate(d1);
            if (!d2Fld.getText().isEmpty())
                appointments=beforeDate(d2);
            tableViewAppointments.setItems(FXCollections.observableList(appointments));

}
    public void addReportAction (ActionEvent actionEvent) {

    }

}
