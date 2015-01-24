package se.leiflandia.sample.lroi;

import se.leiflandia.lroi.AuthAdapter;
import se.leiflandia.lroi.auth.AuthenticationService;

public class ExampleAuthenticationService extends AuthenticationService {

    @Override
    public AuthAdapter getAuthAdapter() {
        return ((App) getApplication()).getAuth();
    }
}
