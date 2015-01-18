package se.leiflandia.lroi.auth;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.util.Log;

import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.Proxy;

import se.leiflandia.lroi.utils.AuthUtils;

public class ApiAuthenticator implements Authenticator {

    private static final String TAG = ApiAuthenticator.class.getSimpleName();

    final AccountManager accountManager;
    final Application app;
    final String authtokenType;
    final String accountType;

    public ApiAuthenticator(Application application, String authtokenType, String accountType) {
        this.app = application;
        this.authtokenType = authtokenType;
        this.accountType = accountType;
        this.accountManager = AccountManager.get(application);
    }

    @Override
    public Request authenticate(Proxy proxy, Response response) throws IOException {
        Log.d(TAG, "Calling ApiAuthenticator.authenticate");

        // Do not try to authenticate oauth related paths
        if (response.request().uri().getPath().startsWith("/oauth")) return null;

        Account account = AuthUtils.getActiveAccount(app);

        String oldToken = accountManager.peekAuthToken(account, authtokenType);
        if (oldToken != null) {
            Log.d(TAG, "Invalidating auth token.");
            accountManager.invalidateAuthToken(accountType, oldToken);
        }

        try {
            Log.d(TAG, "calling accountManager.blockingGetAuthToken");
            String token = accountManager.blockingGetAuthToken(account, authtokenType, false);

            if (token == null) {
                accountManager.removeAccount(account, null, null);
            } else {
                return response.request().newBuilder()
                        .header("Authorization", "Bearer " + token)
                        .build();
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to retrieve auth token.", e);
        }

        return null;
    }

    @Override
    public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
        return null;
    }

}
