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

import java.util.ArrayList;

public class Main extends Application {

    @Override
    public void start(Stage window) throws Exception {
        window.setX(100);
        window.setY(10);

        Controller service = new Controller();
        ArrayList<Good> goodsInShop = null;
        try {
            goodsInShop = service.getAll();
        } catch (Exception e) {
            System.out.println("Hello!");
        }

        ScrollPane mainPane = new ScrollPane();
        mainPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        GridPane gridPane = new GridPane();
        int numberOfGoods = goodsInShop.size();

        gridPane.getCellBounds(5, numberOfGoods + 3);

        gridPane.add(new Label(" Наименование "), 0, 0);
        gridPane.add(new Label(" Цена, $"), 1, 0);
        gridPane.add(new Label(" Кол-во в\nмагазине "), 2, 0);
        gridPane.add(new Label(" Кол-во в\nкорзине "), 4, 0);
        gridPane.add(new Label(" Стоимость "), 5, 0);

        Label[] name = new Label[numberOfGoods];
        Label[] price = new Label[numberOfGoods];
        Label[] numberInShop = new Label[numberOfGoods];
        Button[] butPlus = new Button[numberOfGoods];
        Button[] butMinus = new Button[numberOfGoods];
        Label[] numberInCart = new Label[numberOfGoods];
        Label[] cost = new Label[numberOfGoods];
        Label totalCost = new Label();
        Label totalCount = new Label();
        gridPane.add(totalCount, 4, numberOfGoods + 2);
        gridPane.add(totalCost, 5, numberOfGoods + 2);

        gridPane.add(new Label("КОЛ-ВО"), 4, numberOfGoods + 1);
        gridPane.add(new Label("СУММА"), 5, numberOfGoods + 1);
        Button butBuy = new Button();
        butBuy.setText("ОПЛАТА");
        butBuy.setMinWidth(65);

        for (int i = 0; i < numberOfGoods; i++) {
            Good good = goodsInShop.get(i);
            int row = i + 1;
            name[i] = new Label(good.name);
            price[i] = new Label(String.valueOf(good.price));
            numberInShop[i] = new Label(String.valueOf(good.count));
            numberInCart[i] = new Label();
            cost[i] = new Label();
            gridPane.add(name[i], 0, row);
            gridPane.add(price[i], 1, row);
            gridPane.add(numberInShop[i], 2, row);
            gridPane.add(numberInCart[i], 4, row);
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

            int index = i;

            butPlus[i].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (Integer.parseInt(numberInShop[index].getText()) > 0) {
                        int numInCart = 0;
                        if (!numberInCart[index].getText().isEmpty()) {
                            numInCart = Integer.parseInt(numberInCart[index].getText());
                        }
                        numInCart++;
                        numberInCart[index].setText(String.valueOf(numInCart));
                        int currentPrice = Integer.parseInt(price[index].getText());
                        cost[index].setText(String.valueOf(numInCart * currentPrice));
                        totalCount.setText(sum(numberInCart));
                        totalCost.setText(sum(cost));
                    }
                }
            });

            butMinus[i].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    int numInCart;
                    if (!numberInCart[index].getText().isEmpty()) {
                        numInCart = Integer.parseInt(numberInCart[index].getText());
                        if (numInCart > 0) {
                            numInCart--;
                            numberInCart[index].setText(String.valueOf(numInCart));
                            int currentPrice = Integer.parseInt(price[index].getText());
                            cost[index].setText(String.valueOf(numInCart * currentPrice));
                            totalCount.setText(sum(numberInCart));
                            totalCost.setText(sum(cost));
                        }
                        if (numInCart == 0) {
                            numberInCart[index].setText("");
                            cost[index].setText("");
                        }
                    }
                }
            });
        }


        //===================================================================================
//        butBuy.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//                for (int index = 0; index < numberOfGoods; i++) {
//                    if (!numberInCart[index].getText().equals("")) {
//                        int count = Integer.parseInt(numberInCart[i].getText());
//                        numberInShop[index].setText(String.valueOf(--numInShop));
//                        numberInCart[index].setText("");
//                    }
//                }
//            }
//        });
        //===================================================================================
        gridPane.add(butBuy, 3, numberOfGoods + 2);

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
}