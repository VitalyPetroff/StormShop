package sample;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;

public class Controller {
    private SessionService sessionService = new SessionService();
    private ObjectMapper mapper = new ObjectMapper();
    private Logger LOGGER = LoggerFactory.getLogger(Controller.class);
    private String token;

    public ArrayList<Good> getAll(String server) throws IOException {
        StringBuffer request = sessionService.sendGetRequest(server, "getAllGoods");
        if (!request.toString().equals("Error")) {
            ArrayList<Good> goodsInShop = new ArrayList<>();
            try {
                goodsInShop = new ObjectMapper().readValue(request.toString(),
                        new TypeReference<ArrayList<Good>>() {
                        });
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
            return goodsInShop;
        } else {
            throw new IOException("The address is invalid or the connection is missing!");
        }
    }

    public String buy(String server, ArrayList<Good> goods) {
        String goodsToJson = "";
        try {
            goodsToJson = mapper.writeValueAsString(goods);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        String result = sessionService.sendPostRequest(server, "buy", goodsToJson).toString();
        return result;
    }

    public String authorization(String server, String login, String password) {
        Account account = new Account(login, password);
        String strAccount = "";
        try {
            strAccount = mapper.writeValueAsString(account);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage(), e);
        }
        token = (sessionService.sendPostRequest(server, "authorization", strAccount)).toString();
        return token;
    }

    public String add(String server, Good good) {
        String goodToJson = "";
        try {
            goodToJson = mapper.writeValueAsString(good);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        String result = sessionService.sendPostRequest(server, "add", goodToJson, token).toString();
        return result;
    }
}