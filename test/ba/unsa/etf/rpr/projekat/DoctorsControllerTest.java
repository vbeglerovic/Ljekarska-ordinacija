package ba.unsa.etf.rpr.projekat;

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

@ExtendWith(ApplicationExtension.class)
class DoctorsControllerTest {
    DoctorsController ctrl;
    DAO dao = DAO.getInstance();
    Office office=dao.getOfficeWithUsername("username2");

    @Start
    public void start (Stage stage) throws Exception {
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/doctors.fxml"),bundle);
        ctrl = new DoctorsController(office);
        loader.setController(ctrl);
        Parent root = loader.load();
        stage.setTitle("Doctors");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();
        stage.toFront();
    }

    @BeforeEach
    public void resetujBazu() throws SQLException {
        dao.resetBaseToDefault();
    }

    @Test
    public void testRemoveDoctor(FxRobot robot) {
        robot.clickOn("Samira");
        robot.clickOn("#btnRemove");
        robot.lookup(".dialog-pane").tryQuery().isPresent();
        DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        robot.clickOn(okButton);
        TableView tableView = robot.lookup("#tableViewDoctors").queryAs(TableView.class);
        assertEquals(0, tableView.getItems().size());
    }

  @Test public void test1AddDoctor(FxRobot robot) {
        robot.clickOn("#addDoctorBtn");
        robot.lookup("#fieldNaziv").tryQuery().isPresent();
        robot.lookup("#nameFld").tryQuery().isPresent();
        robot.clickOn("#nameFld");
        robot.write("Samira");
        robot.clickOn("#lastNameFld");
        robot.write("Beglerovic");
        robot.clickOn("#JMBGFld");
        robot.write("987654321");
        robot.clickOn("#yearFld1");
        robot.write("1999");
        robot.clickOn("#POBFld");
        robot.write("Sarajevo");
        robot.clickOn("#addressFld");
        robot.write("Donji Hotonj 21");
        robot.clickOn("#emailFld");
        robot.write("beglerovicvildana@gmail.com");
        robot.clickOn("#yearFld2");
        robot.write("2020");
        robot.clickOn("#specialtyFld");
        robot.write("Hirurg");
        robot.clickOn("#addButton");
        TableView tableView = robot.lookup("#tableViewDoctors").queryAs(TableView.class);
        assertEquals(2, tableView.getItems().size());
    }
}