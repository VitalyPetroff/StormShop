package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application {

    private Logger LOGGER = LoggerFactory.getLogger(Controller.class);
    private final String serverName = "http://localhost:4567/";
    private Controller controller;
    private double buttonWidth = 55;
    private GridPane pane;

    private TextField server;
    private Button refreshButton;

    private TableView<Good> shopTable;
    private ObservableList<Good> observableGoodsInShop;
    private Button addToShopButton;

    private TableView<Good> cartTable;
    private ObservableList<Good> observableGoodsInCart;
    private Button addToCartButton;
    private Button buttonRemoveFromCart;

    private Label totalCount;
    private Label totalPrice;
    private Button buyButton;
    private Button buttonIncrement;
    private Button buttonDecrement;
    private Button buttonAddToShop;

    public Main() {
    }

    @Override
    public void start(Stage shopWindow) throws Exception {
        stageInitialization(shopWindow);
        createPane();
        createServerInfo();
        createRefreshButton();
        createShopView();
        createCartView();
        createAddToCartButton();
        createIncrement();
        createDecrement();
        createRemoveFromCartButton();
        createAddToShopButton();
        createBuyButton();

        shopWindow.setScene(new Scene(pane));
        shopWindow.show();

        controller = new Controller();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void stageInitialization(Stage shopWindow) {
        int coordX = 100;
        int coordY = 10;
        int height = 600;
        int width = 600;

        shopWindow.setX(coordX);
        shopWindow.setY(coordY);
        shopWindow.setHeight(height);
        shopWindow.setWidth(width);
        shopWindow.setTitle("Shopping");
    }

    private void createPane() {
        int gap = 10;
        int inset = 10;
        pane = new GridPane();
        pane.setHgap(gap);
        pane.setVgap(gap);
        pane.setPadding(new Insets(inset, inset, inset, inset));
    }

    private void createServerInfo() {
        pane.add(new Label("Server"), 0, 0);
        server = new TextField(serverName);
        pane.add(server, 1, 0);
    }

    private void createRefreshButton() {
        refreshButton = new Button("Refresh");
        pane.add(refreshButton, 2, 0);

        refreshButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                updateShopInfo();
                updateCartInfo();
            }
        });
    }

    private void createShopView() {
        observableGoodsInShop = FXCollections.observableArrayList();
        shopTable = new TableView<>(observableGoodsInShop);
        pane.add(new Label("Goods in Shop"), 1, 1);
        TableColumn<Good, String> nameColumn = new TableColumn<>("Name");
        TableColumn<Good, Integer> priceColumn = new TableColumn<>("Price");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        shopTable.getColumns().addAll(nameColumn, priceColumn);
        pane.add(shopTable, 0, 2, 2, 3);
    }

    private void createCartView() {
        observableGoodsInCart = FXCollections.observableArrayList();
        cartTable = new TableView<>(observableGoodsInCart);
        pane.add(new Label("Goods in Cart"), 4, 1);
        cartTable = new TableView<>();
        TableColumn<Good, String> nameColumn = new TableColumn<>("Name");
        TableColumn<Good, Integer> priceColumn = new TableColumn<>("Price");
        TableColumn<Good, Integer> countColumn = new TableColumn<>("Number");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
        cartTable.getColumns().addAll(nameColumn, priceColumn, countColumn);
        pane.add(cartTable, 3, 2, 3, 2);
    }

    private void createAddToCartButton() {
        addToCartButton = new Button("=>");
        addToCartButton.setMinWidth(buttonWidth);
        pane.add(addToCartButton, 2, 2);
        addToCartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    Good selectedGood = shopTable.getSelectionModel().getSelectedItem();
                    controller.addGoodToCart(selectedGood);
                    updateCartInfo();
                } catch (NullPointerException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        });
    }

    private void createRemoveFromCartButton() {
        buttonRemoveFromCart = new Button("<=");
        buttonRemoveFromCart.setMinWidth(buttonWidth);
        pane.add(buttonRemoveFromCart, 2, 3);

        buttonRemoveFromCart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Good selectedGood = cartTable.getSelectionModel().getSelectedItem();
                controller.removeGoodFromCart(selectedGood);
                updateCartInfo();
            }
        });
    }

    private void createIncrement() {
        buttonIncrement = new Button("+");
        buttonIncrement.setMinWidth(buttonWidth * 2);
        pane.add(buttonIncrement, 3, 4);

        buttonIncrement.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    Good selectedGood = cartTable.getSelectionModel().getSelectedItem();
                    controller.increaseCount(selectedGood);
                    updateCartInfo();
                    cartTable.getSelectionModel().select(selectedGood);
                } catch (NullPointerException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        });
    }

    private void createDecrement() {
        buttonDecrement = new Button("-");
        buttonDecrement.setMinWidth(buttonWidth * 2);
        pane.add(buttonDecrement, 4, 4);

        buttonDecrement.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    Good selectedGood = cartTable.getSelectionModel().getSelectedItem();
                    controller.decreaseCount(selectedGood);
                    updateCartInfo();
                    cartTable.getSelectionModel().select(selectedGood);
                } catch (NullPointerException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        });
    }

    private void createAddToShopButton() {
        buttonAddToShop = new Button("Add new good");
        pane.add(buttonAddToShop, 1, 6);
        buttonAddToShop.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                new AuthorizationStage().createStage(server.getText(), controller);
            }
        });
    }

    private void createBuyButton() {
        addToCartButton = new Button("Buy all goods");
        pane.add(addToCartButton, 4, 6);
        addToCartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    controller.buy(server.getText());
                    new Alert(Alert.AlertType.INFORMATION, "Buying is OK!").show();
                    updateCartInfo();
                    updateShopInfo();
                } catch (IOException e) {
                    new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                }
            }
        });

        pane.add(new Label("Total count:"), 3, 7);
        pane.add(new Label("Total price:"), 3, 8);
        totalCount = new Label("");
        totalPrice = new Label("");
        pane.add(totalCount, 4, 7);
        pane.add(totalPrice, 4, 8);
    }

    private void updateShopInfo() {
        try {
            observableGoodsInShop.clear();
            observableGoodsInShop.addAll(controller.getGoodsInShop(server.getText()));
            shopTable.setItems(observableGoodsInShop);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void updateCartInfo() {
        observableGoodsInCart.clear();
        observableGoodsInCart.addAll(controller.getGoodsInCart());
        cartTable.setItems(observableGoodsInCart);

        totalCount.setText(Integer.toString(controller.getTotalCount()));
        totalPrice.setText(Integer.toString(controller.getTotalPrice()));
    }
}