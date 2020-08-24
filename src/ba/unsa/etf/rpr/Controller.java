package ba.unsa.etf.rpr;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;


public class Controller {
    private DAO dao;
    public TextField fldUsername;
    public PasswordField fldPassword;

    public Controller() {
        dao = DAO.getInstance();
    }

    public void registerAction(ActionEvent actionEvent) {
        Stage stage = (Stage) fldUsername.getScene().getWindow();
        Parent root = null;
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/register.fxml"),bundle);
        RegisterController registerController = new RegisterController();
        loader.setController(registerController);
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Register");
        stage.setScene(new Scene(root, 600, 400));
        stage.setResizable(false);
        stage.show();

    }

    public void signInAction(ActionEvent actionEvent) {
        Office office= dao.getOfficeWithUsername(fldUsername.getText());
        if (office == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Pogresan username!");
            alert.showAndWait();
        }
        else if (!dao.checkIfOfficeExist(office, fldPassword.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Pogresan password!");
            alert.showAndWait();
        } else {
            Stage stage = (Stage) fldUsername.getScene().getWindow();
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
            stage.setResizable(true);
            stage.show();
        }
    }

    public void bosnianAction (ActionEvent actionEvent) {
        Locale.setDefault(new Locale("bs","BA"));
        open();
    }

    public void englishAction (ActionEvent actionEvent) {
        Locale.setDefault(new Locale("en", "US"));
        open();
    }
    private void open()  {
        Stage stage = (Stage) fldUsername.getScene().getWindow();
        Controller ctrl = new Controller();
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/logIn.fxml"),bundle);
        loader.setController(ctrl);
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Welcome");
        stage.setScene(new Scene(root, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE));
        stage.show();
    }
}
