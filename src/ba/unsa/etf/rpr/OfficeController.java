package ba.unsa.etf.rpr;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

public class OfficeController {
    private DAO dao;
    private Office office;
   // private ObservableList<Appointment> appointmentList;
    public Button btnSignOut,patientBtn;

    public OfficeController(Office office) {
        dao=DAO.getInstance();
        this.office=office;
       //appointmentList= FXCollections.observableArrayList(dao.appointments(office.getId()));
    }

    @FXML
    public void initialize() {
    }
    public void makeAppointmentAction (ActionEvent actionEvent) {
        Stage stage=new Stage();
        Parent root = null;
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("Translation");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/appointment.fxml"),bundle);
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
                    try {
                        dao.addAppointment(appointment, office.getId());
                    } catch (ReservedAppointmentExcepction reservedAppointmentExcepction) {
                       //reservedAppointmentExcepction.printStackTrace();
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Dialog");
                        alert.setHeaderText("Appointment reserved!");
                        alert.setContentText("See free appointments!");

                        alert.showAndWait();
                        makeAppointmentAction(null);
                    }
                    //appointmentList.setAll(dao.appointments(office.getId()));
                }
            } );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void reviewAction (ActionEvent actionEvent) {
        Stage stage=(Stage) btnSignOut.getScene().getWindow();
        //Stage stage=new Stage();
        Parent root = null;
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/appointments.fxml"),bundle);
        AppointmentsController appointmentsController = new AppointmentsController(office);
        loader.setController(appointmentsController);
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Pregledi");
        stage.setScene(new Scene(root, 600, 400));
        //stage.setResizable(false);
        stage.show();
    }

    public void patientsAction (ActionEvent actionEvent) {
        Stage stage=(Stage) btnSignOut.getScene().getWindow();
        Parent root = null;
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/patients.fxml"),bundle);
        PatientsController patientsController = new PatientsController(office);
        loader.setController(patientsController);
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Patients");
        stage.setScene(new Scene(root, 600, 400));
        stage.setResizable(false);
        stage.show();
    }

    public void doctorsAction (ActionEvent actionEvent) {
        Stage stage=(Stage) btnSignOut.getScene().getWindow();
        Parent root = null;
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/doctors.fxml"),bundle);
        DoctorsController doctorsController = new DoctorsController(office);
        loader.setController(doctorsController);
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Doctors");
        stage.setScene(new Scene(root, 600, 400));
        stage.setResizable(false);
        stage.show();
    }

    public void signOutAction (ActionEvent actionEvent) {
        Stage stage=(Stage) btnSignOut.getScene().getWindow();
        Parent root=null;
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("Translation");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/logIn.fxml"),bundle);
            Controller ctrl = new Controller();
            loader.setController(ctrl);
            root = loader.load();
            stage.setTitle("Pocetna");
            stage.setResizable(false);
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
