package ba.unsa.etf.rpr;

public class OfficeWithThisUsernameAlreadyExist extends Exception {
    public OfficeWithThisUsernameAlreadyExist (String text) {
        super(text);
    }
}