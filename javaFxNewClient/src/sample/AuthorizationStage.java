package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class AuthorizationStage {


    public void createStage(String server, Controller controller) {
        int coordX = 700;
        int coordY = 10;
        int gap = 20;
        int inset = 10;

        Stage authorization = new Stage();

        authorization.setX(coordX);
        authorization.setY(coordY);
        authorization.setTitle("Authorization");

        GridPane pane = new GridPane();
        pane.setHgap(gap);
        pane.setVgap(gap);
        pane.setPadding(new Insets(inset, inset, inset, inset));

        pane.add(new Label("Input Admin Name & Password"), 0, 0, 2, 1);

        pane.add(new Label("Admin Name"), 0, 1);
        TextField textLogin = new TextField("login3");
        pane.add(textLogin, 1, 1);

        pane.add(new Label("Password"), 0, 2);
        PasswordField textPassword = new PasswordField();
        pane.add(textPassword, 1, 2);

        Button buttonLogin = new Button("Login");
        buttonLogin.setAlignment(Pos.BOTTOM_RIGHT);
        pane.add(buttonLogin, 1, 3);

        authorization.setScene(new Scene(pane));
        authorization.show();

        buttonLogin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String result = controller.authorization(server, textLogin.getText(), textPassword.getText());
                if (!result.equals("FAILED")) {
                    authorization.close();
                    new AdminStage().adminForm(server, controller);
                } else {
                    new Alert(Alert.AlertType.ERROR, result).show();
                }
            }
        });
    }
}