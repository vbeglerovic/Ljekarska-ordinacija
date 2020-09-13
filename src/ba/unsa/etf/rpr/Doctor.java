package ba.unsa.etf.rpr;

import java.time.LocalDate;
import java.util.Objects;

public class Doctor {
    private int id;
    private String firstName;
    private String lastName;
    private String JMBG;
    private LocalDate birthDate;
    private String birthPlace;
    private String address;
    private String email;
    private LocalDate employmentDate;
    private String specialization;
    private boolean active;

    public Doctor() {
        active=true;
    }

    public Doctor(int id, String firstName, String lastName, String JMBG, LocalDate birthDate, String birthPlace, String address, String email, LocalDate employmentDate, String specialization,boolean active) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.JMBG = JMBG;
        this.birthDate = birthDate;
        this.birthPlace = birthPlace;
        this.address = address;
        this.email = email;
        this.employmentDate = employmentDate;
        this.specialization = specialization;
        this.active=active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getJMBG() {
        return JMBG;
    }

    public void setJMBG(String JMBG) {
        this.JMBG = JMBG;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getEmploymentDate() {
        return employmentDate;
    }

    public void setEmploymentDate(LocalDate employmentDate) {
        this.employmentDate = employmentDate;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return firstName+" "+lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Doctor doctor = (Doctor) o;
        return id==doctor.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
