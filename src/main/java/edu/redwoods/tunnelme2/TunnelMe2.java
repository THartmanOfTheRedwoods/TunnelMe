package edu.redwoods.tunnelme2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class TunnelMe2 extends Application {
    @Override
    public void start(Stage window) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("tunnelme2.fxml")));
        window.setTitle("Tunnel Me 2");
        window.setScene(new Scene(root));
        window.show();
    }
}
