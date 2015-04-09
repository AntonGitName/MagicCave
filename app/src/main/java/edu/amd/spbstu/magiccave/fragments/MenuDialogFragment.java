package edu.amd.spbstu.magiccave.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import edu.amd.spbstu.magiccave.MainApplication;
import edu.amd.spbstu.magiccave.R;

/**
 * @author iAnton
 * @since 10/03/15
 */
public class MenuDialogFragment extends DialogFragment {

    public static final String TAG = "MenuDialogFragment";

    private static final String GAME_MODE_KEY = "GAME_MODE_KEY";

    private OnGameMenuButtonsClickListener listener;

    public MenuDialogFragment() {
        // Required empty public constructor
    }

    public static MenuDialogFragment newInstance() {
        return new MenuDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });
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
        final Typeface type = MainApplication.RESOURCE_LOADER.getTypeface();
        final View rootView = inflater.inflate(R.layout.fragment_game_menu, container, false);

        final Button resumeButton = (Button) rootView.findViewById(R.id.resume_btn);
        final Button restartButton = (Button) rootView.findViewById(R.id.restart_btn);
        final Button mainMenuButton = (Button) rootView.findViewById(R.id.main_menu_btn);
        final Button[] buttons = {resumeButton, restartButton, mainMenuButton};

        for (Button button : buttons) {
            button.setTypeface(type, Typeface.BOLD);
        }

        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onGameMenuButtonsClick(MenuButtonType.RESUME);
                MenuDialogFragment.this.dismiss();
            }
        });

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onGameMenuButtonsClick(MenuButtonType.RESTART);
                MenuDialogFragment.this.dismiss();
            }
        });

        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onGameMenuButtonsClick(MenuButtonType.MAIN_MENU);
                MenuDialogFragment.this.dismiss();
            }
        });
        return rootView;
    }

    public enum MenuButtonType {
        RESUME, RESTART, MAIN_MENU
    }

    public interface OnGameMenuButtonsClickListener {
        void onGameMenuButtonsClick(MenuButtonType type);
    }
}
