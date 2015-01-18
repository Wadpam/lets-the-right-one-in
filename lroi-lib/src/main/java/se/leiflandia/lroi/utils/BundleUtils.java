package se.leiflandia.lroi.utils;

import android.os.Bundle;
import android.os.Parcelable;

public class BundleUtils {
    public static Bundle createBundle(final String key, final boolean value) {
        final Bundle bundle = new Bundle();
        bundle.putBoolean(key, value);
        return bundle;
    }

    public static Bundle createBundle(final String key, final String value) {
        final Bundle bundle = new Bundle();
        bundle.putString(key, value);
        return bundle;
    }

    public static Bundle createBundle(final String key, final Parcelable value) {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(key, value);
        return bundle;
    }
}
