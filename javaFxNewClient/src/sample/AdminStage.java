package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class AdminStage {

    private Controller controller;
    private String server;
    private Stage adminStage;
    private GridPane pane;
    private TextField name;
    private TextField price;
    private TextField count;
    private Button addButton;

    public void adminForm(String server, Controller controller) {
        this.controller = controller;
        this.server = server;

        stageCreate();
        paneCreate();
        addButtonCreate();

        Scene scene = new Scene(pane, 10, 10);
        adminStage.setScene(scene);
        adminStage.show();
    }

    private void stageCreate() {
        int xCoord = 700;
        int yCoord = 10;
        int height = 200;
        int width = 305;

        adminStage = new Stage();
        adminStage.setX(xCoord);
        adminStage.setY(yCoord);
        adminStage.setHeight(height);
        adminStage.setWidth(width);
        adminStage.setTitle("Adding goods to shop");
    }

    private void paneCreate() {
        int gap = 10;
        int inset = 20;
        pane = new GridPane();
        pane.setHgap(gap);
        pane.setVgap(gap);
        pane.setPadding(new Insets(inset, inset, inset, inset));

        pane.add(new Label("Name:"), 0, 0);
        name = new TextField();
        pane.add(name, 1, 0);

        pane.add(new Label("Price:"), 0, 1);
        price = new TextField();
        pane.add(price, 1, 1);

        pane.add(new Label("Сount:"), 0, 2);
        count = new TextField();
        pane.add(count, 1, 2);
    }

    private void addButtonCreate() {
        addButton = new Button("Add good");
        pane.add(addButton, 1, 3);
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String nameOfGood = name.getText();
                try {
                    int priceOfGood = Integer.parseInt(price.getText());
                    int countOfGood = Integer.parseInt(count.getText());
                    Good newGood = new Good();

                    newGood.name = nameOfGood;
                    newGood.price = priceOfGood;
                    newGood.count = countOfGood;

                    String result = controller.add(server, newGood);
                    if (result.equals("FAILED")) {
                        new Alert(Alert.AlertType.ERROR, "Failed!").show();
                    } else {
                        new Alert(Alert.AlertType.INFORMATION, result).show();
                    }
                } catch (NumberFormatException e) {
                    new Alert(Alert.AlertType.ERROR, "Incorrect values are entered!").show();
                }
            }
        });
    }
}
