package ba.unsa.etf.rpr;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
   // private ObservableList<Appointment> appointmentList;
    public Button pregledBtn;

    public OfficeController(Office office) {
        dao=DAO.getInstance();
        this.office=office;
       //appointmentList= FXCollections.observableArrayList(dao.appointments(office.getId()));
    }

    public void makeAppointmentAction (ActionEvent actionEvent) {
        Stage stage=new Stage();
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/makeAppointment.fxml"));
            MakeAppointmentController makeAppointmentController = new MakeAppointmentController(null, office);
            loader.setController(makeAppointmentController);
            root = loader.load();
            stage.setTitle("Appointment");
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
            //stage.setResizable(true);
            stage.show();

            stage.setOnHiding( event -> {
                Appointment appointment = makeAppointmentController.getAppointment();
                if (appointment != null) {
                    dao.addAppointment(appointment, office.getId());
                    //appointmentList.setAll(dao.appointments(office.getId()));
                }
            } );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void reviewAction (ActionEvent actionEvent) {
        Stage stage=(Stage) pregledBtn.getScene().getWindow();
        //Stage stage=new Stage();
        Parent root = null;
        //ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/pregledi.fxml"));
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
        Stage stage=(Stage) pregledBtn.getScene().getWindow();
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
        stage.setScene(new Scene(root, 600, 400));
        stage.setResizable(false);
        stage.show();
    }

    public void doctorsAction (ActionEvent actionEvent) {
        Stage stage=(Stage) pregledBtn.getScene().getWindow();
        Parent root = null;
        //ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/doctors.fxml"));
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
        Stage stage=(Stage) pregledBtn.getScene().getWindow();
        Parent root=null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/pocetniEkran.fxml"));
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
