package edu.amd.spbstu.magiccave.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.amd.spbstu.magiccave.R;

/**
 * @author iAnton
 * @since 15/03/15
 */
public class LevelChooseFragment extends Fragment {

    public static final String TAG = "LevelChooseFragment";

    public static final String LEVEL_KEY = "LEVEL_KEY";
    private static final int ROW_NUM = 3;
    private static final int COL_NUM = 3;

    private static final int[] LEVEL_BUTTON_CONTAINRES = {
            R.id.level1, R.id.level2, R.id.level3, R.id.level4, R.id.level5, R.id.level6, R.id.level7, R.id.level8, R.id.level9
    };

    public LevelChooseFragment() {
        // Required empty public constructor
    }

    public static LevelChooseFragment newInstance() {
        return new LevelChooseFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_level_choose, container, false);
        final FragmentManager fragmentManager = getFragmentManager();
        for (int i = 0; i < ROW_NUM; ++i) {
            for (int j = 0; j < COL_NUM; ++j) {
                final int level = i * COL_NUM + j + 1;
                final int savedStars = loadStars(level);
                fragmentManager.beginTransaction()
                        .add(LEVEL_BUTTON_CONTAINRES[i * COL_NUM + j], LevelButtonFragment.newInstance(level, savedStars), LevelButtonFragment.TAG)
                        .commit();
            }
        }

        return rootView;
    }

    private int loadStars(int level) {
        return getActivity().getPreferences(Context.MODE_PRIVATE).getInt(LEVEL_KEY + level, 0);
    }
}
