import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShopDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(Good.class);
    private String goodsFilePath;
    private String accountsFilePath;
    private ObjectMapper mapper = new ObjectMapper();

    ShopDao(String goodsFilePath, String accountsFilePath) {
        this.goodsFilePath = goodsFilePath;
        this.accountsFilePath = accountsFilePath;
    }

    public ArrayList<Good> getAllGoods() {
        ArrayList<Good> listOfGoods;
        try {
            listOfGoods = mapper.readValue(new File(goodsFilePath), new TypeReference<ArrayList<Good>>() {});
        } catch (IOException e) {
            listOfGoods = new ArrayList<>();
        }
        return listOfGoods;
    }

    public Good findByName(String name) {
        ArrayList<Good> listOfGoods = getAllGoods();
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
        ArrayList<Good> listOfGoods = getAllGoods();
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
        ArrayList<Good> listOfGoods = getAllGoods();
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
            mapper.writeValue(new File(goodsFilePath), goods);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    synchronized public void getAccounts() {
        List<Account> listOfAccounts;
        try {
            listOfAccounts = mapper.readValue(new File(accountsFilePath), new TypeReference<ArrayList<Account>>() {});
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }

    }
}