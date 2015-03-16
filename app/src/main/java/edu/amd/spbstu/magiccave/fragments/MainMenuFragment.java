package edu.amd.spbstu.magiccave.fragments;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import edu.amd.spbstu.magiccave.MainApplication;
import edu.amd.spbstu.magiccave.R;

/**
 * @author Anton
 * @since 23.02.2015
 */
public class MainMenuFragment extends Fragment {

    public static final String TAG = "MainMenuFragment";
    private static final String SOUND_SWITCH_KEY = "SOUND_SWITCH_KEY";

    private OnMainMenuOptionSelectedListener mListener;
    private int soundImageResource;

    public MainMenuFragment() {
        // Required empty public constructor
    }

    public static MainMenuFragment newInstance(boolean isSoundOn) {
        MainMenuFragment fragment = new MainMenuFragment();
        Bundle args = new Bundle();
        final int res = isSoundOn ? R.drawable.ic_action_volume_on : R.drawable.ic_action_volume_muted;
        args.putInt(SOUND_SWITCH_KEY, res);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        soundImageResource = R.drawable.ic_action_volume_on;
        if (savedInstanceState != null) {
            soundImageResource = savedInstanceState.getInt(SOUND_SWITCH_KEY);
        }
        if (getArguments() != null) {
            soundImageResource = getArguments().getInt(SOUND_SWITCH_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_main_menu, container, false);

        Log.d(TAG, "Setting up main menu buttons");

        final Typeface type = MainApplication.RESOURCE_LOADER.getTypeface();

        final ImageView soundSwitchBtn = (ImageView) rootView.findViewById(R.id.image_view);
        soundSwitchBtn.setImageDrawable(getResources().getDrawable(soundImageResource));

        soundSwitchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onMainMenuOptionSelected(MainMenuOption.SOUND);
                soundImageResource = soundImageResource == R.drawable.ic_action_volume_on ? R.drawable.ic_action_volume_muted : R.drawable.ic_action_volume_on;
                soundSwitchBtn.setImageDrawable(getResources().getDrawable(soundImageResource));
            }
        });

        final Button chsLvlButton = (Button) rootView.findViewById(R.id.choose_level_btn);
        final Button rndLvlButton = (Button) rootView.findViewById(R.id.random_level_btn);
        final Button rulesButton = (Button) rootView.findViewById(R.id.rules_btn);
        final Button aboutButton = (Button) rootView.findViewById(R.id.about_btn);
        final Button quitButton = (Button) rootView.findViewById(R.id.quit_btn);
        final Button[] buttons = {chsLvlButton, rulesButton, aboutButton, quitButton, rndLvlButton};

        for (Button button: buttons) {
            button.setTypeface(type, Typeface.BOLD);
        }
        ((TextView) rootView.findViewById(R.id.main_menu_label)).setTypeface(type, Typeface.BOLD);

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMainMenuOptionSelected(MainMenuOption.ABOUT);
            }
        });

        chsLvlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMainMenuOptionSelected(MainMenuOption.SCENARIO);
            }
        });

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMainMenuOptionSelected(MainMenuOption.EXIT);
            }
        });

        rulesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMainMenuOptionSelected(MainMenuOption.RULES);
            }
        });

        rndLvlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMainMenuOptionSelected(MainMenuOption.RANDOM);
            }
        });

        initCandleAnimation(rootView);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(SOUND_SWITCH_KEY, soundImageResource);
    }

    private void initCandleAnimation(View rootView) {
//        AnimationDrawable candleAnimation;
//        ImageView candle = (ImageView) rootView.findViewById(R.id.candleViewLeft);
//        candleAnimation = (AnimationDrawable) candle.getDrawable();
//        candleAnimation.start();
//        candle = (ImageView) rootView.findViewById(R.id.candleViewRight);
//        candleAnimation = (AnimationDrawable) candle.getDrawable();
//        candleAnimation.start();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnMainMenuOptionSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMainMenuOptionSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public enum MainMenuOption {
        SCENARIO,
        RANDOM,
        RULES,
        ABOUT,
        SOUND,
        EXIT
    }

    public interface OnMainMenuOptionSelectedListener {
        void onMainMenuOptionSelected(MainMenuOption option);
    }

}
