package ru.dinter.magiccave;

import ru.dinter.magiccave.IO.ResourceLoader;
import ru.dinter.magiccave.UI.MainMenuFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class MainActivity extends FragmentActivity {

    public static final int UPDATE_RATE = 1000 / 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            startFragment();
        }
    }

    private void startFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new MainMenuFragment()).commit();

    }
}
