package edu.amd.spbstu.magiccave;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.spbstu.appmathdep.AboutActivity;

import edu.amd.spbstu.magiccave.fragments.GameFragment;
import edu.amd.spbstu.magiccave.fragments.LevelButtonFragment;
import edu.amd.spbstu.magiccave.fragments.LevelChooseFragment;
import edu.amd.spbstu.magiccave.fragments.MainMenuFragment;
import edu.amd.spbstu.magiccave.fragments.MenuDialogFragment;
import edu.amd.spbstu.magiccave.fragments.RulesPageFragment;
import edu.amd.spbstu.magiccave.fragments.WinDialogFragment;
import edu.amd.spbstu.magiccave.util.GameMode;

/**
 * @author Anton
 * @since 22.02.2015
 */
public class MainMenuActivity extends FragmentActivity implements MainMenuFragment.OnMainMenuOptionSelectedListener
        , GameFragment.OnGameInteractionListener
        , MenuDialogFragment.OnGameMenuButtonsClickListener
        , WinDialogFragment.OnWinMenuButtonsClickListener
        , LevelButtonFragment.OnLevelButtonClickListener {

    private static final String TAG = "MainMenuActivity";
    private static final String SOUND_SWITCH_KEY = "SOUND_SWITCH_KEY";

    private MediaPlayer mediaPlayer;
    private boolean isMusicPlaying;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SOUND_SWITCH_KEY, isMusicPlaying);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu);

        if (savedInstanceState == null) {
            isMusicPlaying = true;
            startFragment();
        } else {
            isMusicPlaying = savedInstanceState.getBoolean(SOUND_SWITCH_KEY);
        }
    }

    private void startFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, MainMenuFragment.newInstance(isMusicPlaying), MainMenuFragment.TAG).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.w(TAG, "Starting player");
        mediaPlayer = MediaPlayer.create(this, R.raw.background_music);
        mediaPlayer.setLooping(true);
        if (isMusicPlaying) {
            mediaPlayer.start();
        }
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
            case CHOOSE_LEVEL:
                onChooseLevelButtonClicked();
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
            case SOUND:
                isMusicPlaying = !isMusicPlaying;
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                } else {
                    mediaPlayer.start();
                }
                break;
            case EXIT:
                finish();
                break;
        }
    }

    private void onChooseLevelButtonClicked() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, LevelChooseFragment.newInstance(), LevelChooseFragment.TAG)
                .addToBackStack(MainMenuFragment.TAG)
                .commit();
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
    public void onGameInteraction() {
        DialogFragment dialogFragment = (DialogFragment) getSupportFragmentManager().findFragmentByTag(MenuDialogFragment.TAG);
        if (dialogFragment != null) {
            dialogFragment.dismiss();
        }
        dialogFragment = (DialogFragment) getSupportFragmentManager().findFragmentByTag(WinDialogFragment.TAG);
        if (dialogFragment != null) {
            dialogFragment.dismiss();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, MainMenuFragment.newInstance(isMusicPlaying), MainMenuFragment.TAG).commit();
    }

    @Override
    public void onGameMenuButtonsClick(MenuDialogFragment.MenuButtonType type) {
        final GameFragment gameFragment = (GameFragment) getSupportFragmentManager().findFragmentByTag(GameFragment.TAG);
        if (gameFragment != null) {
            gameFragment.onGameMenuButtonsClick(type);
        }
    }

    @Override
    public void onWinMenuButtonsClick(WinDialogFragment.WinMenuButtonType type) {
        final GameFragment gameFragment = (GameFragment) getSupportFragmentManager().findFragmentByTag(GameFragment.TAG);
        if (gameFragment != null) {
            gameFragment.onWinMenuButtonsClick(type);
        }
    }

    @Override
    public void onLevelButtonClick(int level) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, GameFragment.newInstance(GameMode.SCENARIO, level), GameFragment.TAG)
                .addToBackStack(MainMenuFragment.TAG)
                .commit();
    }
}
