package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {

    private final String serverName = "http://localhost:4567/";
    private ArrayList<Good> goodsInShop = new ArrayList<>();
    private Controller controller = new Controller(serverName);

    private GridPane pane;

    private TextField server;
    private Button refreshButton;

    private TableView<Good> shopTable;
    private Button addToShopButton;

    private TableView<Good> cartTable;
    private ObservableList<Good> observableList;
    private Button addToCartButton;
    private Button buttonRemoveFromCart;

    private Button buyButton;

    @Override
    public void start(Stage shopWindow) throws Exception {
        int coordX = 100;
        int coordY = 10;
        shopWindow.setX(coordX);
        shopWindow.setY(coordY);

        createPane();
        createServerInfo();
        createRefreshButton();
        createShopView();
        createAddToShopButton();
        createAddToCartButton();
        createRemoveFromCartButton();
        createCartView();
        createBuyButton();

        goodsInShop = controller.getAll();
        updateShopInfo();

        shopWindow.setScene(new Scene(pane));
        shopWindow.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void createPane() {
        int gap = 10;
        int inset = 10;
        pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(gap);
        pane.setVgap(gap);
        pane.setPadding(new Insets(inset, inset, inset, inset));
    }

    private void createServerInfo() {
        pane.add(new Label("Server"), 0, 0);
        server = new TextField(serverName);
        pane.add(server, 1, 0);
    }

    private void createRefreshButton(){
        refreshButton = new Button("Refresh");
        pane.add(refreshButton, 2, 0);

        refreshButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                goodsInShop.clear();
                goodsInShop = controller.getAll();
                updateShopInfo();
            }
        });
    }

    private void createShopView() {
        shopTable = new TableView<>();
        pane.add(new Label("Goods in Shop"), 1, 1);
        TableColumn<Good, String> nameColumn = new TableColumn<>("Name");
        TableColumn<Good, Integer> priceColumn = new TableColumn<>("Price");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        shopTable.getColumns().addAll(nameColumn, priceColumn);
        pane.add(shopTable, 0, 2, 2, 2);
        observableList  = FXCollections.observableArrayList();
    }

    private void createAddToShopButton() {
        Button buttonAddToShop = new Button("Add new good");
        pane.add(buttonAddToShop, 1, 4);

        buttonAddToShop.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                new AuthorizationStage().createStage(serverName);
            }
        });
    }

    private void createCartView() {
        cartTable = new TableView<>();
        pane.add(new Label("Goods in Cart"), 4, 1);
        cartTable = new TableView<>();
        TableColumn<Good, String> name = new TableColumn<>("Name");
        TableColumn<Good, Integer> price = new TableColumn<>("Price");
        TableColumn<Good, Integer> number = new TableColumn<>("Number");
        cartTable.getColumns().addAll(name, price, number);
        pane.add(cartTable, 3, 2, 3, 2);
        pane.add(new Label("Total number:"), 3, 5);
        pane.add(new Label("Total price:"), 3, 6);
    }

    private void createAddToCartButton() {
        addToCartButton = new Button("Add to Cart");
        addToCartButton.setAlignment(Pos.CENTER);
        pane.add(addToCartButton, 2, 2);
        addToCartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

            }
        });
    }

    private void createRemoveFromCartButton() {
        buttonRemoveFromCart = new Button("Remove from Cart");
        pane.add(buttonRemoveFromCart, 2, 3);

        buttonRemoveFromCart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

            }
        });
    }

    private void createBuyButton() {
        addToCartButton = new Button("Buy all goods");
        pane.add(addToCartButton, 4, 4);

        addToCartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

            }
        });
    }

    private void updateShopInfo(){
        observableList.clear();
        observableList.addAll(goodsInShop);
        shopTable.setItems(observableList);
    }
}