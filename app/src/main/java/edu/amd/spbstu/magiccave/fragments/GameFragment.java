package edu.amd.spbstu.magiccave.fragments;

import android.app.Activity;
import android.content.Context;
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
import edu.amd.spbstu.magiccave.interfaces.OnCandleViewClickListener;
import edu.amd.spbstu.magiccave.interfaces.OnConnectionFinishedListener;
import edu.amd.spbstu.magiccave.interfaces.OnHelpAnimationFinishedListener;
import edu.amd.spbstu.magiccave.model.CandlePuzzle;
import edu.amd.spbstu.magiccave.model.CandlePuzzleBuilder;
import edu.amd.spbstu.magiccave.util.GameMode;
import edu.amd.spbstu.magiccave.views.GameView;

import static edu.amd.spbstu.magiccave.fragments.LevelChooseFragment.LEVEL_SEED_MAP;

/**
 * @author Anton
 * @since 23.02.2015
 */
public class GameFragment extends Fragment implements OnHelpAnimationFinishedListener
        , OnConnectionFinishedListener {

    public static final String TAG = "GameFragment";

    private static final String GAME_MODE_KEY = "GAME_MODE_KEY";
    private static final String GAME_LEVEL_KEY = "GAME_LEVEL_KEY";
    private static final String PUZZLE_KEY = "PUZZLE_KEY";

    private static final int DEFAULT_PUZZLE_SIZE = 7;
    private static final int DEFAULT_PUZZLE_ROW = 3;
    private static final int DEFAULT_PUZZLE_COL = 4;
    private static final int MAX_LEVEL = 9;
    private static final int MOVES_TO_ENABLE_HELP = 15;
    private static final int WRONG_CONNECTION_PENALTY = 5;

    private GameMode gameMode;
    private CandlePuzzle candlePuzzle;
    private String initialCandlePuzzle;
    private int moves;
    private int bestMoves;

    private GameView gameView;
    private TextView counterView;
    private OnGameInteractionListener onGameInteractionListener;
    private Button helpBtn;
    private int level;
    private boolean helpUsed;
    private Button menuBtn;
    private Button connectBtn;

    private ConnectionListener connectionListener;

    public GameFragment() {
        // Required empty public constructor
    }

    public static GameFragment newInstance(GameMode gameMode, int level) {
        if (gameMode == GameMode.RANDOM) {
            throw new IllegalArgumentException("use one argument method for this GameMode");
        }
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putInt(GAME_MODE_KEY, gameMode.getValue());
        args.putInt(GAME_LEVEL_KEY, level);
        fragment.setArguments(args);
        return fragment;
    }

    public static GameFragment newInstance(GameMode gameMode) {
        if (gameMode == GameMode.SCENARIO) {
            throw new IllegalArgumentException("use two arguments method for this GameMode");
        }
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putInt(GAME_MODE_KEY, gameMode.getValue());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            gameMode = GameMode.fromValue(savedInstanceState.getInt(GAME_MODE_KEY));
            level = savedInstanceState.getInt(GAME_MODE_KEY, 0);
            candlePuzzle = new CandlePuzzle(savedInstanceState.getString(PUZZLE_KEY));
            initialCandlePuzzle = candlePuzzle.toString();
            bestMoves = candlePuzzle.getSolution().size();
            Log.v(TAG, "Instance state loaded");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            gameMode = GameMode.fromValue(getArguments().getInt(GAME_MODE_KEY));
            if (gameMode == GameMode.SCENARIO) {
                this.level = getArguments().getInt(GAME_LEVEL_KEY);
            }
            candlePuzzle = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(GAME_MODE_KEY, gameMode.getValue());
        outState.putString(PUZZLE_KEY, candlePuzzle.toString());
        if (gameMode == GameMode.SCENARIO) {
            outState.putInt(GAME_LEVEL_KEY, level);
        }
        Log.v(TAG, "Instance state saved");
    }

    @Override
    public void onHelpAnimationFinished() {
        menuBtn.setEnabled(true);
        gameView.setEnabledCandles(true);
        checkIfPuzzleSolved();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_game, container, false);
        final Typeface type = MainApplication.RESOURCE_LOADER.getTypeface();

        gameView = (GameView) rootView.findViewById(R.id.game_view);
        if (candlePuzzle == null) {
            switch (gameMode) {
                case RANDOM:
                    candlePuzzle = CandlePuzzleBuilder.build(DEFAULT_PUZZLE_COL, DEFAULT_PUZZLE_ROW, DEFAULT_PUZZLE_SIZE);
                    break;
                case SCENARIO:
                    candlePuzzle = CandlePuzzleBuilder.build(LEVEL_SEED_MAP[level - 1]);
                    break;
            }
            initialCandlePuzzle = candlePuzzle.toString();
            bestMoves = candlePuzzle.getSolution().size();
        }
        helpBtn = (Button) rootView.findViewById(R.id.game_help_btn);
        helpBtn.setOnClickListener(new OnHelpButtonClickedListener());
        helpBtn.setTypeface(type);

        menuBtn = (Button) rootView.findViewById(R.id.game_menu_btn);
        menuBtn.setOnClickListener(new OnMenuButtonClickListener());
        menuBtn.setTypeface(type);

        connectBtn = (Button) rootView.findViewById(R.id.connect_btn);
        connectBtn.setTypeface(type);
        connectBtn.setOnClickListener(new OnConnectButtonClickedListener());

        counterView = (TextView) rootView.findViewById(R.id.touch_counter);

        connectionListener = new ConnectionListener(gameView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        resetGame();
    }

    public void onGameMenuButtonsClick(MenuDialogFragment.MenuButtonType type) {
        setEnabledButtons(true);
        if (moves < MOVES_TO_ENABLE_HELP || helpUsed) {
            helpBtn.setEnabled(false);
        }
        switch (type) {

            case RESUME:
                break;
            case RESTART:
                candlePuzzle = new CandlePuzzle(initialCandlePuzzle);
                resetGame();
                break;
            case MAIN_MENU:
                onGameInteractionListener.onGameInteraction();
                break;
        }
    }

    private void checkIfPuzzleSolved() {
        if (candlePuzzle.isSolved()) {
            final MenuDialogFragment menuDialogFragment = (MenuDialogFragment) getFragmentManager().findFragmentByTag(MenuDialogFragment.TAG);
            if (menuDialogFragment != null) {
                menuDialogFragment.dismiss();
            }
            gameView.setEnabledCandles(true);
            gameView.setLinesVisible(true);
            if (gameMode == GameMode.SCENARIO) {
                saveStars(level);
            }
            setEnabledButtons(false);
            WinDialogFragment.newInstance(moves, bestMoves).show(getFragmentManager(), WinDialogFragment.TAG);
        }
    }

    private void resetGame() {
        resetMoves();
        gameView.setPuzzle(candlePuzzle, connectionListener, this);
        setEnabledButtons(true);
        helpBtn.setEnabled(false);
        helpUsed = false;
    }

    private void saveStars(int level) {
        int stars;
        if (helpUsed) {
            stars = 1;
        } else if (moves == bestMoves) {
            stars = 3;
        } else {
            stars = 2;
        }
        if (getActivity().getPreferences(Context.MODE_PRIVATE).getInt(LevelChooseFragment.LEVEL_KEY + level, 0) < stars)
            getActivity().getPreferences(Context.MODE_PRIVATE).edit().putInt(LevelChooseFragment.LEVEL_KEY + level, stars).commit();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onGameInteractionListener = (OnGameInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnGameInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onGameInteractionListener = null;
    }

    public void onWinMenuButtonsClick(WinDialogFragment.WinMenuButtonType type) {
        setEnabledButtons(true);
        helpBtn.setEnabled(false);
        switch (type) {
            case RESTART:
                candlePuzzle = new CandlePuzzle(initialCandlePuzzle);
                resetGame();
                break;
            case NEXT:
                if (gameMode == GameMode.RANDOM) {
                    candlePuzzle = CandlePuzzleBuilder.build(DEFAULT_PUZZLE_COL, DEFAULT_PUZZLE_ROW, DEFAULT_PUZZLE_SIZE);
                } else {
                    ++level;
                    if (level > MAX_LEVEL) {
                        level = 1;
                    }
                    candlePuzzle = CandlePuzzleBuilder.build((long) level);
                }
                initialCandlePuzzle = candlePuzzle.toString();
                gameView.setPuzzle(candlePuzzle, connectionListener, this);
                resetMoves();
                bestMoves = candlePuzzle.getSolution().size();
                break;
            case MAIN_MENU:
                onGameInteractionListener.onGameInteraction();
                break;
        }
    }

    private void setEnabledButtons(boolean set) {
        helpBtn.setEnabled(set);
        menuBtn.setEnabled(set);
        connectBtn.setEnabled(set);
        gameView.setEnabledCandles(set);
    }

    @Override
    public void onConnectionFinished(boolean isSuccessfully) {
        connectionListener.setIsConnecting(false);
        setEnabledButtons(true);
        helpBtn.setEnabled(false);
        if (!isSuccessfully) {
            addMoves(WRONG_CONNECTION_PENALTY);
        } else {
            addMoves(0);
        }
    }

    private void addMoves(int movesToAdd) {
        moves += movesToAdd;
        counterView.setText("  " + String.valueOf(moves), TextView.BufferType.SPANNABLE);
        if (moves >= MOVES_TO_ENABLE_HELP && !helpUsed && !connectionListener.isConnecting()) {
            helpBtn.setEnabled(true);
        }
    }

    private void resetMoves() {
        moves = 0;
        counterView.setText("  ", TextView.BufferType.SPANNABLE);
    }

    public interface OnGameInteractionListener {
        void onGameInteraction();
    }

    public interface OnCandlesConnectedListener {

        void OnCandlesConnected(int firstCandleId, int secondCandleId);
    }

    private final class OnConnectButtonClickedListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            setEnabledButtons(false);
            gameView.setEnabledCandles(true);
            connectionListener.setIsConnecting(true);
        }
    }

    private final class OnHelpButtonClickedListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            setEnabledButtons(false);
            gameView.showHelpAnimation(GameFragment.this.candlePuzzle.getSolution(), GameFragment.this);
            gameView.setLinesVisible(true);
            helpUsed = true;
        }
    }

    private final class OnMenuButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            setEnabledButtons(false);
            MenuDialogFragment.newInstance().show(GameFragment.this.getFragmentManager(), MenuDialogFragment.TAG);
        }
    }

    private class ConnectionListener implements OnCandleViewClickListener {

        private static final int NOT_USED = -1;
        private final OnCandlesConnectedListener listener;
        private boolean isConnecting;
        private int firstCandleId;

        public ConnectionListener(OnCandlesConnectedListener listener) {
            this.listener = listener;
        }

        @Override
        public void onCandleViewClick(int id, boolean needCheck) {
            if (isConnecting) {
                if (firstCandleId == NOT_USED) {
                    firstCandleId = id;
                } else {
                    listener.OnCandlesConnected(firstCandleId, id);
                    setIsConnecting(false);
                }
            } else {
                addMoves(1);
                if (needCheck) {
                    checkIfPuzzleSolved();
                }
            }

        }

        @Override
        public boolean isConnecting() {
            return isConnecting;
        }

        public void setIsConnecting(boolean isConnecting) {
            this.firstCandleId = NOT_USED;
            this.isConnecting = isConnecting;
        }
    }
}
