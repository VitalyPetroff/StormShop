package sample;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage shopWindow) throws Exception{
        AuthorizationStage authorization = new AuthorizationStage();
        authorization.initialization();

    }

    public static void main(String[] args) {
        launch(args);
    }
}