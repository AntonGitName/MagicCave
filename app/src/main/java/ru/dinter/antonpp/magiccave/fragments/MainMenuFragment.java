package ru.dinter.antonpp.magiccave.fragments;

import android.app.Activity;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ru.dinter.antonpp.magiccave.MainApplication;
import ru.dinter.antonpp.magiccave.R;

/**
 * Created by Anton on 23.02.2015.
 */
public class MainMenuFragment extends Fragment {
    public static final String TAG = "MainMenuFragment";

    private OnMainMenuOptionSelectedListener mListener;

    public static MainMenuFragment newInstance() {
        MainMenuFragment fragment = new MainMenuFragment();
        return fragment;
    }

    public MainMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_main_menu, container, false);

        Log.d(TAG, "Setting up main menu buttons");

        final Typeface type = MainApplication.RESOURCE_LOADER.getTypeface();

        final Button chsLvlButton = (Button) rootView.findViewById(R.id.choose_level_btn);
        final Button rndLvlButton = (Button) rootView.findViewById(R.id.random_level_btn);
        final Button rulesButton = (Button) rootView.findViewById(R.id.rules_btn);
        final Button quitButton = (Button) rootView.findViewById(R.id.quit_btn);
        Button[] buttons = {chsLvlButton, rulesButton, quitButton, rndLvlButton};

        for (Button button: buttons) {
            button.setTypeface(type, Typeface.BOLD);
        }
        ((TextView) rootView.findViewById(R.id.main_menu_label)).setTypeface(type, Typeface.BOLD_ITALIC);

        chsLvlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMainMenuOptionSelected(MAIN_MENU_OPTION.SCENARIO);
            }
        });

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMainMenuOptionSelected(MAIN_MENU_OPTION.EXIT);
            }
        });

        rulesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMainMenuOptionSelected(MAIN_MENU_OPTION.RULES);
            }
        });

        rndLvlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMainMenuOptionSelected(MAIN_MENU_OPTION.RANDOM);
            }
        });

        initCandleAnimation(rootView);

        return rootView;
    }

    private void initCandleAnimation(View rootView) {
        AnimationDrawable candleAnimation;
        ImageView candle = (ImageView) rootView.findViewById(R.id.candleViewLeft);
        candleAnimation = (AnimationDrawable) candle.getDrawable();
        candleAnimation.start();
        candle = (ImageView) rootView.findViewById(R.id.candleViewRight);
        candleAnimation = (AnimationDrawable) candle.getDrawable();
        candleAnimation.start();
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

    public enum MAIN_MENU_OPTION {
        SCENARIO,
        RANDOM,
        RULES,
        ABOUT,
        EXIT
    }

    public interface OnMainMenuOptionSelectedListener {
        public void onMainMenuOptionSelected(MAIN_MENU_OPTION option);
    }

}
