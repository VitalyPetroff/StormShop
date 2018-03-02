import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;

public class ShopService {

    final private String goodsFilePath = "dataBase.json";
    private static final Logger LOGGER = LoggerFactory.getLogger(Good.class);
    private ObjectMapper mapper = new ObjectMapper();
    private ShopDao dao = new ShopDao(goodsFilePath);

    public String getAllGoods() {
        ArrayList<Good> goods = dao.getAllGoods();
        String listToJson = null;
        try {
            listToJson = mapper.writeValueAsString(goods);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return listToJson;
    }

    public String addGood(Good good) {
        String result = "FAILED";
        ArrayList<Good> goodsInShop = dao.getAllGoods();
        boolean isAvailable = false;
        for (Good goodInShop : goodsInShop) {
            if (good.name.equals(goodInShop.name)) {
                goodInShop.count += good.count;
                goodInShop.price = good.price;
                isAvailable = true;
                result = "The goods in the store have been updated.";
                break;
            }
        }
        if (!isAvailable) {
            goodsInShop.add(good);
            result = "The product was added to the store.";
        }
        dao.saveAll(goodsInShop);
        return result;
    }

    public String buyGoods(ArrayList<Good> goods) {
        String result = "OK";
        boolean testGoods = true;
        for (Good good : goods) {
            Good goodInShop = dao.findByName(good.name);
            if (goodInShop == null) {
                result = good.name.concat(" is end in shop");
                testGoods = false;
                break;
            } else if (goodInShop.count < good.count) {
                result = "Quantity of ".concat(good.name).concat(" is less than requested");
                testGoods = false;
                break;
            }
        }
        if (testGoods) {
            for (Good good : goods) {
                Good goodInShop = dao.findByName(good.name);
                goodInShop.count -= good.count;
                dao.saveOrUpdate(goodInShop);
            }
        }
        return result;
    }
}