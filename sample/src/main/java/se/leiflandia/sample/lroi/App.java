package se.leiflandia.sample.lroi;

import android.accounts.AccountManager;
import android.app.Application;

import se.leiflandia.lroi.AuthAdapter;
import se.leiflandia.lroi.auth.model.ClientCredentials;

public class App extends Application {

    private AuthAdapter auth;

    @Override
    public void onCreate() {
        super.onCreate();

        auth = new AuthAdapter.Builder()
                .setClientCredentials(new ClientCredentials("clientId", "clientSecret"))
                .setApplicationContext(this)
                .setEndpoint("https://example.org")
                .setAccountType("org.example.account")
                .setAuthTokenType("org.example.account")
                .setAccountManager(AccountManager.get(this))
                .setLoginActivityClass(LoginActivity.class)
                .build();
    }

    public AuthAdapter getAuth() { return auth; }
}
