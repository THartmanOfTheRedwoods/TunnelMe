package edu.redwoods.tunnelme2;

public class PortFwdData {
    private String host;
    private int localPort;
    private int remotePort;
    private boolean isReverse;

    public PortFwdData(String host, int localPort, int remotePort, boolean isReverse) {
        this.host = host;
        this.localPort = localPort;
        this.remotePort = remotePort;
        this.isReverse = isReverse;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getLocalPort() {
        return localPort;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public boolean isReverse() {
        return isReverse;
    }
}
