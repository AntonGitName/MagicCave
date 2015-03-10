package edu.amd.spbstu.magiccave;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.spbstu.appmathdep.AboutActivity;

import edu.amd.spbstu.magiccave.fragments.GameFragment;
import edu.amd.spbstu.magiccave.fragments.MainMenuFragment;
import edu.amd.spbstu.magiccave.fragments.RulesPageFragment;
import edu.amd.spbstu.magiccave.util.GameMode;

/**
 * Created by Anton on 22.02.2015.
 */
public class MainMenuActivity extends FragmentActivity implements MainMenuFragment.OnMainMenuOptionSelectedListener
        , RulesPageFragment.OnRulesPageInteractionListener
        , GameFragment.OnGameInteractionListener {

    public static final String TAG = "MainMenuActivity";


    private MediaPlayer mediaPlayer;

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
    protected void onStart() {
        super.onStart();

        Log.w(TAG, "Starting player");
        mediaPlayer = MediaPlayer.create(this, R.raw.background_music);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    @Override
    public void onMainMenuOptionSelected(MainMenuFragment.MainMenuOption option) {
        switch (option){

            case SCENARIO:
                break;
            case RANDOM:
                onRandomButtonClicked();
                break;
            case RULES:
                onRulesButtonClicked();
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

    private void onRandomButtonClicked() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, GameFragment.newInstance(GameMode.RANDOM), GameFragment.TAG)
                .addToBackStack(MainMenuFragment.TAG)
                .commit();
    }

    private void onRulesButtonClicked() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, RulesPageFragment.newInstance(), RulesPageFragment.TAG)
                .addToBackStack(MainMenuFragment.TAG)
                .commit();
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

    @Override
    public void onGameInteraction(Uri uri) {

    }
}
