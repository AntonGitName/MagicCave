package edu.amd.spbstu.magiccave.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import edu.amd.spbstu.magiccave.MainApplication;
import edu.amd.spbstu.magiccave.R;

/**
 * @author iAnton
 * @since 15/03/15
 */
public class WinDialogFragment extends DialogFragment {

    public static final String TAG = WinDialogFragment.class.getSimpleName();

    private static final String GAME_MODE_KEY = "GAME_MODE_KEY";
    private static final String MOVES_KEY = "MOVES_KEY";
    private static final String BEST_MOVES_KEY = "BEST_MOVES_KEY";

    private int moves;
    private int bestMoves;
    private OnWinMenuButtonsClickListener listener;

    public WinDialogFragment() {
        // Required empty public constructor
    }

    public static WinDialogFragment newInstance(int moves, int bestMoves) {
        WinDialogFragment fragment = new WinDialogFragment();
        Bundle args = new Bundle();
        args.putInt(MOVES_KEY, moves);
        args.putInt(BEST_MOVES_KEY, bestMoves);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            moves = getArguments().getInt(MOVES_KEY);
            bestMoves = getArguments().getInt(BEST_MOVES_KEY);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnWinMenuButtonsClickListener) activity;
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
        final View rootView = inflater.inflate(R.layout.fragment_win_menu, container, false);
        final String winText = String.format(getString(R.string.game_win_text), moves, bestMoves);
        final TextView winTextView = (TextView) rootView.findViewById(R.id.game_win_text);
        final Button nextButton = (Button) rootView.findViewById(R.id.next_btn);
        final Button restartButton = (Button) rootView.findViewById(R.id.restart_btn);
        final Button mainMenuButton = (Button) rootView.findViewById(R.id.main_menu_btn);
        final Button[] buttons = {nextButton, restartButton, mainMenuButton};

        winTextView.setText(winText);
        winTextView.setTypeface(type);

        for (Button button : buttons) {
            button.setTypeface(type, Typeface.BOLD);
        }

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onWinMenuButtonsClick(WinMenuButtonType.RESTART);
                WinDialogFragment.this.dismiss();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onWinMenuButtonsClick(WinMenuButtonType.NEXT);
                WinDialogFragment.this.dismiss();
            }
        });

        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onWinMenuButtonsClick(WinMenuButtonType.MAIN_MENU);
                WinDialogFragment.this.dismiss();
            }
        });
        ((TextView) rootView.findViewById(R.id.game_win_label)).setTypeface(type);

        return rootView;
    }

    public enum WinMenuButtonType {
        RESTART, NEXT, MAIN_MENU
    }

    public interface OnWinMenuButtonsClickListener {
        void onWinMenuButtonsClick(WinMenuButtonType type);
    }
}
