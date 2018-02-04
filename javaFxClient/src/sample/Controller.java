package sample;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class Controller {

    private final String USER_AGENT = "Mozilla/5.0";
    ArrayList<Good> goods = new ArrayList<>();

    public ArrayList<Good> getAll() throws IOException {
        String request = "http://localhost:4567/getAll";

        URL url = new URL(request);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        ArrayList<Good> goods = new ObjectMapper().readValue(response.toString(),
                new TypeReference<ArrayList<Good>>() {
        });
        return goods;
    }

}