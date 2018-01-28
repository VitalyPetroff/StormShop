import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ShopDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(Good.class);
    private String filePath;
    private ObjectMapper mapper = new ObjectMapper();

    ShopDao(String filePath) {
        this.filePath = filePath;
    }

    public ArrayList<Good> getAll() {
        ArrayList<Good> listOfGoods;
        try {
            listOfGoods = mapper.readValue(new File(filePath), new TypeReference<ArrayList<Good>>() {});
        } catch (IOException e) {
            listOfGoods = new ArrayList<>();
        }
        return listOfGoods;
    }

    public Good findByName(String name) {
        ArrayList<Good> listOfGoods = getAll();
        Good result = null;
        for (Good good : listOfGoods) {
            if (good.name.equals(name)) {
                result = good;
                break;
            }
        }
        return result;
    }

    synchronized public void saveOrUpdate(Good good) {
        ArrayList<Good> listOfGoods = getAll();
        Boolean isAvailable = false;
        for (Good goodInShop : listOfGoods) {
            if (goodInShop.name.equals(good.name)) {
                goodInShop.count = good.count;
                goodInShop.price = good.price;
                isAvailable = true;
                break;
            }
        }
        if (!isAvailable) {
            listOfGoods.add(good);
        }
        saveAll(listOfGoods);
    }

    synchronized public void deleteByName(String name) {
        ArrayList<Good> listOfGoods = getAll();
        for (Good good : listOfGoods) {
            if (good.name.equals(name)) {
                listOfGoods.remove(listOfGoods.indexOf(good));
                break;
            }
        }
        saveAll(listOfGoods);
    }

    synchronized public void saveAll(ArrayList<Good> goods) {
        try {
            mapper.writeValue(new File(filePath), goods);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}