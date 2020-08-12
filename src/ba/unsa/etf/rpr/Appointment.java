package ba.unsa.etf.rpr;


import java.util.Date;

public class Appointment {
    Date date;
    String time;
    Patient patient;
    Doctor doctor;
    Boolean kontrola;
    String report;

    public Appointment() {
    }

    public Appointment(Date date, String time, Patient patient, Doctor doctor, Boolean kontrola, String report) {
        this.date = date;
        this.time = time;
        this.patient = patient;
        this.doctor = doctor;
        this.kontrola = kontrola;
        this.report = report;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Boolean getKontrola() {
        return kontrola;
    }

    public void setKontrola(Boolean kontrola) {
        this.kontrola = kontrola;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }
}
