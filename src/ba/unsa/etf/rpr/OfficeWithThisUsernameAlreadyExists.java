package ba.unsa.etf.rpr;

public class OfficeWithThisUsernameAlreadyExists extends Exception {
    public OfficeWithThisUsernameAlreadyExists(String text) {
        super(text);
    }
}