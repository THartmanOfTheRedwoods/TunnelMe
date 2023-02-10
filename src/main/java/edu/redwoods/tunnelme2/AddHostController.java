package edu.redwoods.tunnelme2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;

import com.jcraft.jsch.*;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.IOException;

public class AddHostController {
    @FXML
    private TextField tbHostname;
    @FXML
    private TextField tbPort;
    @FXML
    private TextField tbUsername;
    @FXML
    private TextField tbPassword;
    @FXML
    private TextField tbKeyPath;
    @FXML
    private TextField tbPubKeyPath;
    @FXML
    private CheckBox cbUseKey;
    @FXML
    private TextField tbPortFwdRHost;
    @FXML
    private TextField tbPortFwdLPort;
    @FXML
    private TextField tbPortFwdRPort;
    @FXML
    private CheckBox cbPortFwdRev;
    private ListView<HostButton> lvHosts;
    private JSch jsch;

    public void initialize() {
        jsch = new JSch();
    }

    private byte[] readKey(String keyPath) throws IOException {
        FileInputStream fs = new FileInputStream(keyPath);
        byte[] keyBytes = fs.readAllBytes();
        fs.close();
        return keyBytes;
    }

    @FXML
    void enableKeyTextFields(ActionEvent event) {
        if(this.cbUseKey.isSelected()) {
            this.tbKeyPath.setDisable(false);
            this.tbPubKeyPath.setDisable(false);
        } else {
            this.tbKeyPath.setDisable(true);
            this.tbPubKeyPath.setDisable(true);
        }
    }

    @FXML
    void checkConnection(ActionEvent event) {
        System.out.println("Checking Connection...");
        String hostname = this.tbHostname.getText();
        int port = Integer.parseInt(this.tbPort.getText());
        String username = this.tbUsername.getText();
        String password = this.tbPassword.getText();

        Session s = null;
        if(this.cbUseKey.isSelected()) {
            try {
                s = connect(hostname, port, username, password,
                        readKey(this.tbKeyPath.getText()), readKey(this.tbPubKeyPath.getText()));
            } catch (IOException e) {
                pubPrivKeyAlert(e);
            }
        } else {
            s = connect(hostname, port, username, password);
        }
        if(s != null) {
            s.disconnect();
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Connection Successful");
            a.setContentText("Connection Successful");
            a.show();
        }
    }

    private void pubPrivKeyAlert(IOException e) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Failed to load public / private key pairs.");
        a.setContentText(e.getMessage());
        a.show();
    }

    @FXML
    void saveHost(ActionEvent event) {
        System.out.println("Saving Host...");
        String hostname = this.tbHostname.getText();
        int port = Integer.parseInt(this.tbPort.getText());
        String username = this.tbUsername.getText();
        String password = this.tbPassword.getText();

        PortFwdData portFwdData = null;
        try {
            portFwdData = new PortFwdData(this.tbPortFwdRHost.getText(),
                    Integer.parseInt(this.tbPortFwdLPort.getText()),
                    Integer.parseInt(this.tbPortFwdRPort.getText()),
                    this.cbPortFwdRev.isSelected());
        } catch (NumberFormatException nfe) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Port Forward Data Error");
            a.setContentText("Improper port forwarding data.");
            a.show();
        }

        try {
            HostButton btnHost;
            if(cbUseKey.isSelected()) {
                btnHost = new HostButton(hostname, port, username, password,
                        readKey(tbKeyPath.getText()), readKey(tbPubKeyPath.getText()), portFwdData);
            } else {
                btnHost = new HostButton(hostname, port, username, password, portFwdData);
            }
            btnHost.setStyle("-fx-background-color: #1E1F23");
            btnHost.setTextFill(Color.WHITE);
            btnHost.setOnAction((e) -> {
                if(btnHost.isActive()) {
                    btnHost.disconnect();
                } else {
                    try {
                        btnHost.setSession(
                                connect(btnHost.getHostname(), btnHost.getPort(), btnHost.getUsername(),
                                        btnHost.getPassword(), btnHost.getPrivateKeyBytes(), btnHost.getPublicKeyBytes()));
                    } catch (JSchException ex) {
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setTitle("Port Forward Connection Error");
                        a.setContentText("Failed to forward ports.");
                        a.show();
                    }
                }
            });
            btnHost.setOnMouseClicked((me) -> {
                MouseButton btn = me.getButton();
                if(btn == MouseButton.SECONDARY) {
                    this.lvHosts.getItems().remove(btnHost);
                }
            });
            lvHosts.getItems().add(btnHost);
        } catch (IOException e) {
            pubPrivKeyAlert(e);
        }
    }

    private Session connect(String hostname, int port, String username, String password) {
        return connect(hostname, port, username, password, null, null);
    }
    private Session connect(String hostname, int port, String username, String password, byte[] privateKey, byte[] pubKey) {
        try {
            Session session  = jsch.getSession(username, hostname, port);
            if(privateKey != null) {
                try {
                    jsch.addIdentity(username, privateKey, pubKey, password.getBytes());
                } catch (JSchException e) {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Failed to add identity.");
                    a.setContentText(e.getMessage());
                    a.show();
                    return null;
                }
            } else {
                session.setPassword(password);
            }
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            return session;
        } catch (JSchException e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Failed to get a session.");
            a.setContentText(e.getMessage());
            a.show();
        }
        return null;
    }

    public ListView<?> getLvHosts() {
        return lvHosts;
    }

    public void setLvHosts(ListView<HostButton> lvHosts) {
        this.lvHosts = lvHosts;
    }
}
