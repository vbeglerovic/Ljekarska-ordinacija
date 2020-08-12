package ba.unsa.etf.rpr;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

public class DAO {
    private static DAO instance;
    private Connection conn;

   private PreparedStatement addOfficeStatement, getOfficesStatement, getOfficeWithUsernameStatement,getPasswordFromOfficeStatement;

    private DAO () {
        try {
            conn= DriverManager.getConnection("jdbc:sqlite:database.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
            try {
                addOfficeStatement=conn.prepareStatement("INSERT INTO offices VALUES (?,?,?,?,?)");
            //} catch(SQLException e) {
                //regenerateDatabase();
                //try {
                    getOfficesStatement=conn.prepareStatement("SELECT * FROM offices");
                    addOfficeStatement=conn.prepareStatement("INSERT INTO offices VALUES (?,?,?,?,?)");
                    getOfficeWithUsernameStatement=conn.prepareStatement("SELECT * FROM offices WHERE username=?");
                    getPasswordFromOfficeStatement=conn.prepareStatement("SELECT password FROM offices WHERE id=?");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
           // }
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
}
