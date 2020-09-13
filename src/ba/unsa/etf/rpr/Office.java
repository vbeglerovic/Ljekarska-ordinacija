package ba.unsa.etf.rpr;

public class Office {
    private int id;
    private String name;
    private String address;
    private String username;
    private String password;

    public Office() {
    }

    public Office(int id, String name, String address, String username, String password) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {return name;}

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
