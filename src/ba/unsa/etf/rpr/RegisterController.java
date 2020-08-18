package ba.unsa.etf.rpr;


import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


public class RegisterController {
    public TextField fldName;
    public TextField fldAddress;
    public TextField fldUsername;
    public PasswordField fldPassword;
    public PasswordField repeatPasswordFld;

    private DAO dao;

    public RegisterController() {
        dao=DAO.getInstance();
    }

    public void closeAction (ActionEvent actionEvent) {
        Stage stage=(Stage) fldName.getScene().getWindow();
        Parent root=null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/logIn.fxml"));
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
           alert.setContentText("Username "+fldUsername.getText()+" vec postoji, koristite neki drugi!");
           alert.showAndWait();
       } else {
           dao.addOffice(office);
           closeAction(null);
       }
    }


}
