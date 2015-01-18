package se.leiflandia.lroi.auth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public abstract class AuthenticationService extends Service {
    private AccountAuthenticator authenticator;

    public abstract AccountAuthenticator getAuthenticator();

    @Override
    public void onCreate() {
        authenticator = getAuthenticator();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return authenticator.getIBinder();
    }
}
