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
import android.widget.TextView;

import edu.amd.spbstu.magiccave.MainApplication;
import edu.amd.spbstu.magiccave.R;
import edu.amd.spbstu.magiccave.model.CandlePuzzle;
import edu.amd.spbstu.magiccave.model.CandlePuzzleBuilder;
import edu.amd.spbstu.magiccave.util.GameMode;
import edu.amd.spbstu.magiccave.views.CandleView;
import edu.amd.spbstu.magiccave.views.GameView;

/**
 * @author Anton
 * @since 23.02.2015
 */
public class GameFragment extends Fragment implements CandleView.OnCandleViewClickListener, GameView.OnHelpAnimationFinishedListener {

    public static final String TAG = "GameFragment";

    private static final String GAME_MODE_KEY = "GAME_MODE_KEY";
    private static final String PUZZLE_KEY = "PUZZLE_KEY";

    private static final int DEFAULT_PUZZLE_SIZE = 7;

    private GameMode mGameMode;
    private CandlePuzzle candlePuzzle;
    private String initialCandlePuzzle;
    private int moves;
    private int bestMoves;

    private GameView gameView;
    private TextView counterView;
    private OnGameInteractionListener mListener;
    private Button helpBtn;

    public static GameFragment newInstance(GameMode gameMode) {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putInt(GAME_MODE_KEY, gameMode.getValue());
        fragment.setArguments(args);
        return fragment;
    }

    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mGameMode = GameMode.fromValue(savedInstanceState.getInt(GAME_MODE_KEY));
            candlePuzzle = new CandlePuzzle(savedInstanceState.getString(PUZZLE_KEY));
            initialCandlePuzzle = candlePuzzle.toString();
            moves = 0;
            bestMoves = candlePuzzle.getSolution().size();
            Log.v(TAG, "Instance state loaded");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mGameMode = GameMode.fromValue(getArguments().getInt(GAME_MODE_KEY));
            candlePuzzle = null;
            moves = 0;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(GAME_MODE_KEY, mGameMode.getValue());
        outState.putString(PUZZLE_KEY, candlePuzzle.toString());
        Log.v(TAG, "Instance state saved");
    }

    @Override
    public void onHelpAnimationFinished() {
        helpBtn.setEnabled(true);
        gameView.setEnabledCandles(true);
        checkIfPuzzleSolved();
    }

    private final class OnHelpButtonClickedListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            helpBtn.setEnabled(false);
            gameView.setEnabledCandles(false);
            gameView.showHelpAnimation(GameFragment.this.candlePuzzle.getSolution(), GameFragment.this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_game, container, false);
        final Typeface type = MainApplication.RESOURCE_LOADER.getTypeface();

        gameView = (GameView) rootView.findViewById(R.id.game_view);
        if (candlePuzzle == null) {
            candlePuzzle = CandlePuzzleBuilder.build(GameView.COLUMNS, GameView.ROWS, DEFAULT_PUZZLE_SIZE);
            initialCandlePuzzle = candlePuzzle.toString();
            moves = 0;
            bestMoves = candlePuzzle.getSolution().size();
        }
        gameView.setPuzzle(candlePuzzle, this);

        helpBtn = (Button) rootView.findViewById(R.id.game_help_btn);
        helpBtn.setOnClickListener(new OnHelpButtonClickedListener());
        helpBtn.setTypeface(type);
        final Button menuBtn = (Button) rootView.findViewById(R.id.game_menu_btn);
        menuBtn.setOnClickListener(new OnMenuButtonClickListener());
        menuBtn.setTypeface(type);
        counterView = (TextView) rootView.findViewById(R.id.touch_counter);

        return rootView;
    }

    public void onGameMenuButtonsClick(MenuDialogFragment.MenuButtonType type) {
        switch (type) {

            case RESUME:
                break;
            case RESTART:
                candlePuzzle = new CandlePuzzle(initialCandlePuzzle);
                gameView.setPuzzle(candlePuzzle, this);
                moves = 0;
                counterView.setText("  " + String.valueOf(moves), TextView.BufferType.SPANNABLE);
                break;
            case MAIN_MENU:
                mListener.onGameInteraction();
                break;
        }
    }

    @Override
    public void onCandleViewClick(boolean needCheck) {
        counterView.setText("  " + String.valueOf(++moves), TextView.BufferType.SPANNABLE);
        if (needCheck) {
            checkIfPuzzleSolved();
        }
    }

    private void checkIfPuzzleSolved() {
        if (candlePuzzle.isSolved()) {
            final MenuDialogFragment menuDialogFragment = (MenuDialogFragment) getFragmentManager().findFragmentByTag(MenuDialogFragment.TAG);
            if (menuDialogFragment != null) {
                menuDialogFragment.dismiss();
            }
            WinDialogFragment.newInstance(mGameMode, moves, bestMoves).show(getFragmentManager(), WinDialogFragment.TAG);
        }
    }

    private final class OnMenuButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            MenuDialogFragment.newInstance(mGameMode).show(GameFragment.this.getFragmentManager(), MenuDialogFragment.TAG);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnGameInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnGameInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnGameInteractionListener {
        void onGameInteraction();
    }

    public void onWinMenuButtonsClick(WinDialogFragment.WinMenuButtonType type) {
        switch (type) {
            case RESTART:
                candlePuzzle = new CandlePuzzle(initialCandlePuzzle);
                gameView.setPuzzle(candlePuzzle, this);
                moves = 0;
                counterView.setText("  " + String.valueOf(moves), TextView.BufferType.SPANNABLE);
                break;
            case NEXT:
                candlePuzzle = CandlePuzzleBuilder.build(GameView.COLUMNS, GameView.ROWS, DEFAULT_PUZZLE_SIZE);
                initialCandlePuzzle = candlePuzzle.toString();
                gameView.setPuzzle(candlePuzzle, this);
                moves = 0;
                bestMoves = candlePuzzle.getSolution().size();
                counterView.setText("  " + String.valueOf(moves), TextView.BufferType.SPANNABLE);
                break;
            case MAIN_MENU:
                mListener.onGameInteraction();
                break;
        }
    }
}
