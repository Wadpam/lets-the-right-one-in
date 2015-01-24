package se.leiflandia.lroi.utils;

public class Utils {
    public static void checkNotNull(Object o) {
        if (o == null) {
            throw new IllegalArgumentException();
        }
    }
}
