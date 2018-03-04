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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main extends Application {

    private Logger LOGGER = LoggerFactory.getLogger(Controller.class);
    private final String serverName = "http://localhost:4567/";
    private Controller controller;
    private GridPane pane;

    private TextField server;

    private TableView<Good> shopTable;
    private ObservableList<Good> observableGoodsInShop;

    private TableView<Good> cartTable;
    private ObservableList<Good> observableGoodsInCart;

    private Label totalCount;
    private Label totalPrice;

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

        shopWindow.setX(coordX);
        shopWindow.setY(coordY);
        shopWindow.setTitle("Shopping");
    }

    private void createPane() {
        pane = new GridPane();
//        pane.setGridLinesVisible(true);

        int gap = 5;
        pane.setHgap(gap);
        pane.setVgap(gap);

        int inset = 10;
        pane.setPadding(new Insets(inset, inset, inset, inset));

        pane.getColumnConstraints().add(new ColumnConstraints(35));
        pane.getColumnConstraints().add(new ColumnConstraints(140));
        pane.getColumnConstraints().add(new ColumnConstraints(115));
        pane.getColumnConstraints().add(new ColumnConstraints(70));
        pane.getColumnConstraints().add(new ColumnConstraints(90));
        pane.getColumnConstraints().add(new ColumnConstraints(70));

        int rowHeight = 25;
        pane.getRowConstraints().add(new RowConstraints(rowHeight));
        pane.getRowConstraints().add(new RowConstraints(rowHeight));
        pane.getRowConstraints().add(new RowConstraints(100));
        pane.getRowConstraints().add(new RowConstraints(rowHeight));
        pane.getRowConstraints().add(new RowConstraints(rowHeight));
        pane.getRowConstraints().add(new RowConstraints(100));
        pane.getRowConstraints().add(new RowConstraints(rowHeight));
        pane.getRowConstraints().add(new RowConstraints(rowHeight));
        pane.getRowConstraints().add(new RowConstraints(rowHeight));
        pane.getRowConstraints().add(new RowConstraints(rowHeight));
    }

    private void createServerInfo() {
        pane.add(new Label("Server"), 0, 0);
        server = new TextField(serverName);
        pane.add(server, 1, 0);
    }

    private void createRefreshButton() {
        Button refreshButton = new Button("Refresh");
        refreshButton.setMinWidth(115);
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
        pane.add(shopTable, 0, 2, 2, 5);
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
        pane.add(cartTable, 3, 2, 3, 4);
    }

    private void createAddToCartButton() {
        Button addToCartButton = new Button("Add to cart");
        addToCartButton.setMinWidth(115);
        pane.add(addToCartButton, 2, 3);
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
        Button buttonRemoveFromCart = new Button("Remove from cart");
        buttonRemoveFromCart.setMinWidth(115);
        pane.add(buttonRemoveFromCart, 2, 4);

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
        Button buttonIncrement = new Button("+");
        buttonIncrement.setMinWidth(70);
        pane.add(buttonIncrement, 3, 6);
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
        Button buttonDecrement = new Button("-");
        buttonDecrement.setMinWidth(70);
        pane.add(buttonDecrement, 5, 6);
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
        Button buttonAddToShop = new Button("Add new good");
        pane.add(buttonAddToShop, 1, 7);
        buttonAddToShop.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                new AuthorizationStage().createStage(server.getText(), controller);
            }
        });
    }

    private void createBuyButton() {
        Button buyButton = new Button("Buy all goods");
        pane.add(buyButton, 4, 7);
        buyButton.setOnAction(new EventHandler<ActionEvent>() {
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

        pane.add(new Label("Total count:"), 3, 8);
        pane.add(new Label("Total price:"), 3, 9);
        totalCount = new Label("");
        totalPrice = new Label("");
        pane.add(totalCount, 4, 8);
        pane.add(totalPrice, 4, 9);
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