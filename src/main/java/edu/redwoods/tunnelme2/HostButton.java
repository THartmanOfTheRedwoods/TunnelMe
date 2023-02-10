package edu.redwoods.tunnelme2;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import javafx.scene.control.Button;

public class HostButton extends Button {
    private String hostname;
    private int port;
    private String username;
    private String password;
    private byte[] privateKeyBytes;
    private byte[] publicKeyBytes;
    private PortFwdData portFwdData;
    private Session session;

    public HostButton(String hostname, int port, String username, String password) {
        this(hostname, port, username, password, null, null, null);
    }
    public HostButton(String hostname, int port, String username, String password, PortFwdData portFwdData) {
        this(hostname, port, username, password, null, null, portFwdData);
    }
    public HostButton(String hostname, int port, String username, String password, byte[] privateKeyBytes, byte[] publicKeyBytes, PortFwdData portFwdData) {
        super(hostname);
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
        this.privateKeyBytes = privateKeyBytes;
        this.publicKeyBytes = publicKeyBytes;
        this.portFwdData = portFwdData;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public byte[] getPrivateKeyBytes() {
        return privateKeyBytes;
    }

    public byte[] getPublicKeyBytes() {
        return publicKeyBytes;
    }

    public PortFwdData getPortFwdData() {
        return portFwdData;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) throws JSchException {
        this.session = session;
        if(this.isActive()) {
            if(this.portFwdData.isReverse()) {
                this.session.setPortForwardingR(
                        this.portFwdData.getRemotePort(), this.portFwdData.getHost(), this.portFwdData.getLocalPort());
                System.out.println(this.portFwdData.getHost()+":"+
                        this.portFwdData.getRemotePort()+" -> localhost:"+this.portFwdData.getLocalPort());
            } else {
                int assigned_port = this.session.setPortForwardingL(
                        this.portFwdData.getLocalPort(), this.portFwdData.getHost(), this.portFwdData.getRemotePort());
                System.out.println("localhost:"+assigned_port+" -> "+
                        this.portFwdData.getHost()+":"+this.portFwdData.getRemotePort());
            }
            this.setStyle("-fx-background-color: #1EEF23;");
        }
    }

    public boolean isActive() {
        return session != null;
    }

    public void disconnect() {
        if(isActive()) {
            this.session.disconnect();
        }
        this.setStyle("-fx-background-color: #1E1F23;");
        this.session = null;
    }
}
