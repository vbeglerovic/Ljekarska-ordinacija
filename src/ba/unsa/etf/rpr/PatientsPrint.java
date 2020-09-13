package ba.unsa.etf.rpr;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class PatientsPrint implements Runnable {
    private DAO dao;
    private Office office;

    public PatientsPrint(Office office) {
        dao=DAO.getInstance();
        this.office=office;
    }

    @Override
    public void run() {
        PrintWriter out;
        ArrayList<Patient> patients=new ArrayList<>();
        patients=dao.patients(office.getId());
        try {
            out = new PrintWriter(new FileWriter("Patients.txt"));
        } catch(IOException e) {
            return;
        }
        try{
            for (Patient patient:patients)
                out.println(patient.toString());
        } catch(Exception e) {

        } finally {
            out.close();
        }
    }

}
