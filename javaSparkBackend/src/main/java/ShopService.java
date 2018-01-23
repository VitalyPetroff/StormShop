import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;

public class ShopService {

    private static final Logger LOGGER = LoggerFactory.getLogger(Good.class);
    private ObjectMapper mapper = new ObjectMapper();
    private ArrayList<Good> goods;
    private String dataBasePath = "dataBase.json";
    private ShopDao dao = new ShopDao(dataBasePath);

    public String getAll() {
        goods = dao.getAll();
        String listToJson = null;
        try {
            listToJson = mapper.writeValueAsString(goods);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return listToJson;
    }

    public void addGoods(ArrayList<Good> goods) {
        ArrayList<Good> goodsInShop = dao.getAll();
        ArrayList<String> resultOfAdding = new ArrayList<String>();
        for (Good good : goods) {
            Good goodInShop = dao.findByName(good.name);
            if (goodInShop.equals(null)) {

            }
        }
    }

//    buyGoods();

}
