package se.leiflandia.lroi.auth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import se.leiflandia.lroi.AuthAdapter;

public abstract class AuthenticationService extends Service {
    public abstract AuthAdapter getAuthAdapter();

    @Override
    public IBinder onBind(Intent intent) {
        return getAuthAdapter().getAccountAuthenticator().getIBinder();
    }
}
