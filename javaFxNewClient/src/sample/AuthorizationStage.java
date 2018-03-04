package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class AuthorizationStage {

    private Stage authorization;
    private Controller controller;
    private String server;
    private TextField textLogin;
    private PasswordField textPassword;


    public void createStage(String server, Controller controller) {
        this.server = server;
        this.controller = controller;

        int coordX = 700;
        int coordY = 10;
        authorization = new Stage();
        authorization.setX(coordX);
        authorization.setY(coordY);
        authorization.setTitle("Authorization");

        int gap = 20;
        int inset = 10;
        GridPane pane = new GridPane();
        pane.setHgap(gap);
        pane.setVgap(gap);
        pane.setPadding(new Insets(inset, inset, inset, inset));

        pane.add(new Label("Input Admin Name & Password"), 0, 0, 2, 1);
        pane.add(new Label("Admin Name"), 0, 1);

        textLogin = new TextField("login3");
        pane.add(textLogin, 1, 1);

        pane.add(new Label("Password"), 0, 2);
        textPassword = new PasswordField();
        pane.add(textPassword, 1, 2);

        Button buttonLogin = new Button("Login");
        buttonLogin.setAlignment(Pos.BOTTOM_RIGHT);
        pane.add(buttonLogin, 1, 3);

        Scene scene = new Scene(pane);
        authorization.setScene(scene);
        authorization.show();

        buttonLogin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                login();
            }
        });

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    login();
                }
            }
        });
    }

    private void login() {
        String result = controller.authorization(server, textLogin.getText(), textPassword.getText());
        if (!result.equals("FAILED")) {
            authorization.close();
            new AdminStage().adminForm(server, controller);
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed authorization!").show();
        }
    }
}