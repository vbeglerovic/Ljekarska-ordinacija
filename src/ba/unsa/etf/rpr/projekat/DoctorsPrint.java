package ba.unsa.etf.rpr.projekat;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class DoctorsPrint implements Runnable {
    private DAO dao;
    private Office office;
    public DoctorsPrint(Office office) {
        dao=DAO.getInstance();
        this.office=office;
    }

    @Override
    public void run() {
        PrintWriter out;
        ArrayList<Doctor> doctors=new ArrayList<>();
        doctors=dao.doctors(office.getId());
        try {
            out = new PrintWriter(new FileWriter("Doctors.txt"));
        } catch(IOException e) {
            return;
        }
        try{
            for (Doctor doctor:doctors)
                out.println(doctor.toString());
        } catch(Exception e) {

        } finally {
            out.close();
        }
    }
}
