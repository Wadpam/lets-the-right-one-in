package se.leiflandia.sample.lroi;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import se.leiflandia.lroi.AuthAdapter;
import se.leiflandia.lroi.auth.model.DUser;
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

        findViewById(R.id.signin_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        findViewById(R.id.register_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegistration();
            }
        });
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

        if (focusView != null) focusView.requestFocus();
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
            DUser user = new DUser.Builder()
                    .setUsername(email)
                    .setPassword(password)
                    .setEmail(email)
                    .build();
            auth.signup(user, new Callback<DUser, SignupFailure>() {
                @Override
                public void success(DUser response) {
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



