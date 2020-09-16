package ba.unsa.etf.rpr.projekat;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.sql.SQLException;
import java.util.ResourceBundle;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(ApplicationExtension.class)
class RegisterControllerTest {
    Stage theStage;
    RegisterController ctrl;
    DAO dao = DAO.getInstance();

    @Start
    public void start (Stage stage) throws Exception {
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/register.fxml"),bundle);
        ctrl = new RegisterController(null,false);
        loader.setController(ctrl);
        Parent root = loader.load();
        stage.setTitle("Register");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();
        stage.toFront();
        theStage = stage;
    }

    @BeforeEach
    public void resetDatabase() throws SQLException {
        dao.resetBaseToDefault();
    }


    @Test
    public void passwordRepeat(FxRobot robot) {
        robot.lookup("#registerBtn").tryQuery().isPresent();

        robot.clickOn("#fldName");
        robot.write("Office3");
        robot.clickOn("#fldAddress");
        robot.write("Address3");
        robot.clickOn("#fldUsername");
        robot.write("username3");
        robot.clickOn("#fldPassword");
        robot.write("password3");
        robot.clickOn("#repeatPasswordFld");
        robot.write("password4");
        robot.clickOn("#registerBtn");
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
        assertTrue(robot.lookup("#registerBtn").tryQuery().isPresent());

        robot.clickOn("#repeatPasswordFld");
        robot.press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        robot.write("password3");

        robot.clickOn("#registerBtn");
        assertTrue(robot.lookup("#btnLogIn").tryQuery().isPresent());
    }

    @Test
    public void invalidUsername(FxRobot robot) {
        robot.lookup("#registerBtn").tryQuery().isPresent();

        robot.clickOn("#fldName");
        robot.write("Office3");
        robot.clickOn("#fldAddress");
        robot.write("Address3");
        robot.clickOn("#fldUsername");
        robot.write("username2");
        robot.clickOn("#fldPassword");
        robot.write("password3");
        robot.clickOn("#repeatPasswordFld");
        robot.write("password3");
        robot.clickOn("#registerBtn");
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
        assertTrue(robot.lookup("#registerBtn").tryQuery().isPresent());
    }


}