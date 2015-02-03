package se.leiflandia.lroi.ui;

import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;

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

    public static boolean checkEmailFormat(String email) {
        return email != null && email.contains("@") && email.length() >= 3;
    }

    public static boolean checkUsernameFormat(String username) {
        return username != null && username.matches("^.{5,}$");
    }

    public static boolean checkPasswordFormat(String password) {
        return password != null && password.matches("^[^\\x00-\\x1F]{5,}$");
    }

}
