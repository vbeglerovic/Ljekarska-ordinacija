package ba.unsa.etf.rpr;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class OfficeController {
    private DAO dao;
    private Office office;
    public Button pregledBtn;

    public OfficeController(Office office) {
        dao=DAO.getInstance();
        this.office=office;
    }

    public void makeAppointmentAction (ActionEvent actionEvent) {
        Stage stage = new Stage();
        Parent root = null;
        //ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/zakaziPregled.fxml"));
        MakeAppointmentController makeAppointmentController = new MakeAppointmentController();
        loader.setController(makeAppointmentController);
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Zakazi pregled");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();
    }
    public void reviewAction (ActionEvent actionEvent) {
        Stage stage = new Stage();
        Parent root = null;
        //ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/pregledi.fxml"));
        AppointmentController preglediController = new AppointmentController();
        loader.setController(preglediController);
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Pregledi");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();
    }

    public void patientsAction (ActionEvent actionEvent) {
        Stage stage = new Stage();
        Parent root = null;
        //ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/patients.fxml"));
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
        Stage stage = new Stage();
        Parent root = null;
        //ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/doctors.fxml"));
        DoctorsController doctorsController = new DoctorsController();
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
        office=null;
        Stage stage = (Stage) pregledBtn.getScene().getWindow();
        stage.close();
    }


}
