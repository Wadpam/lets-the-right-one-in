package se.leiflandia.lroi.auth.model;

import android.os.Build;

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

    public static class Builder {
        private final ClientCredentials clientCredentials;
        private String username;
        private String password;
        private String grant_type;

        private Builder(ClientCredentials clientCredentials) {
            this.clientCredentials = clientCredentials;
        }

        public static Builder with(ClientCredentials clientCredentials) {
            return new Builder(clientCredentials);
        }

        public static Builder with(String clientId, String clientSecret) {
            return new Builder(new ClientCredentials(clientId, clientSecret));
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setGrantType(String grant_type) {
            this.grant_type = grant_type;
            return this;
        }

        public UserCredentials build() {
            return new UserCredentials(
                    clientCredentials,
                    username,
                    password,
                    grant_type);
        }
    }
}
