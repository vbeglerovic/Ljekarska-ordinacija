package ba.unsa.etf.rpr;


import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


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
        Stage stage = (Stage) fldName.getScene().getWindow();
        stage.close();
    }

    public void registerAction (ActionEvent actionEvent) {
        Office office=new Office (1, fldName.getText(), fldAddress.getText(), fldUsername.getText(), fldPassword.getText());
        dao.addOffice(office);
        Stage stage=(Stage) fldAddress.getScene().getWindow();
        stage.close();

    }


}
