package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application {

    @Override
    public void start(Stage window) throws Exception {

        final Logger LOGGER = LoggerFactory.getLogger(Good.class);
        window.setX(700);
        window.setY(10);

        ScrollPane mainPane = new ScrollPane();
        mainPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        GridPane gridPane = new GridPane();
        gridPane.add(new Label(" Наименование "), 0, 0);
        gridPane.add(new Label(" Цена, $"), 1, 0);
        gridPane.add(new Label(" Кол-во в\nмагазине "), 2, 0);
        gridPane.add(new Label(" Кол-во в\nкорзине "), 4, 0);
        gridPane.add(new Label(" Стоимость "), 5, 0);

        Controller service = new Controller();
        ArrayList<Good> goodsInShop = null;
        try {
            goodsInShop = service.getAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        int countOfGoods = goodsInShop.size();
        gridPane.getCellBounds(5, countOfGoods + 3);
        Label totalCost = new Label();
        Label totalCount = new Label();
        gridPane.add(totalCount, 4, countOfGoods + 2);
        gridPane.add(totalCost, 5, countOfGoods + 2);
        gridPane.add(new Label("КОЛ-ВО"), 4, countOfGoods + 1);
        gridPane.add(new Label("СУММА"), 5, countOfGoods + 1);
        Button butBuy = new Button();
        butBuy.setText("ОПЛАТА");
        butBuy.setMinWidth(65);
        
        Label[] name = new Label[countOfGoods];
        Label[] price = new Label[countOfGoods];
        Label[] countInShop = new Label[countOfGoods];
        Button[] butPlus = new Button[countOfGoods];
        Button[] butMinus = new Button[countOfGoods];
        Label[] countInCart = new Label[countOfGoods];
        Label[] cost = new Label[countOfGoods];
        
        for (int i = 0; i < countOfGoods; i++) {
            Good good = goodsInShop.get(i);
            int row = i + 1;
            name[i] = new Label(good.name);
            price[i] = new Label(String.valueOf(good.price));
            countInShop[i] = new Label(String.valueOf(good.count));
            countInCart[i] = new Label();
            cost[i] = new Label();
            gridPane.add(name[i], 0, row);
            gridPane.add(price[i], 1, row);
            gridPane.add(countInShop[i], 2, row);
            gridPane.add(countInCart[i], 4, row);
            gridPane.add(cost[i], 5, row);
            butPlus[i] = new Button();
            butPlus[i].setText("+");
            butPlus[i].setMinWidth(65);

            butMinus[i] = new Button();
            butMinus[i].setText("-");
            butMinus[i].setMinWidth(65);

            VBox boxButton = new VBox();
            boxButton.getChildren().addAll(butPlus[i], butMinus[i]);
            gridPane.add(boxButton, 3, row);
        }

        for (int i = 0; i < countOfGoods; i++) {
            int index = i;
            butPlus[i].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (Integer.parseInt(countInShop[index].getText()) > 0) {
                        int numInCart = 0;
                        if (!countInCart[index].getText().isEmpty()) {
                            numInCart = Integer.parseInt(countInCart[index].getText());
                        }
                        numInCart++;
                        countInCart[index].setText(String.valueOf(numInCart));
                        int currentPrice = Integer.parseInt(price[index].getText());
                        cost[index].setText(String.valueOf(numInCart * currentPrice));
                        totalCount.setText(sum(countInCart));
                        totalCost.setText(sum(cost));
                    }
                }
            });

            butMinus[i].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    int numInCart;
                    if (!countInCart[index].getText().isEmpty()) {
                        numInCart = Integer.parseInt(countInCart[index].getText());
                        if (numInCart > 0) {
                            numInCart--;
                            countInCart[index].setText(String.valueOf(numInCart));
                            int currentPrice = Integer.parseInt(price[index].getText());
                            cost[index].setText(String.valueOf(numInCart * currentPrice));
                            totalCount.setText(sum(countInCart));
                            totalCost.setText(sum(cost));
                        }
                        if (numInCart == 0) {
                            countInCart[index].setText("");
                            cost[index].setText("");
                        }
                    }
                }
            });
        }

        ArrayList<Good> goods = goodsInShop;
        butBuy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ArrayList<Good> buyList = new ArrayList<>();
                for (int index = 0; index < countOfGoods; index++) {
                    if (!countInCart[index].getText().isEmpty()) {
                        int count = Integer.parseInt(countInCart[index].getText());
                        buyList.add(goods.get(index));
                        buyList.get(buyList.size() - 1).count = count;
                        countInCart[index].setText("");
                        cost[index].setText("");
                    }
                }
                try {
                    service.buy(buyList);
                    service.getAll();
                    buyList.clear();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        gridPane.add(butBuy, 3, countOfGoods + 2);

        mainPane.setContent(gridPane);
        window.setScene(new Scene(mainPane, 400, 450));

        window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static String sum(Label[] cost) {
        int result = 0;
        for (Label iter : cost) {
            if (!(iter.getText().isEmpty())) {
                result += Integer.parseInt(iter.getText());
            }
        }
        return String.valueOf(result);
    }

//    private static void viewUpdate(ArrayList<Good> goods, GridPane gridPane) {
//
//
//    }
}