package ru.dinter.magiccave;

import android.app.Application;

import ru.dinter.magiccave.IO.ResourceLoader;

/**
 * Created by Anton on 06/12/14.
 */
public class MainApplication extends Application {

    public static ResourceLoader RESOURCE_LOADER;

    public static final String FONT_FILE = "fonts/Parchment MF.ttf";

    @Override
    public void onCreate() {
        super.onCreate();
        RESOURCE_LOADER = new ResourceLoader(getResources());
    }

}
