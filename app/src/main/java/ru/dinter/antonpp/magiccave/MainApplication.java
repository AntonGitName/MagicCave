package ru.dinter.antonpp.magiccave;

import android.app.Application;

import ru.dinter.antonpp.magiccave.IO.ResourceLoader;

/**
 * Created by Anton on 23.02.2015.
 */
public class MainApplication extends Application {

    public static ResourceLoader RESOURCE_LOADER;

    @Override
    public void onCreate() {
        super.onCreate();

        RESOURCE_LOADER = new ResourceLoader(getResources(), getAssets());
    }
}
