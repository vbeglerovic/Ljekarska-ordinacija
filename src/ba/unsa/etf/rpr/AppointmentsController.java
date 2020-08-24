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
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
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
        appointments=new ArrayList<>();
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
    }

    public void closeAction (ActionEvent actionEvent) {
        Stage stage = (Stage) patientFld.getScene().getWindow();
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
            ResourceBundle bundle = ResourceBundle.getBundle("Translation");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/appointment.fxml"),bundle);
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
    public List<Appointment> appointmentsOfPatient (String patient) {
        return filterAppointment((Appointment a) -> { return a.getPatient().toString().equals(patient);});
    }
    public List<Appointment> appointmentsOfDoctor (String doctor) {
        return filterAppointment((Appointment a) -> { return a.getDoctor().toString().equals(doctor);});
    }

public void search (ActionEvent actionEvent) {
    appointments=dao.appointments(office.getId());
    if (!doctorFld.getText().isEmpty())  appointments=appointmentsOfDoctor(doctorFld.getText());
    if (!patientFld.getText().isEmpty()) appointments=appointmentsOfPatient(patientFld.getText());
    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            if (!d1Fld.getText().isEmpty()) {
                LocalDate d1 = LocalDate.parse(d1Fld.getText(), df);
                appointments = afterDate(d1);
            }
            if (!d2Fld.getText().isEmpty()) {
                LocalDate d2 = LocalDate.parse(d2Fld.getText(), df);
                appointments = beforeDate(d2);
            }
        } catch (DateTimeParseException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Neispravan format datuma! \n Ispravan format: yyyy-HH-mm");
            alert.showAndWait();
        }
    tableViewAppointments.setItems(FXCollections.observableList(appointments));

}
    public void addReportAction (ActionEvent actionEvent) {
        Appointment a=tableViewAppointments.getSelectionModel().getSelectedItem();
        if (a==null) return;
        Stage stage = new Stage();
        Parent root = null;
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("Translation");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/report.fxml"),bundle);
            ReportController reportController = new ReportController(office,a);
            loader.setController(reportController);
            root = loader.load();
            stage.setTitle("Report");
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
            stage.setResizable(true);
            stage.show();

            /*stage.setOnHiding(event -> {
                Patient patient = patientController.getPatient();
                if (patient != null) {
                    dao.addPatient(patient,office.getId());
                    patients=FXCollections.observableArrayList(dao.patients(office.getId()));
                    patientsChoiceBox.setItems(patients);

                }
            });*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
