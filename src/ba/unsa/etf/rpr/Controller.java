package ba.unsa.etf.rpr;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;


public class Controller {
    private DAO dao;
    public TextField fldUsername;
    public PasswordField fldPassword;

    public Controller() {
        dao = DAO.getInstance();
    }

    public void registerAction(ActionEvent actionEvent) {
        Stage stage = new Stage();
        Parent root = null;
        //ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/register.fxml"));
        RegisterController registerController = new RegisterController();
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
        boolean ok = true;
        Office office= dao.getOfficeWithUsername(fldUsername.getText());
        if (office == null) ok = false;
        else if (!dao.checkIfOfficeExist(office, fldPassword.getText())) ok = false;


        if (ok) {
            Stage stage = new Stage();
            Parent root = null;
            //ResourceBundle bundle = ResourceBundle.getBundle("Translation");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/office.fxml"));
            //Warehouse user = dao.getWarehouseByUsername(fldUsername.getText());
            OfficeController officeController = new OfficeController(office);
            loader.setController(officeController);
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.setTitle("Office");
            stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            stage.setResizable(false);
            stage.show();
        }
    }
}
/*
        Stage stage = new Stage();
        Parent root = null;
        //ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/office.fxml"));
        //Warehouse user = dao.getWarehouseByUsername(fldUsername.getText());
        OfficeController officeController = new OfficeController();
        loader.setController(officeController);
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Office");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();

            //Stage stage1 = (Stage) fldUsername.getScene().getWindow();
            //stage1.hide();
            //kako da se vratim nazad
        }
        /*String username=fldUsername.getText();
        String password=fldPassword.getText();
       if (dao.checkIfOfficeExist(username, password)) {
           Stage stage = new Stage();
           Parent root = null;
           //ResourceBundle bundle = ResourceBundle.getBundle("Translation");
           FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/office.fxml"));
           OfficeController officeController = new OfficeController();
           loader.setController(officeController);
           try {
               root = loader.load();
           } catch (IOException e) {
               e.printStackTrace();
           }
           stage.setTitle("Office");
           stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
           stage.setResizable(false);
           stage.show();
       } else {
           Alert alert = new Alert(Alert.AlertType.WARNING);
           alert.setTitle("Information Dialog");
           alert.setHeaderText(null);
           alert.setContentText("Pogresni podaci");
           alert.showAndWait();
       }*/
