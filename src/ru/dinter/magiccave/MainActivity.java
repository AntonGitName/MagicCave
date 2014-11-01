package ru.dinter.magiccave;

import ru.dinter.magiccave.IO.ResourceLoader;
import ru.dinter.magiccave.fragments.MainMenuFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class MainActivity extends FragmentActivity {

    public static final int UPDATE_RATE = 1000 / 30;
    private static final String MENU_FRAGMENT_TAG = "MENU_FRAGMENT_TAG";

    public ResourceLoader RESOURCE_LOADER;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        RESOURCE_LOADER = ResourceLoader.newInstance(getResources());
        
        if (savedInstanceState == null) {
            startFragment();
        }
    }

    private void startFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new MainMenuFragment(), MENU_FRAGMENT_TAG).commit();

    }
}
