package ba.unsa.etf.rpr;

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
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class AppointmentControllerTest {
    Stage theStage;
    AppointmentController ctrl;
    DAO dao = DAO.getInstance();
    Office office=dao.getOfficeWithUsername("username2");

    @Start
    public void start (Stage stage) throws Exception {
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/appointment.fxml"),bundle);
        ctrl = new AppointmentController(null,office);
        loader.setController(ctrl);
        Parent root = loader.load();
        stage.setTitle("Appointment");
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
    public void enterAllData(FxRobot robot) {
        robot.lookup("#patientsChoiceBox").tryQuery().isPresent();
        robot.clickOn("#patientsChoiceBox");
        robot.clickOn("Amar Beglerović");
        robot.clickOn("#doctorsChoiceBox");
        robot.clickOn("Samira Beglerović");
        robot.clickOn("#datePicker");
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
        robot.clickOn("#addButton");
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
        assertTrue(robot.lookup("#addButton").tryQuery().isPresent());
    }
}