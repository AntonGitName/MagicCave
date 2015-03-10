package edu.amd.spbstu.magiccave.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.amd.spbstu.magiccave.R;
import edu.amd.spbstu.magiccave.model.CandlePuzzleBuilder;
import edu.amd.spbstu.magiccave.util.GameMode;
import edu.amd.spbstu.magiccave.views.GameView;

/**
 * Created by Anton on 23.02.2015.
 */
public class GameFragment extends Fragment {

    public static final String TAG = "GameFragment";

    private static final String GAME_MODE_KEY = "GAME_MODE_KEY";

    private GameMode mGameMode;

    private OnGameInteractionListener mListener;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGameMode = GameMode.fromValue(getArguments().getInt(GAME_MODE_KEY));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_game, container, false);

        final GameView gameView = (GameView) rootView.findViewById(R.id.game_view);
        gameView.setPuzzle(CandlePuzzleBuilder.build(GameView.COLUMNS, GameView.ROWS, 7));

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onGameInteraction(uri);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnGameInteractionListener {
        // TODO: Update argument type and name
        public void onGameInteraction(Uri uri);
    }
}
