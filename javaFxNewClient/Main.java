package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage authorization) throws Exception{

        authorization.setX(100);
        authorization.setY(10);
        authorization.setTitle("Authorization");

        GridPane pane = new GridPane();
        pane.setHgap(20);
        pane.setVgap(10);
        pane.setPadding(new Insets(5));

        pane.add(new Label("Input User Name & Password"), 0, 0, 2, 1);

        pane.add(new Label("User Name"), 0, 2);
        TextField textLogin = new TextField("User Name");
        pane.add(textLogin, 1, 2);

        pane.add(new Label("Password"), 0, 3);
        PasswordField textPassword = new PasswordField();
        pane.add(textPassword, 1, 3);

        Button buttonLogin = new Button("Login");
        pane.add(buttonLogin, 1, 4);

        authorization.setScene(new Scene(pane));
        authorization.show();

        buttonLogin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
