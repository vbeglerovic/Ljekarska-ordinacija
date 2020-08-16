package ba.unsa.etf.rpr;


import javafx.collections.FXCollections;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class Appointment {
    Integer id;
    LocalDate date;
    LocalTime time;
    Patient patient;
    Doctor doctor;
    boolean kontrola;
    String report;

    public Appointment() {
    }

    public Appointment(Integer id, LocalDate date, LocalTime time, Patient patient, Doctor doctor, boolean kontrola, String report) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.patient = patient;
        this.doctor = doctor;
        this.kontrola = kontrola;
        this.report = report;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
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

    public boolean isKontrola() {
        return kontrola;
    }

    public void setKontrola(boolean kontrola) {
        this.kontrola = kontrola;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }
}