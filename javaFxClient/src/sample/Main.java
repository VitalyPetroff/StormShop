package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private List<Good> goodsInShop = new ArrayList<>();

    @Override
    public void start(Stage purchase) {
        purchase.setX(100);
        purchase.setY(10);
        purchase.setTitle("ПОКУПКА ТОВАРОВ В МАГАЗИНЕ");

        ScrollPane mainPane = new ScrollPane();
        mainPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        GridPane gridPane = new GridPane();
        gridPane.add(new Label(" Наименование "), 0, 0);
        gridPane.add(new Label(" Цена, $"), 1, 0);
        gridPane.add(new Label(" Кол-во в\nмагазине "), 2, 0);
        gridPane.add(new Label(" Кол-во в\nкорзине "), 4, 0);
        gridPane.add(new Label(" Стоимость "), 5, 0);

        Controller service = new Controller();
        goodsInShop = service.getAll();
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
            int row = i + 1;
            name[i] = new Label(goodsInShop.get(i).name);
            price[i] = new Label(String.valueOf(goodsInShop.get(i).price));
            countInShop[i] = new Label();
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

        goodsCounter(goodsInShop, countInShop);

        gridPane.add(butBuy, 3, countOfGoods + 2);
        mainPane.setContent(gridPane);
        purchase.setScene(new Scene(mainPane));
        purchase.show();

        for (int i = 0; i < countOfGoods; i++) {
            int index = i;

            butPlus[i].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (Integer.parseInt(countInCart[index].getText()) < Integer.parseInt(countInShop[index].getText())) {
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
                    } else {
                        showAlert("Количество товара в магазине меньше нуля!");
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
                    } else {
                        showAlert("Данный товар в корзине отсутствует!");
                    }
                }
            });
        }

        butBuy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ArrayList<Good> buyList = new ArrayList<>();
                for (int iD = 0; iD < countOfGoods; iD++) {
                    if (!countInCart[iD].getText().isEmpty()) {
                        int count = Integer.parseInt(countInCart[iD].getText());
                        buyList.add(goodsInShop.get(iD));
                        buyList.get(buyList.size() - 1).count = count;
                        countInCart[iD].setText("");
                        cost[iD].setText("");
                    }
                }
                service.buy(buyList);
                totalCount.setText("");
                totalCost.setText("");
                goodsInShop = service.getAll();
                goodsCounter(goodsInShop, countInShop);
            }
        });

        Stage addingGoodsWindow = new Stage();

        addingGoodsWindow.setX(500);
        addingGoodsWindow.setY(10);
        addingGoodsWindow.setHeight(70);
        addingGoodsWindow.setWidth(580);
        addingGoodsWindow.setTitle("ДОБАВЛЕНИЕ ТОВАРОВ В МАГАЗИН");

        TextField nameOfGood = new TextField();
        TextField priceOfGood = new TextField();
        TextField countOfGood = new TextField();
        Button inputGood = new Button("Добавить товар");

        FlowPane flowPane = new FlowPane();
        flowPane.getChildren().addAll(nameOfGood, priceOfGood, countOfGood, inputGood);
        Scene scene = new Scene(flowPane, 10, 10);

        addingGoodsWindow.setScene(scene);
        addingGoodsWindow.show();

        inputGood.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String name = nameOfGood.getText();
                int price = Integer.parseInt(priceOfGood.getText());
                int count = Integer.parseInt(countOfGood.getText());

                Good newGood = new Good();

                newGood.name = name;
                newGood.price = price;
                newGood.count = count;

                String result = service.addGood(newGood);
                showResultOfAdding(result);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static String sum(Label[] cost) {
        int result = 0;
        for (Label index : cost) {
            if (!(index.getText().isEmpty())) {
                result += Integer.parseInt(index.getText());
            }
        }
        return String.valueOf(result);
    }

    private static void goodsCounter(List<Good> goods, Label[] count) {
        for (int i = 0; i < goods.size(); i++) {
            count[i].setText(String.valueOf(goods.get(i).count));
        }
    }

    private void showAlert(String information) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText("ВНИМАНИЕ!");
        alert.setContentText(information);
        alert.showAndWait();
    }

    private void showResultOfAdding(String information) {
        if (information.equals("Добавление товара НЕ получилось!")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("");
            alert.setHeaderText("ПРОВАЛ !!!");
            alert.setContentText(information);
            alert.showAndWait();

        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText("УСПЕШНО !!!");
            alert.setContentText(information);
            alert.showAndWait();
        }
    }
}