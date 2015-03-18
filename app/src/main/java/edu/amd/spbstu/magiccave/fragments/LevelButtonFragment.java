package edu.amd.spbstu.magiccave.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import edu.amd.spbstu.magiccave.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnLevelButtonClickListener} interface
 * to handle interaction events.
 * Use the {@link LevelButtonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LevelButtonFragment extends Fragment {

    public static final String TAG = "LevelButtonFragment";

    private static final String ARG_LEVEL = "param1";
    private static final String ARG_STARS = "param2";

    private static final int MAX_STARS = 3;

    private int level;
    private int stars;
    private ImageView[] starViews;

    private OnLevelButtonClickListener mListener;

    public LevelButtonFragment() {
        // Required empty public constructor
    }

    public static LevelButtonFragment newInstance(int level, int stars) {
        LevelButtonFragment fragment = new LevelButtonFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LEVEL, level);
        args.putInt(ARG_STARS, stars);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            level = getArguments().getInt(ARG_LEVEL);
            stars = getArguments().getInt(ARG_STARS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_level_button, container, false);
        final TextView textView = (TextView) rootView.findViewById(R.id.level_text);
        textView.setText("  " + level);
        starViews = new ImageView[]{(ImageView) rootView.findViewById(R.id.star_1)
                , (ImageView) rootView.findViewById(R.id.star_2)
                , (ImageView) rootView.findViewById(R.id.star_3)};

        updateStars(stars);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onLevelButtonClick(level);
            }
        });
        return rootView;
    }

    public void updateStars(int stars) {
        this.stars = stars;
        int i = 0;
        for (; i < stars; ++i) {
            starViews[i].setImageResource(R.drawable.star);
        }
        for (; i < MAX_STARS; ++i) {
            starViews[i].setImageResource(R.drawable.star_empty);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnLevelButtonClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnLevelButtonClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnLevelButtonClickListener {
        void onLevelButtonClick(int level);
    }

}
