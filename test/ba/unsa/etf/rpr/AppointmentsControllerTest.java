package ba.unsa.etf.rpr;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableView;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(ApplicationExtension.class)
class AppointmentsControllerTest {
    Stage theStage;
    AppointmentsController ctrl;
    DAO dao = DAO.getInstance();
    Office office=dao.getOfficeWithUsername("username2");

    @Start
    public void start (Stage stage) throws Exception {
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/appointments.fxml"),bundle);
        ctrl = new AppointmentsController(office);
        loader.setController(ctrl);
        Parent root = loader.load();
        stage.setTitle("Appointments");
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
    public void testDeleteAppointment (FxRobot robot) throws SQLException {
        robot.clickOn("2020-09-12");
        robot.clickOn("#btnDelete");

        robot.lookup(".dialog-pane").tryQuery().isPresent();

        DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        robot.clickOn(okButton);

        TableView tableView = robot.lookup("#tableViewAppointments").queryAs(TableView.class);
        assertEquals(0, tableView.getItems().size());

        DAO dao=DAO.getInstance();
        assertEquals(0, dao.appointments(office.getId()).size());
    }

    @Test
    public void testEditAppointmentFromPast (FxRobot robot) throws SQLException {
        robot.clickOn("2020-09-12");
        robot.clickOn("#editAppointment");

        DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        robot.clickOn(okButton);
        assertTrue(robot.lookup("#editAppointment").tryQuery().isPresent());

        Platform.runLater(() -> theStage.show());
    }
}