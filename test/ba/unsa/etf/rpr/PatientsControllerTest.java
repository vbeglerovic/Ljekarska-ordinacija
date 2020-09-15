package ba.unsa.etf.rpr;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(ApplicationExtension.class)
class PatientsControllerTest {
    Stage theStage;
    PatientsController ctrl;
    DAO dao = DAO.getInstance();
    Office office=dao.getOfficeWithUsername("username1");

    @Start
    public void start (Stage stage) throws Exception {
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/patients.fxml"),bundle);
        ctrl = new PatientsController(office);
        loader.setController(ctrl);
        Parent root = loader.load();
        stage.setTitle("Patients");
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
    public void testAddPatient(FxRobot robot) throws SQLException {
        robot.clickOn("#addButton");
        robot.lookup("#fieldNaziv").tryQuery().isPresent();

        robot.lookup("#nameFld").tryQuery().isPresent();
        robot.clickOn("#nameFld");
        robot.write("Vildana");
        robot.clickOn("#lastNameFld");
        robot.write("Beglerovic");
        robot.clickOn("#JMBGFld");
        robot.write("965321478");
        robot.clickOn("#femaleButton");
        robot.clickOn("#yearFld");
        robot.write("1999");
        robot.clickOn("#POBFld");
        robot.write("Sarajevo");
        robot.clickOn("#addressFld");
        robot.write("Donji Hotonj 21");
        robot.clickOn("#emailFld");
        robot.write("beglerovicvildana@gmail.com");
        robot.clickOn("#addButton");

        DAO dao = DAO.getInstance();
        assertEquals(2, dao.patients(office.getId()).size());

    }

    @Test
    public void testEditPatient (FxRobot robot) throws SQLException {
        robot.clickOn("Sanela");
        robot.clickOn("#btnEditPatient");

        robot.lookup("#nameFld").tryQuery().isPresent();

        TextField fieldName = robot.lookup("#nameFld").queryAs(TextField.class);
        assertNotNull(fieldName);
        assertEquals("Sanela", fieldName.getText());

        TextField fieldLastName = robot.lookup("#lastNameFld").queryAs(TextField.class);
        assertNotNull(fieldLastName);
        assertEquals("Beglerović", fieldLastName.getText());

        TextField fieldJMBG = robot.lookup("#JMBGFld").queryAs(TextField.class);
        assertNotNull(fieldJMBG);
        assertEquals("124563987", fieldJMBG.getText());

        TextField fieldAddress = robot.lookup("#addressFld").queryAs(TextField.class);
        assertNotNull(fieldAddress);
        assertEquals("Gornji Hotonj 22", fieldAddress.getText());

        TextField fieldPOB = robot.lookup("#POBFld").queryAs(TextField.class);
        assertNotNull(fieldPOB);
        assertEquals("Sarajevo", fieldPOB.getText());

        TextField fieldEmail = robot.lookup("#emailFld").queryAs(TextField.class);
        assertNotNull(fieldEmail);
        assertEquals("sanela1964@gmail.com", fieldEmail.getText());

        TextField fieldYear = robot.lookup("#yearFld").queryAs(TextField.class);
        assertNotNull(fieldYear);
        assertEquals("1964", fieldYear.getText());

        robot.clickOn("#addressFld");
        robot.press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        robot.write("Donji Hotonj 22");

        robot.lookup("#addButton").tryQuery().isPresent();
        robot.clickOn("#addButton");

        DAO dao = DAO.getInstance();
        Patient patient = dao.getPatient(1);
        assertEquals("Donji Hotonj 22", patient.getAddress());
    }
}