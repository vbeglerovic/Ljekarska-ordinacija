package ba.unsa.etf.rpr.projekat;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DAOTest {

    @Test
    void regenerateFile() {
        DAO.removeInstance();
        File dbfile = new File("database.db");
        dbfile.delete();
        DAO dao = DAO.getInstance();
        ArrayList<Patient> patients = dao.patients(1);
        ArrayList<Doctor> doctors = dao.doctors(1);
        assertEquals(1, patients.size());
        assertEquals(1, doctors.size());
        assertEquals("Sanela", patients.get(0).getFirstName());
        assertEquals("Vildana", doctors.get(0).getFirstName());
    }

    @Test
    void addOffice() {
        DAO.removeInstance();
        File dbfile = new File("database.db");
        dbfile.delete();
        DAO dao = DAO.getInstance();
        Office office=new Office(0, "Office3", "address3", "username3", "password3");
        try {
            dao.addOffice(office);
        } catch (OfficeWithThisUsernameAlreadyExists officeWithThisUsernameAlreadyExists) {
            officeWithThisUsernameAlreadyExists.printStackTrace();
        }
        assertEquals(3, dao.getOffices().size());
    }

    @Test
    void editOffice() {
        DAO.removeInstance();
        File dbfile = new File("database.db");
        dbfile.delete();
        DAO dao = DAO.getInstance();
        Office office = dao.getOfficeWithUsername("username2");
        office.setUsername("username3");
        try {
            dao.editOffice(office);
        } catch (OfficeWithThisUsernameAlreadyExists officeWithThisUsernameAlreadyExists) {
            officeWithThisUsernameAlreadyExists.printStackTrace();
        }
        ArrayList<Office> offices = dao.getOffices();
        assertEquals("username3", offices.get(1).getUsername());
    }

    @Test
    void editOfficeException() {
        DAO.removeInstance();
        File dbfile = new File("database.db");
        dbfile.delete();
        DAO dao = DAO.getInstance();
        Office office = dao.getOfficeWithUsername("username2");
        office.setUsername("username1");
        Exception exception = assertThrows(OfficeWithThisUsernameAlreadyExists.class,
                () -> dao.addOffice(office));

        ArrayList<Office> offices = dao.getOffices();
        assertEquals(2, offices.size());
    }

    @Test
    void editPatient() {
        DAO.removeInstance();
        File dbfile = new File("database.db");
        dbfile.delete();
        DAO dao = DAO.getInstance();
        Patient patient = dao.getPatient(1);
        patient.setFirstName("Selma");
        dao.editPatient(patient);

        ArrayList<Patient> patients = dao.patients(1);
        assertEquals("Selma", patients.get(0).getFirstName());
    }

    @Test
    void editDoctor() {
        DAO.removeInstance();
        File dbfile = new File("database.db");
        dbfile.delete();
        DAO dao = DAO.getInstance();
        Doctor doctor = dao.getDoctor(1);
        doctor.setFirstName("Selma");
        dao.editDoctor(doctor);

        ArrayList<Doctor> doctors = dao.doctors(1);
        assertEquals("Selma", doctors.get(0).getFirstName());
    }

    @Test
    void addPatient() {
        DAO.removeInstance();
        File dbfile = new File("database.db");
        dbfile.delete();
        DAO dao = DAO.getInstance();
        Patient patient = new Patient(0,"Edin", "Beglerovic","123456789", Gender.MALE, LocalDate.of(1999,7,7),"Sarajevo", "Donji Hotonj 21", Status.STUDENT,"vildanabeglerovic@gmail.com",true);
        dao.addPatient(patient,1);
        ArrayList<Patient> patients = dao.patients(1);
        assertEquals(2, patients.size());
    }

    @Test
    void addDoctor() {
        DAO.removeInstance();
        File dbfile = new File("database.db");
        dbfile.delete();
        DAO dao = DAO.getInstance();
        Doctor doctor = new Doctor(0,"Edin", "Beglerovic","123456789", LocalDate.of(1999,7,7),"Sarajevo", "Donji Hotonj 21","vildanabeglerovic@gmail.com", LocalDate.of(2020,04,04), "Stomatolog",true);
        dao.addDoctor(doctor,1);
        ArrayList<Doctor> doctors = dao.doctors(1);
        assertEquals(2, doctors.size());
    }

    @Test
    void addAppointment() {
        DAO.removeInstance();
        File dbfile = new File("database.db");
        dbfile.delete();
        DAO dao = DAO.getInstance();
        Patient patient=dao.getPatient(1);
        Doctor doctor=dao.getDoctor(1);
        Appointment appointment=new Appointment(0, LocalDate.of(2020,12,6), LocalTime.of(10,30), patient , doctor, false, null, null, null);
        dao.addAppointment(appointment, 1);
        assertEquals(2, dao.appointments(1).size());
    }

    @Test
    void editAppointment() {
        DAO.removeInstance();
        File dbfile = new File("database.db");
        dbfile.delete();
        DAO dao = DAO.getInstance();
        Appointment appointment=dao.appointments(1).get(0);
        appointment.setTime(LocalTime.of(11,30));
        dao.editAppointment(appointment);
        assertEquals("11:30", dao.appointments(1).get(0).getTime().toString());
    }
}
