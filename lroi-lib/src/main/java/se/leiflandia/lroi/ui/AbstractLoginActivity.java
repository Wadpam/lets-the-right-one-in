package se.leiflandia.lroi.ui;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;

import org.apache.http.HttpStatus;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import se.leiflandia.lroi.auth.model.AccessToken;
import se.leiflandia.lroi.auth.model.User;
import se.leiflandia.lroi.auth.model.UserCredentials;
import se.leiflandia.lroi.network.AuthApi;
import se.leiflandia.lroi.utils.AuthUtils;

public abstract class AbstractLoginActivity extends AccountAuthenticatorActivity {
    // TODO Read parameters in onCreate and handle missing
    private static final String PARAM_AUTH_TOKEN_TYPE = "PARAM_AUTH_TOKEN_TYPE";

    public static Intent createIntent(
            Context context,
            Class<? extends AbstractLoginActivity> loginActivityClass,
            AccountAuthenticatorResponse response,
            String accountName,
            String accountType,
            String authTokenType) {

        final Intent intent = new Intent(context, loginActivityClass);
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, accountName);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType);
        intent.putExtra(AbstractLoginActivity.PARAM_AUTH_TOKEN_TYPE, authTokenType);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        return intent;
    }

    public static Intent createIntent(
            Context context,
            Class<? extends AbstractLoginActivity> loginActivityClass,
            AccountAuthenticatorResponse response,
            String accountType,
            String authTokenType) {
        return createIntent(context, loginActivityClass, response, null, accountType, authTokenType);
    }

    public static Intent createIntent(
            Context context,
            Class<? extends AbstractLoginActivity> loginActivityClass,
            String accountType,
            String authTokenType) {
        return createIntent(context, loginActivityClass, null, null, accountType, authTokenType);
    }

    public abstract AuthApi getApi();

    public static boolean checkEmailFormat(String email) {
        return email != null && email.contains("@") && email.length() >= 3;
    }

    public static boolean checkUsernameFormat(String username) {
        return username != null && username.matches("^.{5,}$");
    }

    public static boolean checkPasswordFormat(String password) {
        return password != null && password.matches("^[^\\x00-\\x1F]{5,}$");
    }


    /**
     * Convenience method for signing up a user.
     */
    public void signup(final User user, final SignupCallback callback) {
        getApi().signup(user, new Callback<String>() {
            @Override
            public void success(String id, Response response) {
                callback.success(user);
            }

            @Override
            public void failure(RetrofitError error) {
                SignupCallback.SignupFailure kind;
                switch (error.getKind()) {
                    case NETWORK:
                        kind = SignupCallback.SignupFailure.NETWORK;
                        break;
                    case HTTP:
                        int status = error.getResponse().getStatus();
                        if (status == HttpStatus.SC_CONFLICT) {
                            kind = SignupCallback.SignupFailure.CONFLICT;
                        } else if (status == HttpStatus.SC_BAD_REQUEST) {
                            kind = SignupCallback.SignupFailure.BAD_REQUEST;
                        } else {
                            kind = SignupCallback.SignupFailure.UNEXPECTED;
                        }
                    default:
                        kind = SignupCallback.SignupFailure.UNEXPECTED;
                }
                callback.failure(kind, error);
            }
        });
    }

    public void signin(final UserCredentials credentials, final SigninCallback callback) {
        getApi().authorize(credentials, new Callback<AccessToken>() {
            @Override
            public void success(AccessToken token, Response response) {
                AuthUtils.setAuthorizedAccount(
                        getApplicationContext(),
                        credentials,
                        token,
                        getIntent().getStringExtra(PARAM_AUTH_TOKEN_TYPE),
                        getIntent().getStringExtra(AccountManager.KEY_ACCOUNT_TYPE)
                );
                callback.success();
            }

            @Override
            public void failure(RetrofitError error) {
                SigninCallback.SigninFailure kind;
                switch (error.getKind()) {
                    case NETWORK:
                        kind = SigninCallback.SigninFailure.NETWORK;
                        break;
                    case HTTP:
                        int status = error.getResponse().getStatus();
                        if (status == HttpStatus.SC_BAD_REQUEST) {
                            kind = SigninCallback.SigninFailure.BAD_CREDENTIALS;
                        } else {
                            kind = SigninCallback.SigninFailure.UNEXPECTED;
                        }
                    default:
                        kind = SigninCallback.SigninFailure.UNEXPECTED;
                }
                callback.failure(kind, error);
            }
        });
    }

    public interface SignupCallback {
        public enum SignupFailure { NETWORK, CONFLICT, BAD_REQUEST, UNEXPECTED; }

        public void success(User user);
        public void failure(SignupFailure kind, RetrofitError error);
    }

    public interface SigninCallback {
        public enum SigninFailure { NETWORK, BAD_CREDENTIALS, UNEXPECTED; }

        public void success();
        public void failure(SigninFailure kind, RetrofitError error);
    }
}
