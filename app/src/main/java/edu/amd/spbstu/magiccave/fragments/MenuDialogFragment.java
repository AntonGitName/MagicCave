package edu.amd.spbstu.magiccave.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import edu.amd.spbstu.magiccave.R;
import edu.amd.spbstu.magiccave.util.GameMode;

/**
 * @author iAnton
 * @since 10/03/15
 */
public class MenuDialogFragment extends DialogFragment {

    public static final String TAG = "MenuDialogFragment";

    private static final String GAME_MODE_KEY = "GAME_MODE_KEY";

    private GameMode mGameMode;
    private OnGameMenuButtonsClickListener listener;

    public static MenuDialogFragment newInstance(GameMode gameMode) {
        MenuDialogFragment fragment = new MenuDialogFragment();
        Bundle args = new Bundle();
        args.putInt(GAME_MODE_KEY, gameMode.getValue());
        fragment.setArguments(args);
        return fragment;
    }

    public MenuDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGameMode = GameMode.fromValue(getArguments().getInt(GAME_MODE_KEY));
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnGameMenuButtonsClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMainMenuOptionSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_game_menu, container, false);

        rootView.findViewById(R.id.resume_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onGameMenuButtonsClick(MenuButtonType.RESUME);
                MenuDialogFragment.this.dismiss();
            }
        });

        rootView.findViewById(R.id.restart_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onGameMenuButtonsClick(MenuButtonType.RESTART);
                MenuDialogFragment.this.dismiss();
            }
        });

        rootView.findViewById(R.id.main_menu_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onGameMenuButtonsClick(MenuButtonType.MAIN_MENU);
                MenuDialogFragment.this.dismiss();
            }
        });

        return rootView;
    }

    public interface OnGameMenuButtonsClickListener {
        void onGameMenuButtonsClick(MenuButtonType type);
    }

    public enum MenuButtonType {
        RESUME, RESTART, MAIN_MENU
    }
}
