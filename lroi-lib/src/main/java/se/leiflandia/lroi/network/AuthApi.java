package se.leiflandia.lroi.network;

import com.google.gson.JsonElement;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import se.leiflandia.lroi.auth.model.AccessToken;
import se.leiflandia.lroi.auth.model.RefreshTokenCredentials;
import se.leiflandia.lroi.auth.model.ResetPassword;
import se.leiflandia.lroi.auth.model.RevocationRequest;
import se.leiflandia.lroi.auth.model.DUser;
import se.leiflandia.lroi.auth.model.UserCredentials;

public interface AuthApi {
    @POST("/oauth/authorize")
    public AccessToken authorize(@Body UserCredentials credentials);

    @POST("/oauth/authorize")
    public void authorize(@Body UserCredentials credentials, Callback<AccessToken> callback);

    @POST("/oauth/refresh")
    public AccessToken refreshAccessToken(@Body RefreshTokenCredentials credentials);

    @POST("/api/user")
    public void signup(@Body DUser user, Callback<String> callback);

    @POST("/oauth/revoke")
    public void revoke(@Body RevocationRequest request, Callback<String> callback);

    @GET("/api/user/me")
    public void me(Callback<JsonElement> callback);

    @POST("/api/user/password/reset")
    public void resetPassword(@Body ResetPassword request, Callback<JsonElement> callback);
}
