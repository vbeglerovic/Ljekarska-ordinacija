package ba.unsa.etf.rpr.projekat;


import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class AppointmentsController implements ControllerInterface{

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
    public TextField d1Fld;
    public TextField d2Fld;
    public TextField patientFld;
    public TextField doctorFld;
    public TableView<Appointment> tableViewAppointments;
    public Button closeButton;

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

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
        colAppointmentType.setCellValueFactory(data->new SimpleObjectProperty<>(data.getValue().firstAppointment()));
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

    public void deleteAppointmentAction (ActionEvent actionEvent) {
        Appointment appointment = tableViewAppointments.getSelectionModel().getSelectedItem();
        if (appointment == null) {
            String message;
            if (Locale.getDefault().equals(new Locale("bs", "BA")))
                message = "Izaberite pregled koji želite otkazati!";
            else
                message = "Select appointment that you want to remove!";
            showAlert(message);
            return;
        }
        LocalDateTime lt=LocalDateTime.of(appointment.getDate(), appointment.getTime());
        if (lt.isBefore(LocalDateTime.now()) && (appointment.getRecommendation()!=null || appointment.getDiagnosis()!=null || appointment.getAnamnesis()!=null)) {
            if (Locale.getDefault().equals(new Locale("bs", "BA")))
                showAlert("Ne možete obrisati preglede koji su obavljeni!");
            else
                showAlert("You can not delete appointment from past!");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        String message;
        if (Locale.getDefault().equals(new Locale("bs","BA")))
            message = "Da li ste sigurni da želite otkazati pregled?";
        else
            message = "Are you sure you want to remove appointment?";
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            dao.deleteAppointment(appointment.getId());
            appointmentsList.setAll(dao.appointments(office.getId()));
        }
    }

    public void editAppointmentAction (ActionEvent actionEvent) {
        Appointment a = tableViewAppointments.getSelectionModel().getSelectedItem();
        if (a == null) {
            String message;
            if (Locale.getDefault().equals(new Locale("bs","BA")))
                message = "Izaberite pregled čije podatke želite izmijeniti!";
            else
                message = "Select appointment that you want to edit!";
            showAlert(message);
            return;
        }
        if (LocalDateTime.of(a.getDate(), a.getTime()).isBefore(LocalDateTime.now())){
            String message;
            if (Locale.getDefault().equals(new Locale("bs","BA")))
                message = "Ne možete mijenjati podatke za preglede koji su već obavljeni!";
            else
                message = "You can not change data for appointments from past!";
            showAlert(message);
            return;
        }
        Stage stage=(Stage) closeButton.getScene().getWindow();
        Parent root = null;
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("Translation");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/appointment.fxml"),bundle);
            AppointmentController appointmentController = new AppointmentController(a, office,true);
            loader.setController(appointmentController);
            root = loader.load();
            stage.setTitle("Appointment");
            stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Appointment> filterAppointments(Predicate<Appointment> predicate) {
      return appointments.stream().filter(predicate).collect(Collectors.toList());
    }

    public List<Appointment> afterDate(LocalDate localDate) {
        return filterAppointments((Appointment a) -> { return a.getDate().isAfter(localDate) || a.getDate().equals(localDate);});
    }

    public List<Appointment> beforeDate(LocalDate localDate) {
        return filterAppointments((Appointment a) -> { return a.getDate().isBefore(localDate) || a.getDate().equals(localDate);});
    }

    public List<Appointment> appointmentsOfPatient (String patient) {
        return filterAppointments((Appointment a) -> { return a.getPatient().toString().equals(patient);});
    }

    public List<Appointment> appointmentsOfDoctor (String doctor) {
        return filterAppointments((Appointment a) -> { return a.getDoctor().toString().equals(doctor);});
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
            if (Locale.getDefault().equals(new Locale("bs","BA")))
                showAlert("Neispravan format datuma!\n Ispravan: yyyy-HH-mm");
            else
                showAlert("Invalid date format! \n Correct: yyyy-HH-mm");
            return;
        }
        tableViewAppointments.setItems(FXCollections.observableList(appointments));
    }

    public void addReportAction (ActionEvent actionEvent) {
        Appointment a = tableViewAppointments.getSelectionModel().getSelectedItem();
        if (a == null) {
            String message;
            if (Locale.getDefault().equals(new Locale("bs","BA")))
                message = "Izaberite pregled kojem želite dodati izvještaj!";
            else
                message = "Select appointment you want to add report!";
            showAlert(message);
            return;
        }
        if (a.getRecommendation()!=null || a.getDiagnosis()!=null || a.getRecommendation()!=null) {
            String message;
            if (Locale.getDefault().equals(new Locale("bs","BA")))
                message = "Izvještaj je već dodan!";
            else
                message = "Report has already been added!";
            showAlert(message);
            return;
        }
        LocalDateTime lt=LocalDateTime.of(a.getDate(),a.getTime());
        if (lt.isAfter(LocalDateTime.now())) {
            String message;
            if (Locale.getDefault().equals(new Locale("bs","BA")))
                message = "Ne možete dodati izvještaj za pregled koji nije obavljen!";
            else
                message = "You can not add report for future appointments!";
            showAlert(message);
            return;
        }
        Stage stage=(Stage) closeButton.getScene().getWindow();
        Parent root = null;
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("Translation");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/report.fxml"),bundle);
            ReportController reportController = new ReportController(office,a);
            loader.setController(reportController);
            root = loader.load();
            stage.setTitle("Report");
            stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void viewReportAction(ActionEvent actionEvent) {
        Appointment a=tableViewAppointments.getSelectionModel().getSelectedItem();
        if (a==null) {
            String message;
            if (Locale.getDefault().equals(new Locale("bs","BA")))
                message = "Izaberite pregled čiji izvještaj želite vidjeti!";
            else
                message = "Select appointment whose report you want to view!";
            showAlert(message);
            return;
        }
        if (a.getRecommendation()==null && a.getDiagnosis()==null && a.getAnamnesis()==null) {
            String message;
            if (Locale.getDefault().equals(new Locale("bs","BA")))
                message = "Izvještaj nije dodan!";
            else
                message = "Report has not been added!";
            showAlert(message);
            return;
        }
        if (a!=null) {
            try {
                new PrintReportForAppointment().showReport(DAO.getConn(), a.getId(), a.getDoctor().toString(), a.getPatient().getId());
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }
}
