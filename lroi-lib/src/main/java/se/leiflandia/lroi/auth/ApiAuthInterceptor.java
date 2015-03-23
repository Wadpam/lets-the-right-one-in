package se.leiflandia.lroi.auth;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.Context;

import retrofit.RequestInterceptor;
import se.leiflandia.lroi.utils.AuthUtils;

public class ApiAuthInterceptor implements RequestInterceptor {

    private final Context appContext;
    private final AccountManager accountManager;
    private final String authtokenType;
    private String accountType;

    public ApiAuthInterceptor(Context application, AccountManager accountManager, String authtokenType, String accountType) {
        this.appContext = application;
        this.accountManager = accountManager;
        this.authtokenType = authtokenType;
        this.accountType = accountType;
    }

    @Override
    public void intercept(RequestFacade request) {
        Account account = AuthUtils.getActiveAccount(appContext, accountType);
        String token = null;
        if (account != null) {
            token = accountManager.peekAuthToken(account, authtokenType);
        }
        // Do this even if token is null because otherwise we get 403 and the
        // RequestInterceptor won't fire.
        request.addHeader("Authorization", authHeaderValue(token));
    }

    public static String authHeaderValue(String token) {
        return "Bearer " + token;
    }
}
