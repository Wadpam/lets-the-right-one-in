package se.leiflandia.sample.lroi;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import se.leiflandia.lroi.AuthAdapter;
import se.leiflandia.lroi.network.SignoutFailure;
import se.leiflandia.lroi.utils.Callback;

public class MainActivity extends ActionBarActivity {

    private AuthAdapter auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = ((App) getApplication()).getAuth();
        if (!auth.isSignedIn()) {
            startActivity(auth.getLoginActivityIntent(this));
        }

        setContentView(R.layout.activity_main);
        ((TextView) findViewById(R.id.message)).setText("Successfully authenticated user (hopefully).");

        findViewById(R.id.signout_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signout(false, new Callback<Void, SignoutFailure>() {
                    @Override
                    public void success(Void response) {
                        startActivity(auth.getLoginActivityIntent(MainActivity.this));
                    }

                    @Override
                    public void failure(SignoutFailure failure) {
                        Toast.makeText(MainActivity.this, "Failed signout.", Toast.LENGTH_LONG).show();                    }
                });
            }
        });
    }
}
