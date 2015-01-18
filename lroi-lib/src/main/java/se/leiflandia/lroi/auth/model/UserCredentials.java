package se.leiflandia.lroi.auth.model;

public class UserCredentials extends ClientCredentials {

    private String username;
    private String password;
    private String grant_type;

    public UserCredentials(ClientCredentials clientCredentials, String username, String password, String grantType) {
        super(clientCredentials);
        this.username = username;
        this.password = password;
        this.grant_type = grantType;
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getGrantType() {
        return grant_type;
    }

}
