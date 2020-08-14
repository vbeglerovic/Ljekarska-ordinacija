package ba.unsa.etf.rpr;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class DAO {
    private static DAO instance;
    private Connection conn;

   private PreparedStatement addOfficeStatement, getOfficesStatement, getOfficeWithUsernameStatement,getPasswordFromOfficeStatement, getPatientsStatement;
    private PreparedStatement addPatientStatement;
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
                getPatientsStatement=conn.prepareStatement("SELECT * FROM patients");
                addPatientStatement=conn.prepareStatement("INSERT INTO patients VALUES (?,?,?,?,?,?,?,?,?,?,?)");

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

    public void addPatient(Patient patient) {
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
            addPatientStatement.setInt(11, 1);
            addPatientStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Patient> patients() {
        ArrayList<Patient> p=new ArrayList<>();
        try {
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
        if (s=="RETIREE")
                status=Status.RETIREE;
        else if (s=="EMPLOYEE")
            status=Status.EMPLOYEE;
        else if (s=="OTHER")
            status=Status.OTHER;
        try {
            if (rs.getString(5)=="ZENSKO")
                gender=Gender.ZENSKO;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern("DD-MM-YYYY");
        try {
            patient = new Patient(rs.getInt(1), rs.getString(2), rs.getString(3),rs.getString(4),gender, LocalDate.parse(rs.getString(6),df),rs.getString(7), rs.getString(8),status, rs.getString(10));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //patient.setDrzava(dajDrzavu(rs.getInt(4), grad));
        return patient;
    }

}

