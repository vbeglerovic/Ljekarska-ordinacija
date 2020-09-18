package ba.unsa.etf.rpr.projekat;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;


public class RegisterController implements ControllerInterface {

    private DAO dao;
    private Office office;

    public TextField fldName;
    public TextField fldAddress;
    public TextField fldUsername;
    public PasswordField fldPassword;
    public PasswordField repeatPasswordFld;
    public Button closeButton;
    private boolean edit;
    public Label informationLabel;

    public RegisterController(Office office, boolean edit) {
        this.office=office;
        dao=DAO.getInstance();
        this.edit=edit;
    }

    @FXML
    public void initialize() {
        informationLabel.setVisible(false);
        if (office!=null) {
            fldName.setText(office.getName());
            fldAddress.setText(office.getAddress());
            fldUsername.setText(office.getUsername());
            fldPassword.setText(office.getPassword());
            repeatPasswordFld.setText(office.getPassword());
        }
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
        fldUsername.textProperty().addListener((obs, oldValue, newValue)->{
            informationLabel.setVisible(false);
        });
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void openMainStage() {
        Stage stage = (Stage) informationLabel.getScene().getWindow();
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

    private void openLogInStage() {
        Stage stage=(Stage) informationLabel.getScene().getWindow();
        Parent root=null;
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("Translation");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/logIn.fxml"),bundle);
            MainController ctrl = new MainController();
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

    public void closeAction (ActionEvent actionEvent) {
        if (edit) openMainStage();
        else openLogInStage();
    }

    public void registerAction (ActionEvent actionEvent) {
        if (fldName.getText().isEmpty() || fldAddress.getText().isEmpty() || fldUsername.getText().isEmpty() || fldPassword.getText().isEmpty() || repeatPasswordFld.getText().isEmpty()) {
            String message;
            if (Locale.getDefault().equals(new Locale("bs","BA")))
                message = "Morate unijeti sve podatke!";
            else {
                message = "You have to enter all data!";
            }
            showAlert(message);
            return;
        }
        if (!repeatPasswordFld.getText().equals(fldPassword.getText())) return;
        if (office==null) {
            Office office = new Office(0, fldName.getText(), fldAddress.getText(), fldUsername.getText(), fldPassword.getText());
            try {
                dao.addOffice(office);
                openLogInStage();
            } catch (OfficeWithThisUsernameAlreadyExists officeWithThisUsernameAlreadyExists) {
                informationLabel.setVisible(true);
                return;
            }
        } else {
            office.setName(fldName.getText());
            office.setAddress(fldAddress.getText());
            office.setUsername(fldUsername.getText());
            office.setPassword(fldPassword.getText());
            try {
                dao.editOffice(office);
                openMainStage();
            } catch (OfficeWithThisUsernameAlreadyExists officeWithThisUsernameAlreadyExists) {
                informationLabel.setVisible(true);
                return;
            }
        }
    }

}
