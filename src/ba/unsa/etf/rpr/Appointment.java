package ba.unsa.etf.rpr;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    private Integer id;
    private LocalDate date;
    private LocalTime time;
    private Patient patient;
    private Doctor doctor;
    private String type;
    private String anamnesis;
    private String diagnosis;
    private String recommendation;

    public Appointment() {
    }

    public Appointment(Integer id, LocalDate date, LocalTime time, Patient patient, Doctor doctor, String type, String anamnesis, String diagnosis, String recommendation) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.patient = patient;
        this.doctor = doctor;
        this.type = type;
        this.anamnesis = anamnesis;
        this.diagnosis=diagnosis;
        this.recommendation=recommendation;
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


    public String getAnamnesis() {
        return anamnesis;
    }

    public void setAnamnesis(String anamnesis) {
        this.anamnesis = anamnesis;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }
}