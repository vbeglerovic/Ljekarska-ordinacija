package ba.unsa.etf.rpr.projekat;

public class OfficeWithThisUsernameAlreadyExists extends Exception {
    public OfficeWithThisUsernameAlreadyExists(String text) {
        super(text);
    }
}