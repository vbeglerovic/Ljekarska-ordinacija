package ba.unsa.etf.rpr;

public class AppointmentsController {
    private DAO dao;
    private Office office;

    public AppointmentsController() {
        dao=DAO.getInstance();
    }
}
