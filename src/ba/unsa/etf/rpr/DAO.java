package ba.unsa.etf.rpr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class DAO {

    private static DAO instance;
    private static Connection conn;

    private PreparedStatement addOfficeStatement, getOfficesStatement, getOfficeWithUsernameStatement, getPatientsStatement;
    private PreparedStatement addPatientStatement, updatePatientStatment, deletePatientStatement,getAppointmentsByDate,addReportStatement;
    private PreparedStatement addAppointmentStatement, getAppointmentsStatement, getPatientStatement,getDoctorStatement,getDoctorsStatement,addDoctorStatement;
    private PreparedStatement deleteDoctorStatement,updateDoctorStatment,getAppointementStatement,deleteAppointmentStatement,updateAppointmentStatment,markPatientAsInactiveStatement,markDoctorAsInactiveStatement ;
    private PreparedStatement getMaxIdForOffice, getMaxIdForPatient, getMaxIdForDoctor, getMaxIdForAppointment, updateOfficeStatement;
    private DAO () {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:database.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {

            addOfficeStatement=conn.prepareStatement("INSERT INTO offices VALUES (?,?,?,?,?)");
        } catch (SQLException e) {
            regenerateDatabase();
            try {
                addOfficeStatement=conn.prepareStatement("INSERT INTO offices VALUES (?,?,?,?,?)");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
            try {
                addOfficeStatement=conn.prepareStatement("INSERT INTO offices VALUES (?,?,?,?,?)");
                addDoctorStatement=conn.prepareStatement("INSERT INTO doctors VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
                addPatientStatement=conn.prepareStatement("INSERT INTO patients VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
                addAppointmentStatement=conn.prepareStatement("INSERT INTO appointments VALUES (?,?,?,?,?,?,?,?,?,?)");
                addReportStatement=conn.prepareStatement("UPDATE appointments SET anamnesis=?, diagnosis=?, recommendation=? WHERE id=?");

                deleteDoctorStatement=conn.prepareStatement("DELETE FROM doctors WHERE JMBG=?");
                deleteAppointmentStatement=conn.prepareStatement("DELETE FROM appointments WHERE id=?");
                deletePatientStatement=conn.prepareStatement("DELETE FROM patients WHERE id=?");
                deleteAppointmentStatement=conn.prepareStatement("DELETE FROM appointments WHERE id=?");

                getOfficesStatement=conn.prepareStatement("SELECT * FROM offices");
                getOfficeWithUsernameStatement=conn.prepareStatement("SELECT * FROM offices WHERE username=?");
                getPatientsStatement=conn.prepareStatement("SELECT * FROM patients WHERE office_id=?");
                getPatientStatement=conn.prepareStatement("SELECT * FROM patients WHERE id=?");
                getAppointmentsStatement=conn.prepareStatement("SELECT * FROM appointments WHERE office_id=?");
                getDoctorStatement=conn.prepareStatement("SELECT * FROM doctors WHERE id=?");
                getDoctorsStatement=conn.prepareStatement("SELECT * FROM doctors WHERE office_id=?");
                getAppointementStatement=conn.prepareStatement("SELECT * FROM appointments WHERE id=?");
                getAppointmentsByDate=conn.prepareStatement("SELECT * FROM appointments WHERE doctor_id=? AND date=? AND office_id=?");

                updatePatientStatment=conn.prepareStatement("UPDATE patients SET firstName=?, lastName=?, JMBG=?, gender=?, DOB=?, POB=?, address=?, status=?, email=?, active=? WHERE id=?");
                updateDoctorStatment=conn.prepareStatement("UPDATE doctors SET firstName=?, lastName=?, JMBG=?, DOE=?, POB=?, address=?, email=?, DOE=?, specialty=?, active=? WHERE id=?");
                updateAppointmentStatment=conn.prepareStatement("UPDATE appointments SET date=?, time=?, doctor_id=?, patient_id=?, type=?, anamnesis=?, diagnosis=?, recommendation=? WHERE id=?");
                updateOfficeStatement=conn.prepareStatement("UPDATE offices SET name=?, address=?, username=?, password=? WHERE id=?");

                markPatientAsInactiveStatement=conn.prepareStatement("UPDATE patients SET active=0 WHERE id=?");
                markDoctorAsInactiveStatement=conn.prepareStatement("UPDATE doctors SET active=0 WHERE id=?");

                getMaxIdForOffice = conn.prepareStatement("SELECT MAX(id)+1 FROM offices");
                getMaxIdForPatient=conn.prepareStatement("SELECT MAX(id)+1 FROM patients");
                getMaxIdForDoctor = conn.prepareStatement("SELECT MAX(id)+1 FROM doctors");
                getMaxIdForAppointment = conn.prepareStatement("SELECT MAX(id)+1 FROM appointments");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
    }

    public static Connection getConn() {
        return conn;
    }

    private void regenerateDatabase() {
        Scanner ulaz = null;
        try {
            ulaz = new Scanner(new FileInputStream("database.db.sql"));
            String sqlUpit = "";
            while (ulaz.hasNext()) {
                sqlUpit += ulaz.nextLine();
                if ( sqlUpit.charAt( sqlUpit.length()-1 ) == ';') {
                    try {
                        Statement stmt = conn.createStatement();
                        stmt.execute(sqlUpit);
                        sqlUpit = "";
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            ulaz.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static DAO getInstance() {
        if (instance == null) instance = new DAO();
        return instance;
    }

    public static void removeInstance() {
        if (instance == null) return;
        instance.close();
        instance = null;
    }

    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void addOffice(Office office) throws OfficeWithThisUsernameAlreadyExist {
        try {
            getOfficeWithUsernameStatement.setString(1,office.getUsername());
            ResultSet rs=getOfficeWithUsernameStatement.executeQuery();
            if (rs.next())
                throw new OfficeWithThisUsernameAlreadyExist("Office with this username already exists!");
            ResultSet rs2 = getMaxIdForOffice.executeQuery();
            int id = 1;
            if (rs2.next()) {
                id = rs2.getInt(1);
            }
            addOfficeStatement.setInt(1,id);
            addOfficeStatement.setString(2, office.getName());
            addOfficeStatement.setString(3, office.getAddress());
            addOfficeStatement.setString(4, office.getUsername());
            addOfficeStatement.setString(5, office.getPassword());
            addOfficeStatement.executeUpdate();
        } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public Office getOfficeWithUsername (String username) {
        Office office=null;
        try {
            getOfficeWithUsernameStatement.setString(1,username);
            ResultSet rs=getOfficeWithUsernameStatement.executeQuery();
            if (rs.next()) {
                office= new Office(rs.getInt(1),rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return office;
    }

    public void addPatient(Patient patient, int officeId) {
        try {
            ResultSet rs = getMaxIdForPatient.executeQuery();
            int id = 1;
            if (rs.next()) {
                id = rs.getInt(1);
            }
            addPatientStatement.setInt(1,id);
            addPatientStatement.setString(2,patient.getFirstName());
            addPatientStatement.setString(3,patient.getLastName());
            addPatientStatement.setString(4,patient.getJMBG());
            addPatientStatement.setString(5, patient.getGender().toString());
            addPatientStatement.setString(6,patient.getBirthDate().toString());
            addPatientStatement.setString(7,patient.getBirthPlace());
            addPatientStatement.setString(8,patient.getAddress());
            addPatientStatement.setString(9,patient.getStatus().toString());
            addPatientStatement.setString(10,patient.getEmail());
            int active=0;
            if (patient.isActive()) active=1;
            addPatientStatement.setInt(11, active);
            addPatientStatement.setInt(12, officeId);
            addPatientStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Patient> patients(int officeId) {
        ArrayList<Patient> p=new ArrayList<>();
        try {
            getPatientsStatement.setInt(1,officeId);
            ResultSet rs=getPatientsStatement.executeQuery();
            while (rs.next()) {
                Patient patient=getPatientFromResultSet(rs);
                if(patient.isActive()) {
                    p.add(patient);
                }
            }
        } catch (SQLException e) {
        }
        return p;
    }

    Patient getPatientFromResultSet(ResultSet rs) {
        Patient patient= null;
        Gender gender=Gender.MALE;
        Status status=Status.EMPLOYEE;
        String s= new String();
        try {
            s = rs.getString(9);
        if (s.equals("RETIREE"))
                status=Status.RETIREE;
        else if (s.equals("EMPLOYEE"))
            status=Status.EMPLOYEE;
        else if (s.equals("OTHER"))
            status=Status.OTHER;
        else if (s.equals("STUDENT"))
            status=Status.STUDENT;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (rs.getString(5).equals("FEMALE"))
                gender=Gender.FEMALE;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        boolean active=false;
        try {
            if(rs.getInt(11)==1) active=true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            patient = new Patient(rs.getInt(1), rs.getString(2), rs.getString(3),rs.getString(4),gender, LocalDate.parse(rs.getString(6),df),rs.getString(7), rs.getString(8),status, rs.getString(10),active);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patient;
    }

    public void editPatient(Patient patient) {
        try {
            updatePatientStatment.setString(1, patient.getFirstName());
            updatePatientStatment.setString(2, patient.getLastName());
            updatePatientStatment.setString(3, patient.getJMBG());
            updatePatientStatment.setString(4, patient.getGender().toString());
            updatePatientStatment.setString(5, patient.getBirthDate().toString());
            updatePatientStatment.setString(6, patient.getBirthPlace());
            updatePatientStatment.setString(7, patient.getAddress());
            updatePatientStatment.setString(8, patient.getStatus().toString());
            updatePatientStatment.setString(9, patient.getEmail());
            int active=0;
            if (patient.isActive()) active=1;
            updatePatientStatment.setInt(10,active);
            updatePatientStatment.setInt(11, patient.getId());
            updatePatientStatment.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePatient(int id) {
        try {
            getPatientStatement.setInt(1,id);
            ResultSet rs=getPatientStatement.executeQuery();
            if (!rs.next()) return;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            markPatientAsInactiveStatement.setInt(1,id);
            markPatientAsInactiveStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Appointment> appointments(int officeId) {
        ArrayList<Appointment> a=new ArrayList<>();
        try {
            getAppointmentsStatement.setInt(1,officeId);
            ResultSet rs=getAppointmentsStatement.executeQuery();
            while (rs.next()) {
                Appointment appointment=getAppointmentFromResultSet(rs);
                a.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return a;
    }

    public Patient getPatient(int id) {
        try {
            getPatientStatement.setInt(1,id);
            ResultSet rs=getPatientStatement.executeQuery();
            if (rs.next())  return getPatientFromResultSet(rs);
            return null;
        } catch (SQLException e) {
            return null;
        }
    }

    private Appointment getAppointmentFromResultSet(ResultSet rs) {
        Appointment appointment=null;
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter tf=DateTimeFormatter.ofPattern("HH:mm");
        try {
            appointment=new Appointment(rs.getInt(1), LocalDate.parse(rs.getString(2),df), LocalTime.parse(rs.getString(3),tf),null,null, rs.getString(6), rs.getString(8),rs.getString(9),rs.getString(10));
            appointment.setPatient(getPatient(rs.getInt(4)));
            appointment.setDoctor(getDoctor(rs.getInt(5)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointment;
    }

    public void addAppointment(Appointment appointment, int officeId) {
        try {
            ResultSet rs = getMaxIdForAppointment.executeQuery();
            int id = 1;
            if (rs.next()) {
                id = rs.getInt(1);
            }
            addAppointmentStatement.setInt(1,id);
            addAppointmentStatement.setString(2, appointment.getDate().toString());
            addAppointmentStatement.setString(3,appointment.getTime().toString());
            addAppointmentStatement.setInt(4,appointment.getPatient().getId());
            addAppointmentStatement.setInt(5,appointment.getDoctor().getId());
            addAppointmentStatement.setString(6,appointment.getType());
            addAppointmentStatement.setInt(7,officeId);
            addAppointmentStatement.setString(8,appointment.getAnamnesis());
            addAppointmentStatement.setString(9,appointment.getDiagnosis());
            addAppointmentStatement.setString(10,appointment.getRecommendation());
            addAppointmentStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAppointment(int id) {
        try {
            getAppointementStatement.setInt(1,id);
            ResultSet rs=getAppointementStatement.executeQuery();
            if (rs.next()==false) return;
            Appointment appointment=getAppointmentFromResultSet(rs);
            deleteAppointmentStatement.setInt(1,appointment.getId());
            deleteAppointmentStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editAppointment(Appointment appointment) {
        try {
            updateAppointmentStatment.setString(1, appointment.getDate().toString());
            updateAppointmentStatment.setString(2, appointment.getTime().toString());
            updateAppointmentStatment.setInt(3, appointment.getDoctor().getId());
            updateAppointmentStatment.setInt(4, appointment.getPatient().getId());
            updateAppointmentStatment.setString(5, appointment.getType());
            updateAppointmentStatment.setString(6, appointment.getAnamnesis());
            updateAppointmentStatment.setString(7, appointment.getDiagnosis());
            updateAppointmentStatment.setString(8, appointment.getRecommendation());
            updateAppointmentStatment.setInt(9,appointment.getId());
            updateAppointmentStatment.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<LocalTime> getAppointments(Doctor doctor, LocalDate date, int office_id) {
        ArrayList<LocalTime> appointments=new ArrayList<>();
        try {
            getAppointmentsByDate.setInt(1,doctor.getId());
            getAppointmentsByDate.setString(2, date.toString());
            getAppointmentsByDate.setInt(3, office_id);
            ResultSet rs=getAppointmentsByDate.executeQuery();
            while (rs.next()) {
                Appointment appointment=getAppointmentFromResultSet(rs);
                appointments.add(appointment.getTime());
            }
            return appointments;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Doctor getDoctor(int id) {
        try {
            getDoctorStatement.setInt(1,id);
            ResultSet rs=getDoctorStatement.executeQuery();
            if (rs.next())  return getDoctorFromResultSet(rs);
            return null;
        } catch (SQLException e) {
            return null;
        }
    }

    private Doctor getDoctorFromResultSet(ResultSet rs) {
        Doctor doctor= null;
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        boolean active=false;
        try {
            if (rs.getInt(11)==1) active=true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
        doctor = new Doctor(rs.getInt(1), rs.getString(2), rs.getString(3),rs.getString(4),LocalDate.parse(rs.getString(5),df),rs.getString(6), rs.getString(7), rs.getString(8),LocalDate.parse(rs.getString(9),df), rs.getString(10),active);
    } catch (SQLException e) {
        e.printStackTrace();
    }
        return doctor;
    }

    public ArrayList<Doctor> doctors(int officeId) {
        ArrayList<Doctor> d=new ArrayList<>();
        try {
            getDoctorsStatement.setInt(1,officeId);
            ResultSet rs=getDoctorsStatement.executeQuery();
            while (rs.next()) {
                Doctor doctor=getDoctorFromResultSet(rs);
                if (doctor.isActive())
                d.add(doctor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return d;
    }

    public void addDoctor(Doctor doctor, int officeId) {
        try {
            ResultSet rs = getMaxIdForDoctor.executeQuery();
            int id = 1;
            if (rs.next()) {
                id = rs.getInt(1);
            }
            addDoctorStatement.setInt(1,3);
            addDoctorStatement.setString(2,doctor.getFirstName());
            addDoctorStatement.setString(3,doctor.getLastName());
            addDoctorStatement.setString(4,doctor.getJMBG());
            addDoctorStatement.setString(5, doctor.getBirthDate().toString());
            addDoctorStatement.setString(6,doctor.getBirthPlace());
            addDoctorStatement.setString(7,doctor.getAddress());
            addDoctorStatement.setString(8,doctor.getEmail());
            addDoctorStatement.setString(9,doctor.getEmploymentDate().toString());
            addDoctorStatement.setString(10,doctor.getSpecialization());
            int i=0;
            if (doctor.isActive()) i=1;
            addDoctorStatement.setInt(11,i);
            addDoctorStatement.setInt(12, officeId);
            addDoctorStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editDoctor(Doctor doctor) {
        try {
            updateDoctorStatment.setString(1, doctor.getFirstName());
            updateDoctorStatment.setString(2, doctor.getLastName());
            updateDoctorStatment.setString(3, doctor.getJMBG());
            updateDoctorStatment.setString(4, doctor.getBirthDate().toString());
            updateDoctorStatment.setString(5, doctor.getBirthPlace());
            updateDoctorStatment.setString(6, doctor.getAddress());
            updateDoctorStatment.setString(7, doctor.getEmail());
            updateDoctorStatment.setString(8, doctor.getEmploymentDate().toString());
            updateDoctorStatment.setString(9, doctor.getSpecialization());
            int i=0;
            if (doctor.isActive())i=1;
            updateDoctorStatment.setInt(10,i);
            updateDoctorStatment.setInt(11, doctor.getId());
            updateDoctorStatment.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteDoctor(int id) {
        try {
            getDoctorStatement.setInt(1,id);
            ResultSet rs=getDoctorStatement.executeQuery();
            if (!rs.next()) return;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            markDoctorAsInactiveStatement.setInt(1,id);
            markDoctorAsInactiveStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addReport (Appointment appointment){
        try {
            addReportStatement.setString(1,appointment.getAnamnesis());
            addReportStatement.setString(2,appointment.getDiagnosis());
            addReportStatement.setString(3,appointment.getRecommendation());
            addReportStatement.setInt(4,appointment.getId());
            addReportStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void editOffice(Office office) throws OfficeWithThisUsernameAlreadyExist {
        try {
            getOfficeWithUsernameStatement.setString(1,office.getUsername());
            ResultSet rs=getOfficeWithUsernameStatement.executeQuery();
            Office office1;
            if (rs.next()) {
                office1 = new Office(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
                if (office1.getId() != office.getId())
                    throw new OfficeWithThisUsernameAlreadyExist("Office with this username already exists!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            updateOfficeStatement.setString(1,office.getName());
            updateOfficeStatement.setString(2, office.getAddress());
            updateOfficeStatement.setString(3, office.getUsername());
            updateOfficeStatement.setString(4, office.getPassword());
            updateOfficeStatement.setInt(5, office.getId());
            updateOfficeStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void resetBaseToDefault() throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("DELETE FROM appointments");
        stmt.executeUpdate("DELETE FROM patients");
        stmt.executeUpdate("DELETE FROM doctors");
        stmt.executeUpdate("DELETE FROM offices");
        regenerateDatabase();
    }

}

