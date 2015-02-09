package se.leiflandia.lroi.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import se.leiflandia.lroi.auth.model.AccessToken;
import se.leiflandia.lroi.auth.model.UserCredentials;

public class AuthUtils {

    private static final String PREF_ACTIVE_ACCOUNT = "active_account";
    private static final String PREFS_NAME = "se.leiflandia.lroi.prefs";

    public static void removeActiveAccount(Context context, String accountType) {
        Account account = getActiveAccount(context, accountType);
        if (account != null) {
            AccountManager.get(context).removeAccount(account, null, null);
        }
        setActiveAccountName(context, null);
    }

    public static Account getActiveAccount(final Context context, final String accountType) {
        Account[] accounts = AccountManager.get(context).getAccountsByType(accountType);
        return getActiveAccount(accounts, getActiveAccountName(context));
    }

    public static boolean hasActiveAccount(final Context context, final String accountType) {
        return getActiveAccount(context, accountType) != null;
    }

    private static String getActiveAccountName(final Context context) {
        return getSharedPreferences(context)
                .getString(PREF_ACTIVE_ACCOUNT, null);
    }

    public static void setActiveAccountName(final Context context, final String name) {
        getSharedPreferences(context).edit()
                .putString(PREF_ACTIVE_ACCOUNT, name)
                .commit();
    }

    private static Account getActiveAccount(final Account[] accounts, final String activeAccountName) {
        for (Account account : accounts) {
            if (TextUtils.equals(account.name, activeAccountName)) {
                return account;
            }
        }
        return null;
    }

    private static SharedPreferences getSharedPreferences(final Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Saves an authorized account in account manager and set as active account.
     */
    public static void setAuthorizedAccount(Context context, UserCredentials credentials, AccessToken token, String authtokenType, String accountType) {
        final AccountManager accountManager = AccountManager.get(context);
        Account account = findOrCreateAccount(accountManager, credentials.getUsername(), token.getRefreshToken(), accountType);
        accountManager.setAuthToken(account, authtokenType, token.getAccessToken());
        setActiveAccountName(context, account.name);
    }

    /**
     * Sets password of account, creates a new account if necessary.
     */
    private static Account findOrCreateAccount(AccountManager accountManager, String username, String refreshToken, String accountType) {

        for (Account account : accountManager.getAccountsByType(accountType)) {
            if (account.name.equals(username)) {
                accountManager.setPassword(account, refreshToken);
                return account;
            }
        }

        Account account = new Account(username, accountType);
        accountManager.addAccountExplicitly(account, refreshToken, null);
        return account;
    }

}
