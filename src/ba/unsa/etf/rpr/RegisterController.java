package ba.unsa.etf.rpr;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;


public class RegisterController {

    private DAO dao;

    public TextField fldName;
    public TextField fldAddress;
    public TextField fldUsername;
    public PasswordField fldPassword;
    public PasswordField repeatPasswordFld;
    public Button closeButton;


    public RegisterController() {
        dao=DAO.getInstance();
    }

    @FXML
    public void initialize() {
        fldPassword.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.isEmpty() && repeatPasswordFld.getText().equals(newValue)) {
                fldPassword.getStyleClass().removeAll("notOk");
                fldPassword.getStyleClass().add("ok");
                repeatPasswordFld.getStyleClass().removeAll("notOk");
                repeatPasswordFld.getStyleClass().add("ok");
            } else {
                fldPassword.getStyleClass().removeAll("ok");
                fldPassword.getStyleClass().add("notOk");
                repeatPasswordFld.getStyleClass().removeAll("ok");
                repeatPasswordFld.getStyleClass().add("notOk");
            }
        });
        repeatPasswordFld.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.isEmpty() && newValue.equals(fldPassword.getText())) {
                fldPassword.getStyleClass().removeAll("notOk");
                fldPassword.getStyleClass().add("ok");
                repeatPasswordFld.getStyleClass().removeAll("notOk");
                repeatPasswordFld.getStyleClass().add("ok");

            } else {
                fldPassword.getStyleClass().removeAll("ok");
                fldPassword.getStyleClass().add("notOk");
                repeatPasswordFld.getStyleClass().removeAll("ok");
                repeatPasswordFld.getStyleClass().add("notOk");
            }
        });
    }

    public void closeAction (ActionEvent actionEvent) {
        Stage stage=(Stage) closeButton.getScene().getWindow();
        Parent root=null;
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("Translation");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/logIn.fxml"),bundle);
            Controller ctrl = new Controller();
            loader.setController(ctrl);
            root = loader.load();
            stage.setTitle("Log In");
            stage.setResizable(false);
            stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerAction (ActionEvent actionEvent) {
        if (fldName.getText().isEmpty() || fldAddress.getText().isEmpty() || fldUsername.getText().isEmpty() || fldPassword.getText().isEmpty() || repeatPasswordFld.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("You have to enter all data!");
            alert.showAndWait();
            return;
        }
        Office office=new Office (0, fldName.getText(), fldAddress.getText(), fldUsername.getText(), fldPassword.getText());
        try {
            dao.addOffice(office);
        } catch (OfficeWithThisUsernameAlreadyExist officeWithThisUsernameAlreadyExist) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText(officeWithThisUsernameAlreadyExist.getMessage());
            alert.showAndWait();
            return;
        }
        closeAction(null);
    }

}
