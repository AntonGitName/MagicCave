package ru.dinter.antonpp.magiccave;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.spbstu.appmathdep.AboutActivity;

import ru.dinter.antonpp.magiccave.fragments.MainMenuFragment;
import ru.dinter.antonpp.magiccave.fragments.RulesPageFragment;

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
    public void onMainMenuOptionSelected(MainMenuFragment.MAIN_MENU_OPTION option) {
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
                finish();
                break;
        }
    }

    private void onAboutButtonClicked() {
        startActivity(new Intent(this, AboutActivity.class));
    }

    @Override
    public void onRulesPageInteraction() {
        // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, MainMenuFragment.newInstance(), MainMenuFragment.TAG).commit();
    }
}
