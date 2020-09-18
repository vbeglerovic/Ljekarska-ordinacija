package ba.unsa.etf.rpr.projekat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;
import java.util.Objects;

public class Appointment implements Comparable<Appointment> {
    private Integer id;
    private LocalDate date;
    private LocalTime time;
    private Patient patient;
    private Doctor doctor;
    private boolean firstAppointment;
    private String anamnesis;
    private String diagnosis;
    private String recommendation;

    public Appointment() {
        this.firstAppointment=false;
    }

    public Appointment(Integer id, LocalDate date, LocalTime time, Patient patient, Doctor doctor, boolean firstAppointment, String anamnesis, String diagnosis, String recommendation) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.patient = patient;
        this.doctor = doctor;
        this.firstAppointment=firstAppointment;
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

    public boolean isFirstAppointment() {
        return firstAppointment;
    }

    public void setFirstAppointment(boolean firstAppointment) {
        this.firstAppointment = firstAppointment;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return Objects.equals(id, that.id);
    }

    public String firstAppointment() {
        if (isFirstAppointment()) {
            if (Locale.getDefault().equals(new Locale("bs", "BA"))) return "Da";
            return "Yes";
        }
        return "";
    }
    @Override
    public int compareTo(Appointment o) {
        LocalDateTime ld1=LocalDateTime.of(getDate(), getTime());
        LocalDateTime ld2=LocalDateTime.of(o.getDate(), o.getTime());
        if (ld1.isBefore(ld2)) return -1;
        return 1;
    }

}