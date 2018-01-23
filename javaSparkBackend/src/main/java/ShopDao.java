import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShopDao {

    //    private static final Logger LOGGER = LoggerFactory.getLogger(Shop.class);
    public String filePath;
    private ObjectMapper mapper = new ObjectMapper();

    ShopDao(String filePath) {
        this.filePath = filePath;
    }

    public ArrayList<Good> getAll() {
        ArrayList<Good> listOfGoods;
        try {
            listOfGoods = mapper.readValue(new File(filePath), new TypeReference<List<Good>>() {
            });
        } catch (IOException e) {
            listOfGoods = new ArrayList<Good>();
        }
        return listOfGoods;
    }

    public int findByName(String name) {
        ArrayList<Good> listOfGoods = getAll();
        int index = 0;
        for (Good good : listOfGoods) {
            if (good.name.equals(name)) {
                index = listOfGoods.indexOf(good);
                break;
            }
        }
        return index;
    }

    public void save(Good good) {
        try {
            mapper.writeValue(new File(filePath), new TypeReference<List<Good>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteByName(String name) {

    }
}