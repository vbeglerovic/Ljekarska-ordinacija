package ba.unsa.etf.rpr;


import java.time.LocalDate;

public class Patient extends Person {
    private Gender gender;
    private Status status;

    public Patient() {
        super();
    }

    public Patient(int id, String firstName, String lastName, String JMBG, Gender gender, LocalDate birthDate, String birthPlace, String address, Status status, String email,boolean active) {
        super(id, firstName, lastName, JMBG, birthDate, birthPlace, address, email, active);
        this.gender = gender;
        this.status=status;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}