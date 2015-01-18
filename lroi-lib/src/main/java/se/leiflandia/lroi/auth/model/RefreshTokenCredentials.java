package se.leiflandia.lroi.auth.model;

/**
 * Credentials used to refresh access token (see http://tools.ietf.org/html/rfc6749#section-6)
 */
public class RefreshTokenCredentials extends ClientCredentials {

    private String refresh_token;
    private String grant_type;

    public RefreshTokenCredentials(
            ClientCredentials clientCredentials,
            String refreshToken,
            String grantType) {
        super(clientCredentials);
        refresh_token = refreshToken;
        grant_type = grantType;
    }

    public String getRefreshToken() {
        return refresh_token;
    }
    public String getGrantType() {
        return grant_type;
    }
}
