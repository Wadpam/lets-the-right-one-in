package se.leiflandia.lroi;

import android.accounts.AccountManager;

import se.leiflandia.lroi.auth.model.ClientCredentials;

/**
 * Created by A481582 on 2015-01-24.
 */
public class TestAdapter {
    public static void hej() {
        AuthAdapter auth = new AuthAdapter.Builder()
                .setAuthTokenType("org.example.account")
                .setAccountType("org.example.account")
                .setEndpoint("https://example.org")
                .setAccountManager(AccountManager.get(this))
                .setApplicationContext(this)
                .setClientCredentials(new ClientCredentials("clientId", "clientSecret"))
                .build();
    }
}
