package ba.unsa.etf.rpr;

import javafx.event.ActionEvent;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;


public class DoctorsController {

    public TextField searchFld;
    private Office office;

    public DoctorsController() {

    }

    public void closeAction (ActionEvent actionEvent) {
        Stage stage = (Stage) searchFld.getScene().getWindow();
        stage.close();
    }

    public void addDoctorAction (ActionEvent actionEvent) {
        Stage stage = new Stage();
        Parent root = null;
        //ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/formularDoktor.fxml"));
        DoctorController doctorController = new DoctorController(office);
        loader.setController(doctorController);
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Doctor");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();
    }
}
