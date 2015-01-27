package se.leiflandia.lroi;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;

import com.google.gson.JsonElement;

import org.apache.http.HttpStatus;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import se.leiflandia.lroi.auth.AccountAuthenticator;
import se.leiflandia.lroi.auth.ApiAuthInterceptor;
import se.leiflandia.lroi.auth.ApiAuthenticator;
import se.leiflandia.lroi.auth.model.AccessToken;
import se.leiflandia.lroi.auth.model.ClientCredentials;
import se.leiflandia.lroi.auth.model.ResetPassword;
import se.leiflandia.lroi.auth.model.RevocationRequest;
import se.leiflandia.lroi.auth.model.User;
import se.leiflandia.lroi.auth.model.UserCredentials;
import se.leiflandia.lroi.network.AuthApi;
import se.leiflandia.lroi.network.PasswordResetFailure;
import se.leiflandia.lroi.network.SigninFailure;
import se.leiflandia.lroi.network.SignoutFailure;
import se.leiflandia.lroi.network.SignupFailure;
import se.leiflandia.lroi.ui.AbstractLoginActivity;
import se.leiflandia.lroi.utils.AuthUtils;
import se.leiflandia.lroi.utils.Callback;
import se.leiflandia.lroi.utils.Utils;

/**
 * Class used to interface with the auth service.
 */
public class AuthAdapter {

    private final AuthApi api;
    private final Context applicationContext;
    private final String authTokenType;
    private final String accountType;
    private final AccountManager accountManager;
    private final ClientCredentials clientCredentials;
    private final AccountAuthenticator accountAuthenticator;
    private final Class<? extends AbstractLoginActivity> loginActivityClass;

    public AuthAdapter(Builder builder) {
        this.api = builder.api;
        this.applicationContext = builder.applicationContext;
        this.authTokenType = builder.authTokenType;
        this.accountType = builder.accountType;
        this.accountManager = builder.accountManager;
        this.clientCredentials = builder.clientCredentials;
        this.loginActivityClass = builder.loginActivityClass;
        accountAuthenticator = new AccountAuthenticator(applicationContext, api, loginActivityClass,
            authTokenType, clientCredentials, accountType);
    }

    /**
     * Asynchronous request to sign up a new user. A signed up user might have to validate the email
     * adress before signing in.
     *
     * @param user the user to sign in
     * @param callback callback to handle success or error
     */
    public void signup(final User user, final Callback<User, SignupFailure> callback) {
        getApi().signup(user, new retrofit.Callback<String>() {
            @Override
            public void success(String id, Response response) {
                callback.success(user);
            }

            @Override
            public void failure(RetrofitError error) {
                SignupFailure kind;
                switch (error.getKind()) {
                    case NETWORK:
                        kind = SignupFailure.NETWORK;
                        break;
                    case HTTP:
                        int status = error.getResponse().getStatus();
                        if (status == HttpStatus.SC_CONFLICT) {
                            kind = SignupFailure.CONFLICT;
                        } else if (status == HttpStatus.SC_BAD_REQUEST) {
                            kind = SignupFailure.BAD_REQUEST;
                        } else {
                            kind = SignupFailure.UNEXPECTED;
                        }
                    default:
                        kind = SignupFailure.UNEXPECTED;
                }
                callback.failure(kind);
            }
        });
    }

    /**
     * Asynchronous request to sign in user. This includes: 1) asynchronous network request to
     * authorize at the server, then if 1 is successful 2) creating a new account in the Android
     * account manager if neccessary (main thread), 3) storing the access and refresh tokens locally
     * using the account manager (main thread). The refresh token is stored as the password for the
     * account.
     *
     * @param credentials user credentials
     * @param callback callback to handle success or error, supplies the user name on success.
     */
    public void signin(final UserCredentials credentials, final Callback<String, SigninFailure> callback) {
        getApi().authorize(credentials, new retrofit.Callback<AccessToken>() {
            @Override
            public void success(AccessToken token, Response response) {
                AuthUtils.setAuthorizedAccount(
                        getApplicationContext(),
                        credentials,
                        token,
                        getAuthTokenType(),
                        getAccountType()
                );
                callback.success(credentials.getUsername());
            }

            @Override
            public void failure(RetrofitError error) {
                SigninFailure kind;
                switch (error.getKind()) {
                    case NETWORK:
                        kind = SigninFailure.NETWORK;
                        break;
                    case HTTP:
                        int status = error.getResponse().getStatus();
                        if (status == HttpStatus.SC_BAD_REQUEST) {
                            kind = SigninFailure.BAD_CREDENTIALS;
                        } else {
                            kind = SigninFailure.UNEXPECTED;
                        }
                    default:
                        kind = SigninFailure.UNEXPECTED;
                }
                callback.failure(kind);
            }
        });
    }

    /**
     * Tries to sign out the current active account. Will always succeed except if
     * failOnNetworkError is set to true, then it will fail if the network request to sign out is
     * unsuccessful. Will succeed if the account is null or not exist.
     *
     * @param failOnNetworkError if true the sign out will be cancelled on network errors
     * @param callback           callback
     */
    public void signout(final boolean failOnNetworkError, final Callback<Void, SignoutFailure> callback) {
        Account account = AuthUtils.getActiveAccount(getApplicationContext(), getAccountType());

        if (account == null) {
            callback.success(null);
            return;
        }

        RevocationRequest req = new RevocationRequest(getClientCredentials(), getAccountManager().getPassword(account), "refresh_token");
        getApi().revoke(req, new retrofit.Callback<String>() {
            @Override
            public void success(String s, Response response) {
                AuthUtils.removeActiveAccount(getApplicationContext(), getAccountType());
                callback.success(null);
            }

            @Override
            public void failure(RetrofitError error) {
                if (failOnNetworkError) {
                    callback.failure(SignoutFailure.NETWORK);
                } else {
                    AuthUtils.removeActiveAccount(getApplicationContext(), getAccountType());
                    callback.success(null);
                }
            }
        });
    }

    public void passwordReset(final ResetPassword request, final Callback<Void, PasswordResetFailure> callback) {
        getApi().resetPassword(request, new retrofit.Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                callback.success(null);
            }

            @Override
            public void failure(RetrofitError error) {
                switch (error.getKind()) {
                    case NETWORK:
                        callback.failure(PasswordResetFailure.NETWORK);
                        break;
                    default:
                        callback.failure(PasswordResetFailure.UNEXPECTED);
                        break;
                }
            }
        });
    }

    public boolean isSignedIn() {
        return AuthUtils.hasActiveAccount(getApplicationContext(), getAccountType());
    }

    public AccountAuthenticator getAccountAuthenticator() {
        return accountAuthenticator;
    }

    public ApiAuthInterceptor createAuthInterceptor() {
        return new ApiAuthInterceptor(getApplicationContext(), getAccountManager(), getAccountType(), getAccountType());
    }

    public ApiAuthenticator createApiAuthenticator() {
        return new ApiAuthenticator(getApplicationContext(), getAuthTokenType(), getAccountType());
    }

    private AuthApi getApi() {
        return api;
    }

    private Context getApplicationContext() {
        return applicationContext;
    }

    public String getAuthTokenType() {
        return authTokenType;
    }

    public String getAccountType() {
        return accountType;
    }

    private AccountManager getAccountManager() {
        return accountManager;
    }

    public ClientCredentials getClientCredentials() {
        return clientCredentials;
    }

    public UserCredentials createPasswordUserCredentials(String username, String password) {
        return new UserCredentials(getClientCredentials(), username, password, "password");
    }

    public Class<? extends AbstractLoginActivity> getLoginActivityClass() {
        return loginActivityClass;
    }

    public Intent getLoginActivityIntent(Context context) {
        return AbstractLoginActivity.createIntent(context, getLoginActivityClass(), getAccountType(), getAuthTokenType());
    }

    public static class Builder {
        private AuthApi api;
        private Context applicationContext;
        private String authTokenType;
        private String accountType;
        private AccountManager accountManager;
        private ClientCredentials clientCredentials;
        private String endpoint;
        public Class<? extends AbstractLoginActivity> loginActivityClass;

        public Builder() { }

        public Builder setApi(AuthApi api) {
            this.api = api;
            return this;
        }

        public Builder setApplicationContext(Context applicationContext) {
            this.applicationContext = applicationContext;
            return this;
        }

        public Builder setAuthTokenType(String authTokenType) {
            this.authTokenType = authTokenType;
            return this;
        }

        public Builder setAccountType(String accountType) {
            this.accountType = accountType;
            return this;
        }

        public Builder setAccountManager(AccountManager accountManager) {
            this.accountManager = accountManager;
            return this;
        }

        public Builder setClientCredentials(ClientCredentials clientCredentials) {
            this.clientCredentials = clientCredentials;
            return this;
        }

        public Builder setLoginActivityClass(Class<? extends AbstractLoginActivity> loginActivityClass) {
            this.loginActivityClass = loginActivityClass;
            return this;
        }
        /**
         * Set endpoint for the auth api. Not required if an AuthApi is provided, otherwise
         * required.
         */
        public Builder setEndpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public AuthAdapter build() {

            if (api == null && endpoint != null) {
                api = defaultApi(endpoint);
            } else if (api != null && endpoint != null) {
                throw new IllegalArgumentException("You need to set either an AuthApi or an endpoint string.");
            }

            if (accountManager == null && applicationContext != null) {
                accountManager = AccountManager.get(applicationContext);
            }

            Utils.checkNotNull(api);
            Utils.checkNotNull(applicationContext);
            Utils.checkNotNull(authTokenType);
            Utils.checkNotNull(accountType);
            Utils.checkNotNull(accountManager);
            Utils.checkNotNull(clientCredentials);
            Utils.checkNotNull(loginActivityClass);

            return new AuthAdapter(this);
        }

        private AuthApi defaultApi(String endpoint) {
            return new RestAdapter.Builder()
                    .setEndpoint(endpoint)
                    .setLogLevel(RestAdapter.LogLevel.BASIC)
                    .build()
                    .create(AuthApi.class);
        }
    }
}
