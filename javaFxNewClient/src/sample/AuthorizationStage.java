package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class AuthorizationStage {

    public void initialization() {
        int coordX = 500;
        int coordY = 10;
        int gap = 20;

        Stage authorization = new Stage();
        Controller controller = new Controller();

        authorization.setX(coordX);
        authorization.setY(coordY);
        authorization.setTitle("Authorization");

        GridPane pane = new GridPane();
        pane.setHgap(gap);
        pane.setVgap(gap);
        pane.setPadding(new Insets(5));

        pane.add(new Label("Input User Name & Password"), 0, 0, 2, 1);

        pane.add(new Label("User Name"), 0, 1);
        TextField textLogin = new TextField();
        pane.add(textLogin, 1, 1);

        pane.add(new Label("Password"), 0, 2);
        PasswordField textPassword = new PasswordField();
        pane.add(textPassword, 1, 2);

        Button buttonLogin = new Button("Login");
        pane.add(buttonLogin, 1, 3);

        authorization.setScene(new Scene(pane));
        authorization.show();

        buttonLogin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String result = controller.authorization(textLogin.getText(), textPassword.getText());
                if (!result.equals("Authorization is failed!")) {
                    authorization.close();
                    new AdminStage().initialization();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle(result);
                    alert.setHeaderText(result);
                    alert.showAndWait();
                }
            }
        });
    }
}