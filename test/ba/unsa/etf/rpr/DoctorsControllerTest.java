package ba.unsa.etf.rpr;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.File;
import java.util.ResourceBundle;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class DoctorsControllerTest {
    Stage theStage;
    DoctorsController ctrl;
    DAO dao=DAO.getInstance();
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

        theStage = stage;
    }

    @Test
    public void testDodajGrad(FxRobot robot) {
        DAO.removeInstance();
        File dbfile = new File("database.db");
        dbfile.delete();
        dao = DAO.getInstance();

        robot.clickOn("#addDoctorBtn");

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
        robot.clickOn("#addressFld");
        robot.write("Donji Hotonj 21");
        robot.clickOn("#emailFld");
        robot.write("beglerovicvildana@gmail.com");
        robot.clickOn("#yearFld2");
        robot.write("2020");
        robot.clickOn("#specialtyFld");
        robot.write("Hirurg");

        robot.clickOn("#addButton");

        // Da li je Sarajevo dodano u bazu?
        DAO dao = DAO.getInstance();
        assertEquals(2, dao.doctors(office.getId()).size());

       /* Grad sarajevo = null;
        for(Grad grad : dao.gradovi())
            if (grad.getNaziv().equals("Sarajevo"))
                sarajevo = grad;
        assertNotNull(sarajevo);

        assertEquals(350000, sarajevo.getBrojStanovnika());
        assertEquals("Francuska", sarajevo.getDrzava().getNaziv());
        assertEquals(2, sarajevo.getPobratimi().size());

        int pronadjeni = 0;
        for(Grad grad : sarajevo.getPobratimi()) {
            if (grad.getNaziv().equals("London")) pronadjeni++;
            if (grad.getNaziv().equals("Mančester")) pronadjeni++;
        }
        assertEquals(2, pronadjeni);

        // Ponovo prikazujemo glavnu formu*/
        Platform.runLater(() -> theStage.show());
    }

}