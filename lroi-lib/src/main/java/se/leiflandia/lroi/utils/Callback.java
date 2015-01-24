package se.leiflandia.lroi.utils;

public interface Callback<S,F> {
    public void success(S response);
    public void failure(F failure);
}
