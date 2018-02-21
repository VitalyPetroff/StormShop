package sample;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    public String server;
    private SessionService service = new SessionService();
    private ObjectMapper mapper = new ObjectMapper();
    private Logger LOGGER = LoggerFactory.getLogger(Controller.class);
    private String token;

    public Controller(String server) {
        this.server = server;
    }

    public ArrayList<Good> getAll(){
        StringBuffer goods = service.sendGetRequest(server, "getAllGoods");
        ArrayList<Good> goodsInShop = new ArrayList<>();
        try {
            goodsInShop = new ObjectMapper().readValue(goods.toString(),
                    new TypeReference<ArrayList<Good>>() {});
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return goodsInShop;
    }

    public String authorization(String login, String password) {
        Account account = new Account(login, password);
        String strAccount = "";
        try {
            strAccount = mapper.writeValueAsString(account);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage(), e);
        }

        String request = server.concat("/authorization");
        token = (service.sendPostRequest(request, strAccount)).toString();
        System.out.println(token);  // удалить строку
        return token;
    }

    public String add() {
        String request = server.concat("/add");
        StringBuffer result = service.sendPostRequest(request, "Hello World!", token);
        System.out.println(result.toString()); // удалить строку
        return token;
    }
}