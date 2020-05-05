package controller.api;

import controller.impl.EmployeeController;
import controller.impl.MyMain;
import controller.impl.ViewUserController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public interface RootController {

    default FXMLLoader openView(String filename, AnchorPane pane, BorderPane borderPane, String className) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(className.getClass().getResource(filename));
        AnchorPane root = null;
        try {
            root = loader.load();
            borderPane.setCenter(root);
            return loader;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    default void logOut(Button button){
        button.setOnAction(event -> {
            try {
                MyMain.openLoginView();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
