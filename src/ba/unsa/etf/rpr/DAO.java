package ba.unsa.etf.rpr;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

public class DAO {
    private static DAO instance;
    private Connection conn;

   private PreparedStatement addOfficeStatement, getMaxIdOfficeStatement;

    private DAO () {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:database.db");
            try {
                addOfficeStatement = conn.prepareStatement("INSERT INTO offices VALUES (?,?,?,?,?)");
            } catch(SQLException e) {
                regenerateDatabase();
                try {
                    addOfficeStatement = conn.prepareStatement("INSERT INTO offices VALUES (?,?,?,?,?)");
                    //getMaxIdOfficeStatement=conn.prepareStatement("SELECT MAX(id) FROM offices");
                    //dajStudenteStatement = conn.prepareStatement("SELECT id, ime, prezime, brojindexa FROM studenti");
                    //dajNoviId = conn.prepareStatement("SELECT MAX(id)+1 FROM studenti");
                    //dodajStudenta = conn.prepareStatement("INSERT INTO studenti VALUES (?,?,?,?)");
                    /*dajStudenteNaPredmetu = conn.prepareStatement("SELECT studenti.id, studenti.ime, studenti.prezime, studenti.brojindexa FROM studenti, student_predmet sp, predmeti WHERE " +
                            "sp.id_predmeta=predmeti.id AND sp.id_studenta=studenti.id AND predmeti.naziv=?");
                    dodajNoviPredmet = conn.prepareStatement("INSERT INTO predmeti VALUES (?,?)");
                    dajNoviIdPredmeti = conn.prepareStatement("SELECT MAX(id)+1 FROM predmeti");
                    dajPredmete = conn.prepareStatement("SELECT * FROM predmeti");*/
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
                addOfficeStatement.setString(2,office.getName());
                addOfficeStatement.setString(3,office.getAddress());
                addOfficeStatement.setString(4,office.getUsername());
                addOfficeStatement.setString(5,office.getPassword());
                addOfficeStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

}
