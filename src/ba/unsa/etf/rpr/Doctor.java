package ba.unsa.etf.rpr;

import java.time.LocalDate;

public class Doctor extends Person {
    private LocalDate employmentDate;
    private String specialization;

    public Doctor() {
        super();
    }

    public Doctor(int id, String firstName, String lastName, String JMBG, LocalDate birthDate, String birthPlace, String address, String email, LocalDate employmentDate, String specialization,boolean active) {
        super(id, firstName, lastName, JMBG,birthDate, birthPlace, address, email, active);
        this.employmentDate = employmentDate;
        this.specialization = specialization;
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
}
