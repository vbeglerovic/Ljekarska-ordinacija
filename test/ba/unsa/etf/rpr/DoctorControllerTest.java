package ba.unsa.etf.rpr;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
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

class DoctorControllerTest {
    Stage theStage;
    DoctorController ctrl;
    DAO dao = DAO.getInstance();

    @Start
    public void start (Stage stage) throws Exception {
        Office office=dao.getOfficeWithUsername("username2");
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/doctor.fxml"),bundle);
        ctrl = new DoctorController(null,office,true);
        loader.setController(ctrl);
        Parent root = loader.load();
        stage.setTitle("Doctor");
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
    public void fieldAddressIsEmpty(FxRobot robot) {
        robot.lookup("#nameFld").tryQuery().isPresent();
        robot.clickOn("#nameFld");
        robot.write("Vildana");
        robot.clickOn("#lastNameFld");
        robot.write("Beglerovic");
        robot.clickOn("#JMBGFld");
        robot.write("965321478");
        robot.clickOn("#yearFld1");
        robot.write("1999");
        robot.clickOn("#POBFld");
        robot.write("Sarajevo");

        robot.clickOn("#emailFld");
        robot.write("beglerovicvildana@gmail.com");
        robot.clickOn("#yearFld2");
        robot.write("2020");

        robot.clickOn("#specialtyFld");
        robot.write("Hirurg");

        robot.clickOn("#addButton");
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
        assertTrue(robot.lookup("#nameFld").tryQuery().isPresent());
    }
    @Test
    public void invalidYearFormat(FxRobot robot) {
        robot.lookup("#nameFld").tryQuery().isPresent();
        robot.clickOn("#nameFld");
        robot.write("Vildana");
        robot.clickOn("#lastNameFld");
        robot.write("Beglerovic");
        robot.clickOn("#JMBGFld");
        robot.write("965321478");
        robot.clickOn("#yearFld1");
        robot.write("1999a");
        TextField yearFld = robot.lookup("#yearFld1").queryAs(TextField.class);
        Background bg = yearFld.getBackground();
        boolean colorFound = false;
        for (BackgroundFill bf : bg.getFills())
            if (bf.getFill().toString().contains("ffb6c1"))
                colorFound = true;
        assertTrue(colorFound);
        robot.clickOn("#POBFld");
        robot.write("Sarajevo");
        robot.clickOn("#addressFld");
        robot.write("Donji Hotonj 21");
        robot.clickOn("#emailFld");
        robot.write("beglerovicvildana@gmail.com");
        robot.clickOn("#yearFld2");
        robot.write("aaaa");
        bg = yearFld.getBackground();
        colorFound = false;
        for (BackgroundFill bf : bg.getFills())
            if (bf.getFill().toString().contains("ffb6c1"))
                colorFound = true;
        assertTrue(colorFound);
    }
}