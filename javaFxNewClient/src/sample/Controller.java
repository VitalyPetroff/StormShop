package sample;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Controller {
    public String server = "http://localhost:4567";
    private SessionService service = new SessionService();
    private ObjectMapper mapper = new ObjectMapper();
    private Logger LOGGER = LoggerFactory.getLogger(Controller.class);
    private String token;

    public String authorization(String login, String password) {
        Account account = new Account(login, password);
        String strAccount = "";
        try {
            strAccount = mapper.writeValueAsString(account);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage(), e);
        }

        String request = server.concat("/authorization");
        token = (service.sendRequest(request, strAccount)).toString();
        System.out.println(token);  // удалить строку
        return token;
    }

    public String add() {
        String request = server.concat("/add");
        StringBuffer result = service.sendRequest(request, "Hello World!", token);
        System.out.println(result.toString()); // удалить строку
        return token;
    }
}