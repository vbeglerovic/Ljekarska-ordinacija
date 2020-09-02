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


public class RegisterController {
    public TextField fldName;
    public TextField fldAddress;
    public TextField fldUsername;
    public PasswordField fldPassword;
    public PasswordField repeatPasswordFld;
    public Button registerBtn;

    private DAO dao;

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
        Stage stage=(Stage) fldName.getScene().getWindow();
        Parent root=null;
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("Translation");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/logIn.fxml"),bundle);
            Controller ctrl = new Controller();
            loader.setController(ctrl);
            root = loader.load();
            stage.setTitle("Pocetna");
            stage.setResizable(false);
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerAction (ActionEvent actionEvent) {
       Office office=new Office (0, fldName.getText(), fldAddress.getText(), fldUsername.getText(), fldPassword.getText());
       if (dao.getOfficeWithUsername(fldUsername.getText())!=null) {
           Alert alert = new Alert(Alert.AlertType.WARNING);
           alert.setTitle("Information Dialog");
           alert.setHeaderText(null);
           alert.setContentText("Username " + fldUsername.getText() + " vec postoji, koristite neki drugi!");
           alert.showAndWait();
       } else if (fldName.getText().isEmpty() || fldAddress.getText().isEmpty() || fldUsername.getText().isEmpty() || fldPassword.getText().isEmpty() || repeatPasswordFld.getText().isEmpty()) {
           Alert alert = new Alert(Alert.AlertType.WARNING);
           alert.setTitle("Information Dialog");
           alert.setHeaderText(null);
           alert.setContentText("Morate popuniti sve podatke!");
           alert.showAndWait();
       } else {
           dao.addOffice(office);
           closeAction(null);
       }
    }


}
