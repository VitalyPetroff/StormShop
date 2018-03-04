package sample;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;

public class Controller {
    private SessionService sessionService = new SessionService();
    private ObjectMapper mapper = new ObjectMapper();
    private Logger LOGGER = LoggerFactory.getLogger(Controller.class);
    private String token;

    private ArrayList<Good> goodsInShop = new ArrayList<>();
    private ArrayList<Good> goodsInCart = new ArrayList<>();
    private int totalCount;
    private int totalPrice;

    public ArrayList<Good> getGoodsInShop(String server) throws IOException {
        StringBuffer request = sessionService.sendGetRequest(server, "getAllGoods");
        if (!request.toString().equals("Error")) {
            try {
                goodsInShop = new ObjectMapper().readValue(request.toString(),
                        new TypeReference<ArrayList<Good>>() {
                        });
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
            return goodsInShop;
        } else {
            throw new IOException("The address is invalid or the connection is missing!");
        }
    }

    public ArrayList<Good> getGoodsInCart() {
        return goodsInCart;
    }

    public void addGoodToCart(Good newGood) {
        boolean isInCart = false;
        for (Good good : goodsInCart) {
            if (good.name.equals(newGood.name)) {
                good.count++;
                isInCart = true;
                break;
            }
        }
        if (!isInCart) {
            newGood.count = 1;
            goodsInCart.add(newGood);
        }
        updateTotalInfo();
    }

    public void removeGoodFromCart(Good good) {
        goodsInCart.remove(good);
    }

    public void increaseCount(Good good) {
        int index = goodsInCart.indexOf(good);
        goodsInCart.get(index).count++;
        updateTotalInfo();
    }

    public void decreaseCount(Good good) {
        int index = goodsInCart.indexOf(good);
        goodsInCart.get(index).count--;
        if (goodsInCart.get(index).count <= 0) {
            goodsInCart.get(index).count = 0;
        }
        updateTotalInfo();
    }

    private void updateTotalInfo() {
        totalCount = 0;
        totalPrice = 0;
        for (Good good : goodsInCart) {
            totalCount += good.count;
            totalPrice += good.count * good.price;
        }
    }

    public void buy(String server) throws IOException {
        String goodsToJson = "";
        try {
            goodsToJson = mapper.writeValueAsString(goodsInCart);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        String result = sessionService.sendPostRequest(server, "buy", goodsToJson).toString();
        if (result.equals("OK")) {
            goodsInCart.clear();
        } else {
            throw new IOException(result);
        }
    }

    public String authorization(String server, String login, String password) {
        Account account = new Account(login, password);
        String strAccount = "";
        try {
            strAccount = mapper.writeValueAsString(account);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage(), e);
        }
        token = (sessionService.sendPostRequest(server, "authorization", strAccount)).toString();
        return token;
    }

    public String addNewGood(String server, Good good) {
        String goodToJson = "";
        try {
            goodToJson = mapper.writeValueAsString(good);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        String result = sessionService.sendPostRequest(server, "add", goodToJson, token).toString();
        if (result.equals("The goods in the store have been updated.")){
            for (Good goodInCart : goodsInCart) {
                if (goodInCart.name.equals(good.name)) {
                    goodInCart.price = good.price;
                    break;
                }
            }
        }
        return result;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}