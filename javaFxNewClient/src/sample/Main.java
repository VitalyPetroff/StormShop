package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage shopWindow) throws Exception{
        AuthorizationStage authorization = new AuthorizationStage();
        authorization.initialization();

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25,25,25,25));

        Scene scene = new Scene(gridPane);
        shopWindow.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}