package config;

import java.util.List;

public class Config {
    public String url;
    public String restPort;
    public String websocketPort;
    public String rpcPort;
    public String defaultPort;
    public String peerPublicKey;
    public String ontid;
    public String role;
    public long gasprice;
    public int M;
    public List<String> wallets;
    public List<String> passwords;


    public String getOntid() {
        return ontid;
    }

    public void setOntid(String ontid) {
        this.ontid = ontid;
    }
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public int getM() {
        return M;
    }

    public void setM(int m) {
        M = m;
    }

    public long getGasprice() {
        return gasprice;
    }

    public void setGasprice(long gasprice) {
        this.gasprice = gasprice;
    }

    public String getPeerPublicKey() {
        return peerPublicKey;
    }

    public void setPeerPublicKey(String peerPublicKey) {
        this.peerPublicKey = peerPublicKey;
    }

    public List<String> getPasswords() {
        return passwords;
    }

    public void setPasswords(List<String> passwords) {
        this.passwords = passwords;
    }

    public List<String> getWallets() {
        return wallets;
    }

    public void setWallets(List<String> wallets) {
        this.wallets = wallets;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRestPort() {
        return restPort;
    }

    public void setRestPort(String restPort) {
        this.restPort = restPort;
    }

    public String getWebsocketPort() {
        return websocketPort;
    }

    public void setWebsocketPort(String websocketPort) {
        this.websocketPort = websocketPort;
    }

    public String getRpcPort() {
        return rpcPort;
    }

    public void setRpcPort(String rpcPort) {
        this.rpcPort = rpcPort;
    }

    public String getDefaultPort() {
        return defaultPort;
    }

    public void setDefaultPort(String defaultPort) {
        this.defaultPort = defaultPort;
    }
}
