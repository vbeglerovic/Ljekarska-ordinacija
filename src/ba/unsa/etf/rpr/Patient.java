package ba.unsa.etf.rpr;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

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

    public Patient() {
    }

    public Patient(int id, String firstName, String lastName, String JMBG, Gender gender, LocalDate birthDate, String birthPlace, String address, Status status, String email) {
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
    /*SimpleIntegerProperty id;
    SimpleStringProperty firstName;
    SimpleStringProperty lastName;
    SimpleStringProperty JMBG;
    SimpleObjectProperty<Gender> gender;
    SimpleObjectProperty<LocalDate> birthDate;
    SimpleStringProperty birthPlace;
    SimpleStringProperty address;
    SimpleObjectProperty<Status> status;
    SimpleStringProperty email;
    SimpleIntegerProperty officeId;

    public Patient() {
    }
    public Patient(Integer id, String firstName, String lastName, String JMBG, Gender gender, LocalDate birthDate, String birthPlace, String address, Status status, String email, Integer officeId) {
        this.id = new SimpleIntegerProperty(id);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.JMBG = new SimpleStringProperty(JMBG);
        this.gender = new SimpleObjectProperty<>(gender);
        this.birthDate = new SimpleObjectProperty<>(birthDate);
        this.birthPlace = new SimpleStringProperty(birthPlace);
        this.address = new SimpleStringProperty(address);
        this.status = new SimpleObjectProperty<>(status);
        this.email = new SimpleStringProperty(email);;
        this.officeId = new SimpleIntegerProperty(officeId);
    }

    public Patient(SimpleIntegerProperty id, SimpleStringProperty firstName, SimpleStringProperty lastName, SimpleStringProperty JMBG, SimpleObjectProperty<Gender> gender, SimpleObjectProperty<LocalDate> birthDate, SimpleStringProperty birthPlace, SimpleStringProperty address, SimpleObjectProperty<Status> status, SimpleStringProperty email, SimpleIntegerProperty officeId) {
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
        this.officeId = officeId;
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

    public String getFirstName() {
        return firstName.get();
    }

    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public SimpleStringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getJMBG() {
        return JMBG.get();
    }

    public SimpleStringProperty JMBGProperty() {
        return JMBG;
    }

    public void setJMBG(String JMBG) {
        this.JMBG.set(JMBG);
    }

    public Gender getGender() {
        return gender.get();
    }

    public SimpleObjectProperty<Gender> genderProperty() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender.set(gender);
    }

    public LocalDate getBirthDate() {
        return birthDate.get();
    }

    public SimpleObjectProperty<LocalDate> birthDateProperty() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate.set(birthDate);
    }

    public String getBirthPlace() {
        return birthPlace.get();
    }

    public SimpleStringProperty birthPlaceProperty() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace.set(birthPlace);
    }

    public String getAddress() {
        return address.get();
    }

    public SimpleStringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public Status getStatus() {
        return status.get();
    }

    public SimpleObjectProperty<Status> statusProperty() {
        return status;
    }

    public void setStatus(Status status) {
        this.status.set(status);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public int getOfficeId() {
        return officeId.get();
    }

    public SimpleIntegerProperty officeIdProperty() {
        return officeId;
    }

    public void setOfficeId(int officeId) {
        this.officeId.set(officeId);
    }

    @Override
    public String toString() {
        return firstName.toString() + " "+
               lastName.toString();
    }*/
}
