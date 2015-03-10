package edu.amd.spbstu.magiccave.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.amd.spbstu.magiccave.R;
import edu.amd.spbstu.magiccave.util.GameMode;

/**
 * Created by iAnton on 10/03/15.
 */
public class MenuDialogFragment extends DialogFragment {

    public static final String TAG = "MenuDialogFragment";

    private static final String GAME_MODE_KEY = "GAME_MODE_KEY";

    private GameMode mGameMode;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_game_menu, container, false);

        return rootView;
    }
}
