package ba.unsa.etf.rpr;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class OfficeController {
    private DAO dao;
    private Office office;

    public Button btnSignOut;
    public Label labelDate;

    public OfficeController(Office office) {
        dao=DAO.getInstance();
        this.office=office;
    }

    @FXML
    public void initialize() {
        PrintDate pd=new PrintDate();
        try {
            labelDate.setText(pd.print());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void makeAppointmentAction (ActionEvent actionEvent) {
        Stage stage=(Stage) btnSignOut.getScene().getWindow();
        Parent root = null;
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("Translation");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/appointment.fxml"),bundle);
            AppointmentController appointmentController = new AppointmentController(null, office,false);
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
    public void appointmentsAction (ActionEvent actionEvent) {
        Stage stage=(Stage) btnSignOut.getScene().getWindow();
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
        stage.setTitle("Appointments");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.setResizable(false);
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
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
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
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
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
            stage.setTitle("Log in");
            stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void  profileAction(ActionEvent actionEvent) {
        Stage stage = (Stage) btnSignOut.getScene().getWindow();
        Parent root = null;
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/register.fxml"),bundle);
        RegisterController registerController = new RegisterController(office,true);
        loader.setController(registerController);
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Register");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();
    }

}
