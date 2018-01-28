import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static spark.Spark.get;
import static spark.Spark.post;

public class Controller {
    public static final Logger LOGGER = LoggerFactory.getLogger(Good.class);
    public static void main(String[] args) {

        ArrayList<Good> initListOfGoods = null;
        try {
            initListOfGoods = new ObjectMapper().readValue(new File("dataInit.json"), new TypeReference<ArrayList<Good>>() {});
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }

        ShopService service = new ShopService();
        service.addGoods(initListOfGoods);

        get("/get", (request, response) -> {
            String result = service.getAll();
            return result;
        });

        post("/add", (request, response) -> {
            String goodsToStore = request.body();
            ArrayList<Good> newGoods = new ObjectMapper().readValue(goodsToStore, new TypeReference<ArrayList<Good>>() {
            });
            service.addGoods(newGoods);
            return "Adding is OK !";
        });

        post("/buy", (request, response) -> {
            String goodsToBuy = request.body();
            ArrayList<Good> listToBuy = new ObjectMapper().readValue(goodsToBuy, new TypeReference<ArrayList<Good>>(){});
            service.buyGoods(listToBuy);
            return "Buying is OK !";
        });
    }
}