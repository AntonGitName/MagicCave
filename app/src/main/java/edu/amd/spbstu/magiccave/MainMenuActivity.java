package edu.amd.spbstu.magiccave;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.spbstu.appmathdep.AboutActivity;

import edu.amd.spbstu.magiccave.fragments.MainMenuFragment;
import edu.amd.spbstu.magiccave.fragments.RulesPageFragment;

/**
 * Created by Anton on 22.02.2015.
 */
public class MainMenuActivity extends FragmentActivity implements MainMenuFragment.OnMainMenuOptionSelectedListener, RulesPageFragment.OnRulesPageInteractionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu);

        if (savedInstanceState == null) {
            startFragment();
        }
    }

    private void startFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, MainMenuFragment.newInstance(), MainMenuFragment.TAG).commit();
    }

    @Override
    public void onMainMenuOptionSelected(MainMenuFragment.MainMenuOption option) {
        switch (option){

            case SCENARIO:
                break;
            case RANDOM:
                break;
            case RULES:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, RulesPageFragment.newInstance(), RulesPageFragment.TAG).addToBackStack(MainMenuFragment.TAG).commit();
                break;
            case ABOUT:
                onAboutButtonClicked();
                break;
            case EXIT:
                // android.os.Process.killProcess(android.os.Process.myPid());
                finish();
                break;
        }
    }

    private void onAboutButtonClicked() {
        Intent intent = new Intent(this, AboutActivity.class);
        intent.putExtra(AboutActivity.ABOUT_ACTIVITY_MODE_KEY, false);
        startActivity(intent);
    }

    @Override
    public void onRulesPageInteraction() {
        // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, MainMenuFragment.newInstance(), MainMenuFragment.TAG).commit();
    }
}
