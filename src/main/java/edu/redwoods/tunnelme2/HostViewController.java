package edu.redwoods.tunnelme2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class HostViewController {

    @FXML
    private BorderPane mainPane;
    @FXML
    private ListView<HostButton> lvHosts;
    @FXML
    private Button btnAddHost;

    public void initialize() { }

    @FXML
    void loadHostAddView(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader();
        Accordion accHostAdd = null;
        try {
            accHostAdd = loader.load(getClass().getResourceAsStream("hostadd.fxml"));
            AddHostController controller = loader.getController();
            controller.setLvHosts(this.lvHosts);
            mainPane.setCenter(accHostAdd);
        } catch (IOException e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("FXML file not found");
            a.setContentText(e.getMessage());
            a.show();
        }
    }
}
