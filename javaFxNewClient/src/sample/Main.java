package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {

    public final String server = "http://localhost:4567/";

    @Override
    public void start(Stage shopWindow) throws Exception {
        viewInitialization(shopWindow);
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void viewInitialization(Stage shopWindow) {
        int coordX = 100;
        int coordY = 10;
        shopWindow.setX(coordX);
        shopWindow.setY(coordY);

        GridPane pane = createPane();
        createServerInfo(pane);
        createShopView(pane);
        createAddToShop(pane);
        createAddToCart(pane);
        createCartView(pane);
        createBuy(pane);


        shopWindow.setScene(new Scene(pane));
        shopWindow.show();
    }

    private GridPane createPane() {
        int gap = 10;
        int inset = 10;
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(gap);
        pane.setVgap(gap);
        pane.setPadding(new Insets(inset, inset, inset, inset));
        return pane;
    }

    private void createServerInfo(GridPane pane) {
        Label labelServer = new Label("Server");
        TextField textServer = new TextField(server);
        Button refreshButton = new Button("Refresh");

        pane.add(labelServer, 0, 0);
        pane.add(textServer, 1, 0);
        pane.add(refreshButton, 2, 0);

        refreshButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

            }
        });

    }

    private void createShopView(GridPane pane) {
        pane.add(new Label("Goods in Shop"), 1, 1);
        TableView<Good> shopTable = new TableView<>();
        TableColumn<Good, String> name = new TableColumn<>("Name");
        TableColumn<Good, Integer> price = new TableColumn<>("Price");
        shopTable.getColumns().addAll(name, price);
        pane.add(shopTable, 0, 2, 2, 1);
    }

    private void createAddToShop(GridPane pane) {
        Button buttonAddToCart = new Button("Add new good");
        pane.add(buttonAddToCart, 1, 3);

        buttonAddToCart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                new AuthorizationStage().initialization();
            }
        });
    }

    private void createAddToCart(GridPane pane) {
        Button buttonAddToCart = new Button("Add to Card");
        pane.add(buttonAddToCart, 2, 2);

        buttonAddToCart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

            }
        });
    }

    private void createCartView(GridPane pane) {
        pane.add(new Label("Goods in Cart"), 4, 1);
        TableView<Good> cartTable = new TableView<>();
        TableColumn<Good, String> name = new TableColumn<>("Name");
        TableColumn<Good, Integer> price = new TableColumn<>("Price");
        TableColumn<Good, Integer> number = new TableColumn<>("Number");
        cartTable.getColumns().addAll(name, price, number);
        pane.add(cartTable, 3, 2, 3, 1);
        pane.add(new Label("Total number:"), 3, 4);
        pane.add(new Label("Total price:"), 3, 5);
    }

    private void createBuy(GridPane pane) {
        Button buttonAddToCart = new Button("Buy all goods");
        pane.add(buttonAddToCart, 4, 3);

        buttonAddToCart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

            }
        });
    }
}