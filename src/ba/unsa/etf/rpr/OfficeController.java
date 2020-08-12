package ba.unsa.etf.rpr;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class OfficeController {
    private DAO dao;
    private Office office;

    public OfficeController(Office office) {
        dao=DAO.getInstance();
        this.office=office;
    }
    public OfficeController() {
        dao = DAO.getInstance();
    }

    public void makeAppointmentAction (ActionEvent actionEvent) {
        Stage stage = new Stage();
        Parent root = null;
        //ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/zakaziPregled.fxml"));
        ZakaziPregledController zakaziPregledController = new ZakaziPregledController();
        loader.setController(zakaziPregledController);
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
        PreglediController preglediController = new PreglediController();
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

}
