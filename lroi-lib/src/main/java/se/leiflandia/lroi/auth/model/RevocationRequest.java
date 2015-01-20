package se.leiflandia.lroi.auth.model;

public class RevocationRequest extends ClientCredentials {

    private String token;
    private String token_type_hint;

    public RevocationRequest(ClientCredentials clientCredentials, String token, String tokenTypeHint) {
        super(clientCredentials);
        this.token = token;
        this.token_type_hint = tokenTypeHint;
    }

    public String getToken() {
        return token;
    }

    public String getToken_type_hint() {
        return token_type_hint;
    }
}
