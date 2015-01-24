package se.leiflandia.sample.lroi;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import se.leiflandia.lroi.AuthAdapter;
import se.leiflandia.lroi.auth.model.User;
import se.leiflandia.lroi.auth.model.UserCredentials;
import se.leiflandia.lroi.network.SigninFailure;
import se.leiflandia.lroi.network.SignupFailure;
import se.leiflandia.lroi.ui.AbstractLoginActivity;
import se.leiflandia.lroi.utils.Callback;


public class LoginActivity extends AbstractLoginActivity {

    private AuthAdapter auth;

    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = ((App) getApplication()).getAuth();

        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    public boolean validate(String email, String password) {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        boolean valid = true;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!checkPasswordFormat(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            valid = false;
        }

        // Check for a valid email address.
        if (!checkUsernameFormat(email) && checkEmailFormat(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            valid = false;
        }

        focusView.requestFocus();
        return valid;
    }

    public void attemptLogin() {

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        if (validate(email, password)) {
            showProgress(true);
            auth.signin(auth.createPasswordUserCredentials(email, password), new Callback<String, SigninFailure>() {
                @Override
                public void success(String response) {
                    finish();
                }

                @Override
                public void failure(SigninFailure failure) {
                    showProgress(false);
                    Toast.makeText(LoginActivity.this, "Login failed: " + failure.name(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(LoginActivity.this, "Something's wrong...", Toast.LENGTH_LONG).show();
        }
    }

    public void attemptRegistration() {

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        if (validate(email, password)) {
            showProgress(true);
            auth.signup(new User(email, password, email), new Callback<User, SignupFailure>() {
                @Override
                public void success(User response) {
                    Toast.makeText(LoginActivity.this, "Registration successful.", Toast.LENGTH_LONG).show();
                }

                @Override
                public void failure(SignupFailure failure) {
                    Toast.makeText(LoginActivity.this, "Registration failed: " + failure.name(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}



