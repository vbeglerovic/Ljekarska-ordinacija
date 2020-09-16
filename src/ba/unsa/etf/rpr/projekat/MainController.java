package ba.unsa.etf.rpr.projekat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;


public class MainController {
    private DAO dao;

    public TextField fldUsername;
    public PasswordField fldPassword;
    public Label invalidUsernameLabel, usernameLabel, invalidPasswordLabel;

    public MainController() {
        dao = DAO.getInstance();
    }

    @FXML
    public void initialize() {
        invalidUsernameLabel.setVisible(false);
        usernameLabel.setVisible(false);
        invalidPasswordLabel.setVisible(false);
        fldUsername.textProperty().addListener((obs, oldValue, newValue)-> {
            invalidUsernameLabel.setVisible(false);
            usernameLabel.setVisible(false);
        });
        fldPassword.textProperty().addListener((obs, oldValue, newValue)-> {
            invalidPasswordLabel.setVisible(false);
        });
    }

    public void registerAction(ActionEvent actionEvent) {
        Stage stage = (Stage) fldUsername.getScene().getWindow();
        Parent root = null;
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/register.fxml"),bundle);
        RegisterController registerController = new RegisterController(null,false);
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

    public void signInAction(ActionEvent actionEvent) {
        if (fldUsername.getText().isEmpty() || fldPassword.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            if (Locale.getDefault().equals(new Locale("bs","BA")))
            alert.setContentText("Unesite korisničko ime i šifru!");
            else
                alert.setContentText("Enter username and password!");
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }
        Office office= dao.getOfficeWithUsername(fldUsername.getText());
        if (office == null) {
            invalidUsernameLabel.setVisible(true);
            usernameLabel.setText(fldUsername.getText()+"!");
            usernameLabel.setVisible(true);
            return;
        }
        else if (!office.getPassword().equals(fldPassword.getText())) {
            invalidPasswordLabel.setVisible(true);
            return;
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
            stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
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
        MainController ctrl = new MainController();
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/logIn.fxml"),bundle);
        loader.setController(ctrl);
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Log In");
        stage.setScene(new Scene(root, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE));
        stage.show();
    }
}
