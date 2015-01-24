package se.leiflandia.sample.lroi;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import se.leiflandia.lroi.AuthAdapter;

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
        ((TextView) findViewById(R.id.message)).setText("Successfully authenticated user.");
    }
}
