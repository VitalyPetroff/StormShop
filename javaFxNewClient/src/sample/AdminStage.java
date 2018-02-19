package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class AdminStage {
    
    public void initialization(){
        Stage adminStage = new Stage();

        int xCoord = 500;
        int yCoord = 10;
        int height = 70;
        int width = 580;

        adminStage.setX(xCoord);
        adminStage.setY(yCoord);
        adminStage.setHeight(height);
        adminStage.setWidth(width);
        adminStage.setTitle("Adding goods to shop");

        TextField nameOfGood = new TextField();
        TextField priceOfGood = new TextField();
        TextField countOfGood = new TextField();
        Button inputGood = new Button("Add good");

        FlowPane flowPane = new FlowPane();
        flowPane.getChildren().addAll(nameOfGood, priceOfGood, countOfGood, inputGood);
        Scene scene = new Scene(flowPane, 10, 10);

        adminStage.setScene(scene);
        adminStage.show();

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

//                String result = service.addGood(newGood);
//                showResultOfAdding(result);
            }
        });
    }
}
