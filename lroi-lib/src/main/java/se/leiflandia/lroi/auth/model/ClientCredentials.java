package se.leiflandia.lroi.auth.model;

public class ClientCredentials {

    private String client_id;
    private String client_secret;

    public ClientCredentials(String clientId, String clientSecret) {
        this.client_id = clientId;
        this.client_secret = clientSecret;
    }

    public ClientCredentials(ClientCredentials credentials) {
        this(credentials.getClientId(), credentials.getClientSecret());
    }

    public String getClientId() {
        return client_id;
    }
    public String getClientSecret() {
        return client_secret;
    }

    @Override
    public String toString() {
        return String.format("id %s, secret %s", client_id, client_secret);
    }
}
