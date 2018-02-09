import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static spark.Spark.*;

public class Controller {
    public static final Logger LOGGER = LoggerFactory.getLogger(Good.class);

    public static void main(String[] args) {

        ArrayList<Good> initListOfGoods = null;
        try {
            initListOfGoods = new ObjectMapper().readValue(new File("dataInit.json"),
                    new TypeReference<ArrayList<Good>>() {
            });
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }

        ShopService service = new ShopService();
        service.addGoods(initListOfGoods);

        staticFileLocation("/");
        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");

            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }
            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");

            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
            return "OK";
        });
        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

        get("/getAll", (request, response) -> service.getAll());

        post("/add", (request, response) -> {
            String goodsToStore = request.body();
            ArrayList<Good> newGoods = new ObjectMapper().readValue(goodsToStore,
                    new TypeReference<ArrayList<Good>>() {
            });
            service.addGoods(newGoods);
            return "Товар добавлен успешно !";
        });

        post("/buy", (request, response) -> {
            String goodsToBuy = request.body();
            ArrayList<Good> listToBuy = new ObjectMapper().readValue(goodsToBuy,
                    new TypeReference<ArrayList<Good>>() {
            });
            try {
                service.buyGoods(listToBuy);
            } catch (IllegalArgumentException | NullPointerException er) {
                LOGGER.info(er.getMessage(), er);
                response.status(400);
                return er.getMessage();
            }
            return "It's OK!";
        });
    }
}