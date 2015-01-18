package se.leiflandia.lroi.auth.model;


/**
 * Access token as specified in RFC 6749 (http://tools.ietf.org/html/rfc6749#section-5.1)
 */
public class AccessToken {

    /** REQUIRED. The access token issued by the authorization server. */
    private String access_token;

    /** REQUIRED. The type of the token issued as described in http://tools.ietf.org/html/rfc6749#section-7.1. */
    private String token_type;

    /** RECOMMENDED. The lifetime in seconds of the access token. */
    private Integer expires_in;

    /** OPTIONAL. The refresh token. */
    private String refresh_token;

    /** OPTIONAL, if identical to the scope requested by the client; otherwise, REQUIRED. */
    private String scope;

    private AccessToken() { /* For gson. */ }

    public AccessToken(String accessToken, String tokenType, Integer expiresIn, String refreshToken, String scope) {
        this.access_token = accessToken;
        this.token_type = tokenType;
        this.expires_in = expiresIn;
        this.refresh_token = refreshToken;
        this.scope = scope;
    }

    public boolean hasExpireIn() { return expires_in != null; }
    public boolean hasRefreshToken() { return refresh_token != null; }
    public boolean hasScope() { return scope != null; }

    public String getAccessToken() {
        return access_token;
    }
    public String getTokenType() {
        return token_type;
    }
    public Integer getExpiresIn() {
        return expires_in;
    }
    public String getRefreshToken() {
        return refresh_token;
    }
    public String getScope() {
        return scope;
    }
}
