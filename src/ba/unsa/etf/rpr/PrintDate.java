package ba.unsa.etf.rpr;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class PrintDate {
    public String print () throws IOException {
        URL url = new URL("http://www.google.com");
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        long date = httpCon.getDate();
        return String.valueOf(new Date(date));
    }
}
