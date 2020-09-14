package ba.unsa.etf.rpr;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
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
    public void testTableView(FxRobot robot) {
        TableView tableViewDoctors = robot.lookup("#tableViewDoctors").queryAs(TableView.class);
        assertEquals(1, tableViewDoctors.getItems().size());
    }

    @Test
    public void testRemoveDoctor(FxRobot robot) {
        robot.clickOn("Samira");
        robot.clickOn("#btnRemove");

        // Čekamo da dijalog postane vidljiv
        robot.lookup(".dialog-pane").tryQuery().isPresent();

        // Klik na dugme Ok
        DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        robot.clickOn(okButton);

        // Da li sada ima 4 grada u tableview?
        TableView tableView = robot.lookup("#tableViewDoctors").queryAs(TableView.class);
        assertEquals(0, tableView.getItems().size());

        // Da li je Manchester obrisan iz baze?
        DAO dao = DAO.getInstance();
        assertEquals(0, dao.doctors(office.getId()).size());
        //Platform.runLater(() -> theStage.show());
    }

  @Test public void testAddDoctor(FxRobot robot) {
        robot.clickOn("#addDoctorBtn");

        robot.lookup("#fieldNaziv").tryQuery().isPresent();

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


        DAO dao = DAO.getInstance();
        assertEquals(2, dao.doctors(office.getId()).size());
    }

   /* @Test
    public void testIzmijeniGrad(FxRobot robot) {
        // Ovaj test samo provjerava da li se otvara forma za dodavanje grada
        robot.clickOn("Graz");
        robot.clickOn("#btnIzmijeniGrad");

        // Čekamo da dijalog postane vidljiv
        robot.lookup("#fieldNaziv").tryQuery().isPresent();

        // Da li polje Naziv sadrži ispravno ime grada?
        TextField fieldNaziv = robot.lookup("#fieldNaziv").queryAs(TextField.class);
        assertNotNull(fieldNaziv);
        assertEquals("Graz", fieldNaziv.getText());

        // Da li polje broj stanovnika sadrži ispravan broj stanovnika
        TextField fieldBrojStanovnika = robot.lookup("#fieldBrojStanovnika").queryAs(TextField.class);
        assertNotNull(fieldBrojStanovnika);
        assertEquals("280200", fieldBrojStanovnika.getText());

        robot.clickOn("#fieldBrojStanovnika");
        robot.press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        robot.write("123456");

        // Klik na dugme Ok
        robot.clickOn("#btnOk");

        // Da li je promijenjen broj stanovnika Graza?
        GeografijaDAO dao = GeografijaDAO.getInstance();
        Grad graz = dao.nadjiGrad("Graz");
        assertEquals(123456, graz.getBrojStanovnika());
    }

   @Test
    public void testDodajDrzavu(FxRobot robot) {
        // Otvaranje forme za dodavanje
        robot.clickOn("#btnDodajDrzavu");

        // Čekamo da dijalog postane vidljiv
        robot.lookup("#fieldNaziv").tryQuery().isPresent();

        // Postoji li fieldNaziv
        robot.clickOn("#fieldNaziv");
        robot.write("Bosna i Hercegovina");

        // Glavni grad će biti automatski izabran kao prvi

        // Klik na dugme Ok
        robot.clickOn("#btnOk");

        // Da li je BiH dodana u bazu?
        GeografijaDAO dao = GeografijaDAO.getInstance();
        assertEquals(4, dao.drzave().size());

        boolean pronadjeno = false;
        for(Drzava drzava : dao.drzave())
            if (drzava.getNaziv().equals("Bosna i Hercegovina")) {
                pronadjeno = true;
                break;
            }
        assertTrue(pronadjeno);
    }*/
}