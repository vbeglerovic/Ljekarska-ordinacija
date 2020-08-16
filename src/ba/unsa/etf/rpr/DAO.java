package ba.unsa.etf.rpr;

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
    private Connection conn;

   private PreparedStatement addOfficeStatement, getOfficesStatement, getOfficeWithUsernameStatement,getPasswordFromOfficeStatement, getPatientsStatement;
    private PreparedStatement addPatientStatement, updatePatientStatment, deletePatientStatement, getPatientByJMBGStatement,getPatientsByNameStatement;
    private PreparedStatement addAppointmentStatement, getAppointmentsStatement, getPatientStatement,getDoctorStatement,getDoctorsStatement;
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
                addAppointmentStatement=conn.prepareStatement("INSERT INTO appointments VALUES (?,?,?,?,?,?,?,?)");
                getAppointmentsStatement=conn.prepareStatement("SELECT * FROM appointments WHERE id=?");
                getPatientStatement=conn.prepareStatement("SELECT * FROM patients WHERE id=?");
                getDoctorStatement=conn.prepareStatement("SELECT * FROM doctors WHERE id=?");
                getDoctorsStatement=conn.prepareStatement("SELECT * FROM doctors WHERE office_id=?");

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


    /*public boolean checkIfOfficeExist(String username, String password) {
        try {
            getOfficeWithUsernameStatement.setString(1,username);
            ResultSet rs=getOfficeWithUsernameStatement.executeQuery();
            if (rs!=null && rs.getString(1).equals(password)) return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }*/

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
        Gender gender=Gender.MUSKO;
        Status status=Status.EMPLOYEE;
        String s= null;
        try {
            s = rs.getString(9);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (s.equals("RETIREE"))
                status=Status.RETIREE;
        else if (s.equals("EMPLOYEE"))
            status=Status.EMPLOYEE;
        else if (s.equals("OTHER"))
            status=Status.OTHER;
        try {
            if (rs.getString(5).equals("ZENSKO"))
                gender=Gender.ZENSKO;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            patient = new Patient(rs.getInt(1), rs.getString(2), rs.getString(3),rs.getString(4),gender, LocalDate.parse(rs.getString(6),df),rs.getString(7), rs.getString(8),status, rs.getString(10), rs.getInt(11));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //patient.setDrzava(dajDrzavu(rs.getInt(4), grad));
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

    public ArrayList<Patient> searchPatients (int officeId, String firstName, String lastName) {
        ArrayList<Patient> p=new ArrayList<>();
        try {
            getPatientsByNameStatement.setInt(1,officeId);
            getPatientsByNameStatement.setString(2,firstName);
            getPatientsByNameStatement.setString(3,lastName);
            ResultSet rs=getPatientsByNameStatement.executeQuery();
            while (rs.next()) {
                Patient patient=getPatientFromResultSet(rs);
                p.add(patient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return p;
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
        DateTimeFormatter tf=DateTimeFormatter.ofPattern("HH-MM");
        boolean kontrola=false;
        Patient p=new Patient();
        Doctor d=new Doctor();
        try {
            if (rs.getString(6).equals("Kontrola"))
                kontrola=true;
            getPatientStatement.setInt(1,rs.getInt(4));
            ResultSet rez=getPatientStatement.executeQuery();
            p=getPatientFromResultSet(rez);
            getDoctorStatement.setInt(1,rs.getInt(5));
            rez=getDoctorStatement.executeQuery();
            d=getDoctorFromResultSet(rez);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            appointment=new Appointment(rs.getInt(1), LocalDate.parse(rs.getString(2),df), LocalTime.parse(rs.getString(3),tf),p,d, kontrola, "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointment;
    }

    private Doctor getDoctorFromResultSet(ResultSet rs) {
        return null;
    }

    public void addAppointment(Appointment appointment, int officeId) {
        try {
            addAppointmentStatement.setString(2, appointment.getDate().toString());
            addAppointmentStatement.setString(3,appointment.getTime().toString());
            addAppointmentStatement.setInt(4,appointment.getPatient().getId());
            addAppointmentStatement.setInt(5,appointment.getDoctor().getId());
            String s;
            if (appointment.isKontrola())  s="Kontrola";
            else s="";
            addAppointmentStatement.setString(6,s);
            addAppointmentStatement.setString(7,"");
            addAppointmentStatement.setInt(8,officeId);
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
}

