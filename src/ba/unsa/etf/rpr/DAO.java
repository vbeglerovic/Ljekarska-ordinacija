package ba.unsa.etf.rpr;

import javafx.event.ActionEvent;

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

    public static Connection getConn() {
        return conn;
    }

    private PreparedStatement addOfficeStatement, getOfficesStatement, getOfficeWithUsernameStatement,getPasswordFromOfficeStatement, getPatientsStatement;
    private PreparedStatement addPatientStatement, updatePatientStatment, deletePatientStatement, getPatientByJMBGStatement,getPatientsByNameStatement;
    private PreparedStatement addAppointmentStatement, getAppointmentsStatement, getPatientStatement,getDoctorStatement,getDoctorsStatement,addDoctorStatement;
    private PreparedStatement getDoctorsByNameStatement, deleteDoctorStatement,updateDoctorStatment,getAppointementStatement,deleteAppointmentStatement,updateAppointmentStatment;
    private PreparedStatement getAppointmentsByDate;
    private DAO () {
        try {
            conn= DriverManager.getConnection("jdbc:sqlite:database.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
            try {
                addOfficeStatement=conn.prepareStatement("INSERT INTO offices VALUES (?,?,?,?,?)");
                getOfficesStatement=conn.prepareStatement("SELECT * FROM offices");
                addOfficeStatement=conn.prepareStatement("INSERT INTO offices VALUES (?,?,?,?,?)");
                getOfficeWithUsernameStatement=conn.prepareStatement("SELECT * FROM offices WHERE username=?");
                getPasswordFromOfficeStatement=conn.prepareStatement("SELECT password FROM offices WHERE id=?");
                getPatientsStatement=conn.prepareStatement("SELECT * FROM patients WHERE office_id=?");
                addPatientStatement=conn.prepareStatement("INSERT INTO patients VALUES (?,?,?,?,?,?,?,?,?,?,?)");
                updatePatientStatment=conn.prepareStatement("UPDATE patients SET firstName=?, lastName=?, JMBG=?, gender=?, DOB=?, POB=?, address=?, status=?, email=? WHERE id=?");
                deletePatientStatement=conn.prepareStatement("DELETE FROM patients WHERE JMBG=?");
                getPatientByJMBGStatement=conn.prepareStatement("SELECT * FROM patients WHERE JMBG=?");
                getPatientsByNameStatement=conn.prepareStatement("SELECT * FROM patients WHERE office_id=? AND firstName=? AND lastName=?");
                addAppointmentStatement=conn.prepareStatement("INSERT INTO appointments VALUES (?,?,?,?,?,?,?,?,?,?)");
                getAppointmentsStatement=conn.prepareStatement("SELECT * FROM appointments WHERE office_id=?");
                getPatientStatement=conn.prepareStatement("SELECT * FROM patients WHERE id=?");
                getDoctorStatement=conn.prepareStatement("SELECT * FROM doctors WHERE id=?");
                getDoctorsStatement=conn.prepareStatement("SELECT * FROM doctors WHERE office_id=?");
                addDoctorStatement=conn.prepareStatement("INSERT INTO doctors VALUES (?,?,?,?,?,?,?,?,?,?,?)");
                getDoctorsByNameStatement=conn.prepareStatement("SELECT * FROM doctors WHERE office_id=? AND firstName=? AND lastName=?");
                deleteDoctorStatement=conn.prepareStatement("DELETE FROM doctors WHERE JMBG=?");
                updateDoctorStatment=conn.prepareStatement("UPDATE doctors SET firstName=?, lastName=?, JMBG=?, DOE=?, POB=?, address=?, email=?, DOE=?, specialty=? WHERE id=?");
                deleteAppointmentStatement=conn.prepareStatement("DELETE FROM appointments WHERE id=?");
                getAppointementStatement=conn.prepareStatement("SELECT * FROM appointments WHERE id=?");
                updateAppointmentStatment=conn.prepareStatement("UPDATE appointments SET date=?, time=?, doctor_id=?, patient_id=?, type=?, anamnesis=?, diagnosis=?, recommendation=? WHERE id=?");
                getAppointmentsByDate=conn.prepareStatement("SELECT * FROM appointments WHERE doctor_id=? AND date=? AND office_id=?");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
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

    public void addOffice(Office office) {
        try {
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


    public boolean checkIfOfficeExist(Office office, String password) {
        try {
            getPasswordFromOfficeStatement.setInt(1,office.getId());
            ResultSet rs=getPasswordFromOfficeStatement.executeQuery();
            if (rs.next()) {
                return rs.getString(1).equals(password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addPatient(Patient patient, int officeId) {
        try {
            addPatientStatement.setString(2,patient.getFirstName());
            addPatientStatement.setString(3,patient.getLastName());
            addPatientStatement.setString(4,patient.getJMBG());
            addPatientStatement.setString(5, patient.getGender().toString());
            addPatientStatement.setString(6,patient.getBirthDate().toString());
            addPatientStatement.setString(7,patient.getBirthPlace());
            addPatientStatement.setString(8,patient.getAddress());
            addPatientStatement.setString(9,patient.getStatus().toString());
            addPatientStatement.setString(10,patient.getEmail());
            addPatientStatement.setInt(11, officeId);
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
                p.add(patient);
            }
        } catch (SQLException e) {
        }
        return p;
    }


    Patient getPatientFromResultSet(ResultSet rs) {
        Patient patient= null;
        Gender gender=Gender.MALE;
        Status status=Status.EMPLOYEE;
        String s= null;
        try {
            s = rs.getString(9);
        if (s.equals("RETIREE"))
                status=Status.RETIREE;
        else if (s.equals("EMPLOYEE"))
            status=Status.EMPLOYEE;
        else if (s.equals("OTHER"))
            status=Status.OTHER;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (rs.getString(5).equals("ZENSKO"))
                gender=Gender.FEMALE;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            patient = new Patient(rs.getInt(1), rs.getString(2), rs.getString(3),rs.getString(4),gender, LocalDate.parse(rs.getString(6),df),rs.getString(7), rs.getString(8),status, rs.getString(10));
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
            updatePatientStatment.setInt(10, patient.getId());
            updatePatientStatment.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePatient(String jmbg) {
        try {
            getPatientByJMBGStatement.setString(1,jmbg);
            ResultSet rs=getPatientByJMBGStatement.executeQuery();
            if (rs.next()==false) return;
            Patient patient=getPatientFromResultSet(rs);
            deletePatientStatement.setString(1,patient.getJMBG());
            deletePatientStatement.executeUpdate();
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
    private Appointment getAppointmentFromResultSet(ResultSet rs) {
        Appointment appointment=null;
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter tf=DateTimeFormatter.ofPattern("HH:mm");
        try {
            appointment=new Appointment(rs.getInt(1), LocalDate.parse(rs.getString(2),df), LocalTime.parse(rs.getString(3),tf),null,null, rs.getString(6), rs.getString(7),rs.getString(8),rs.getString(9));
            appointment.setPatient(getPatient(rs.getInt(4)));
            appointment.setDoctor(getDoctor(rs.getInt(5)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointment;
    }
    private Patient getPatient(int id) {
        try {
            getPatientStatement.setInt(1,id);
            ResultSet rs=getPatientStatement.executeQuery();
            if (rs.next())  return getPatientFromResultSet(rs);
            return null;
        } catch (SQLException e) {
            return null;
        }
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
        try {
        doctor = new Doctor(rs.getInt(1), rs.getString(2), rs.getString(3),rs.getString(4),LocalDate.parse(rs.getString(5),df),rs.getString(6), rs.getString(7), rs.getString(8),LocalDate.parse(rs.getString(9),df), rs.getString(10));
    } catch (SQLException e) {
        e.printStackTrace();
    }
        return doctor;
    }

    public void addAppointment(Appointment appointment, int officeId) throws ReservedAppointmentExcepction {
        ArrayList<String> terms=new ArrayList<>();
        terms=getAppointments(appointment.getDoctor(),appointment.getDate(),officeId);
        if (terms.contains(appointment.getTime().toString())) throw new ReservedAppointmentExcepction("Termin zauzet");
        try {

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

    public ArrayList<Doctor> doctors(int officeId) {
        ArrayList<Doctor> d=new ArrayList<>();
        try {
            getDoctorsStatement.setInt(1,officeId);
            ResultSet rs=getDoctorsStatement.executeQuery();
            while (rs.next()) {
                Doctor doctor=getDoctorFromResultSet(rs);
                d.add(doctor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return d;
    }

    public void addDoctor(Doctor doctor, int officeId) {
        try {
            addDoctorStatement.setString(2,doctor.getFirstName());
            addDoctorStatement.setString(3,doctor.getLastName());
            addDoctorStatement.setString(4,doctor.getJMBG());
            addDoctorStatement.setString(5, doctor.getBirthDate().toString());
            addDoctorStatement.setString(6,doctor.getBirthPlace());
            addDoctorStatement.setString(7,doctor.getAddress());
            addDoctorStatement.setString(8,doctor.getEmail());
            addDoctorStatement.setString(9,doctor.getEmploymentDate().toString());
            addDoctorStatement.setString(10,doctor.getSpecialization());
            addDoctorStatement.setInt(11, officeId);
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
            updateDoctorStatment.setInt(10, doctor.getId());
            updateDoctorStatment.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteDoctor(int id) {
        try {
            getDoctorStatement.setInt(1,id);
            ResultSet rs=getDoctorStatement.executeQuery();
            if (rs.next()==false) return;
            Doctor doctor=getDoctorFromResultSet(rs);
            deleteDoctorStatement.setString(1,doctor.getJMBG());
            deleteDoctorStatement.executeUpdate();
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

    public ArrayList<String> getAppointments(Doctor doctor, LocalDate date, int office_id) {
        ArrayList<String> appointments=new ArrayList<>();
        try {
            getAppointmentsByDate.setInt(1,doctor.getId());
            getAppointmentsByDate.setString(2, date.toString());
            getAppointmentsByDate.setInt(3, office_id);
            ResultSet rs=getAppointmentsByDate.executeQuery();
            while (rs.next()) {
                Appointment appointment=getAppointmentFromResultSet(rs);
                appointments.add(appointment.getTime().toString());
            }
            return appointments;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



}

