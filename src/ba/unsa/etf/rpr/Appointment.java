package ba.unsa.etf.rpr;


import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class Appointment {
   /* private SimpleIntegerProperty id;
    private SimpleObjectProperty<LocalDate> date;
    private SimpleObjectProperty<LocalTime> time;
    private SimpleObjectProperty<Patient> patient;
    private SimpleObjectProperty<Doctor> doctor;
    private SimpleStringProperty type;
    private SimpleStringProperty report;*/
    Integer id;
    LocalDate date;
    LocalTime time;
    Patient patient;
    Doctor doctor;
    String type;
    String report;

   /* public Appointment(SimpleIntegerProperty id, SimpleObjectProperty<LocalDate> date, SimpleObjectProperty<LocalTime> time, SimpleObjectProperty<Patient> patient, SimpleObjectProperty<Doctor> doctor, SimpleStringProperty type, SimpleStringProperty report) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.patient = patient;
        this.doctor = doctor;
        this.type = type;
        this.report = report;
    }
    public Appointment (Integer id, LocalDate date, LocalTime time, Patient patient, Doctor doctor, String type, String report) {
        this.id=new SimpleIntegerProperty(id);
        this.date=new SimpleObjectProperty<>(date);
        this.time=new SimpleObjectProperty<>(time);
        this.patient=new SimpleObjectProperty<>(patient);
        this.doctor=new SimpleObjectProperty<>(doctor);
        this.type=new SimpleStringProperty(type);
        this.report=new SimpleStringProperty(report);
    }

    public Appointment() {
        this.id=new SimpleIntegerProperty();
        this.date=new SimpleObjectProperty<>();
        this.time=new SimpleObjectProperty<>();
        this.patient=new SimpleObjectProperty<>();
        this.doctor=new SimpleObjectProperty<>();
        this.type=new SimpleStringProperty();
        this.report=new SimpleStringProperty();
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public LocalDate getDate() {
        return date.get();
    }

    public SimpleObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public LocalTime getTime() {
        return time.get();
    }

    public SimpleObjectProperty<LocalTime> timeProperty() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time.set(time);
    }

    public Patient getPatient() {
        return patient.get();
    }

    public SimpleObjectProperty<Patient> patientProperty() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient.set(patient);
    }

    public Doctor getDoctor() {
        return doctor.get();
    }

    public SimpleObjectProperty<Doctor> doctorProperty() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor.set(doctor);
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getReport() {
        return report.get();
    }

    public SimpleStringProperty reportProperty() {
        return report;
    }

    public void setReport(String report) {
        this.report.set(report);
    }*/

    public Appointment() {
    }

    public Appointment(Integer id, LocalDate date, LocalTime time, Patient patient, Doctor doctor, String type, String report) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.patient = patient;
        this.doctor = doctor;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }
}