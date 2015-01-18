package se.leiflandia.lroi.auth;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import retrofit.RetrofitError;
import se.leiflandia.lroi.auth.model.AccessToken;
import se.leiflandia.lroi.auth.model.ClientCredentials;
import se.leiflandia.lroi.auth.model.RefreshTokenCredentials;
import se.leiflandia.lroi.network.AuthApi;
import se.leiflandia.lroi.ui.AbstractLoginActivity;
import se.leiflandia.lroi.utils.BundleUtils;


public class AccountAuthenticator extends AbstractAccountAuthenticator {
    private static final String TAG = AccountAuthenticator.class.getSimpleName();

    private Context context;
    private AuthApi api;
    private Class<? extends AbstractLoginActivity> loginActivity;
    private String authtokenType;
    private ClientCredentials clientCredentials;
    private String accountType;

    public AccountAuthenticator(Context context, AuthApi api, Class<? extends AbstractLoginActivity> loginActivity, String authtokenType, ClientCredentials clientCredentials, String accountType) {
        super(context);
        this.context = context;
        this.api = api;
        this.loginActivity = loginActivity;
        this.authtokenType = authtokenType;
        this.clientCredentials = clientCredentials;
        this.accountType = accountType;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        return BundleUtils.createBundle(
                AccountManager.KEY_INTENT,
                AbstractLoginActivity.createIntent(context, loginActivity, response, accountType, authTokenType));
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        Log.d(TAG, "getAuthToken() account=" + account.name + " type=" + account.type);

        // If the caller requested an authToken type we don't support, then return an error
        if (!TextUtils.equals(authTokenType, authtokenType)) {
            Log.w(TAG, "Invalid authtoken type " + authTokenType);
            return BundleUtils.createBundle(AccountManager.KEY_ERROR_MESSAGE, "Invalid auth token type.");
        }

        // Extract the username and password from the Account Manager, and ask the server for an
        // appropriate AuthToken.
        final AccountManager am = AccountManager.get(context);
        // Refresh token stored as password.
        final String refreshToken = am.getPassword(account);
        if (refreshToken != null) {
            Log.v(TAG, "Trying to refresh access token using refresh token.");
            try {
                final AccessToken token = api.refreshAccessToken(
                        new RefreshTokenCredentials(clientCredentials, refreshToken, "refresh_token"));
                if (!TextUtils.isEmpty(token.getAccessToken())) {
                    Log.v(TAG, "Retrieved new access token.");
                    am.setPassword(account, token.getRefreshToken());
                    return createAuthTokenBundle(account.name, accountType, token.getAccessToken());
                }
            } catch (RetrofitError e) {
                if (e.getKind() == RetrofitError.Kind.NETWORK) {
                    Log.e(TAG, "Failed to refresh access token because of a network error.", e);
                    throw new NetworkErrorException(e.getMessage());
                } else {
                    Log.e(TAG, "Failed to refresh access token.", e);
                }
            } catch (Exception e) {
                Log.e(TAG, "Failed to refresh access token.", e);
            }
        }

        // If we get here, then we couldn't access the user's refresh token or we failed to refresh
        // the access token. So we re-prompt them for their credentials.
        final Intent intent = AbstractLoginActivity.createIntent(context, loginActivity, response, account.name, accountType, authTokenType);
        return BundleUtils.createBundle(AccountManager.KEY_INTENT, intent);
    }

    private static Bundle createAuthTokenBundle(final String accountName, final String accountType,
                                                final String accessToken) {
        final Bundle result = new Bundle();
        result.putString(AccountManager.KEY_ACCOUNT_NAME, accountName);
        result.putString(AccountManager.KEY_ACCOUNT_TYPE, accountType);
        result.putString(AccountManager.KEY_AUTHTOKEN, accessToken);
        return result;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        // This call is used to query whether the Authenticator supports specific features. We don't
        // expect to get called, so we always return false for any queries.
        return BundleUtils.createBundle(AccountManager.KEY_BOOLEAN_RESULT, false);
    }
}
