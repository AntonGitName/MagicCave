package edu.amd.spbstu.magiccave.IO;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.Log;

/**
 * Created by Anton on 23.02.2015.
 */
public class ResourceLoader {

    public static final String TAG = "ResourceLoader";
    private static final String TYPEFACE_FILENAME = "fonts/Parchment MF.ttf";

    private final Typeface typeface;

    public ResourceLoader(Resources resources, AssetManager assetManager) {

        Log.d(TAG, "Resource loader created");

        typeface = Typeface.createFromAsset(assetManager, TYPEFACE_FILENAME);
    }

    public Typeface getTypeface() {
        return typeface;
    }
}
