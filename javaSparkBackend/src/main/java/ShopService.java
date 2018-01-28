import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;

public class ShopService {

    private static final Logger LOGGER = LoggerFactory.getLogger(Good.class);
    private ObjectMapper mapper = new ObjectMapper();
    private String dataBasePath = "dataBase.json";
    private ShopDao dao = new ShopDao(dataBasePath);

    public String getAll() {
        ArrayList<Good> goods = dao.getAll();
        String listToJson = null;
        try {
            listToJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(goods);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return listToJson;
    }

    public void addGoods(ArrayList<Good> newGoods) {
        ArrayList<Good> goodsInShop = dao.getAll();
        for (Good newGood : newGoods) {
            boolean isAvailable = false;
            for (Good goodInShop : goodsInShop) {
                if (newGood.name.equals(goodInShop.name)) {
                    goodInShop.count += newGood.count;
                    goodInShop.price = newGood.price;
                    isAvailable = true;
                    break;
                }
            }
            if (!isAvailable) {
                goodsInShop.add(newGood);
            }
        }
        dao.saveAll(goodsInShop);
    }

    public void buyGoods(ArrayList<Good> goods) {
        for (Good good : goods) {
            Good goodInShop = dao.findByName(good.name);
            if (!(goodInShop == null)) {
                goodInShop.count -= good.count;
                if (goodInShop.count < 0) {
                    goodInShop.count = 0;
                }
                dao.saveOrUpdate(goodInShop);
            } else {
                LOGGER.info("Product is not in the store !");
            }
        }
    }
}