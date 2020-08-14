package ba.unsa.etf.rpr;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class Patient {
    Integer id;
    String firstName;
    String lastName;
    String JMBG;
    Gender gender;
    LocalDate birthDate;
    String birthPlace;
    String address;
    Status status;
    String email;
    Integer officeId;

    public Patient() {
    }

    public Patient(Integer id, String firstName, String lastName, String JMBG, Gender gender, LocalDate birthDate, String birthPlace, String address, Status status, String email, Integer officeId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.JMBG = JMBG;
        this.gender = gender;
        this.birthDate = birthDate;
        this.birthPlace = birthPlace;
        this.address = address;
        this.status = status;
        this.email = email;
        this.officeId=officeId;
    }

    public Integer getOfficeId() {
        return officeId;
    }

    public void setOfficeId(Integer officeId) {
        this.officeId = officeId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return firstName+" "+lastName;
    }
}
