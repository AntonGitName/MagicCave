package ru.dinter.magiccave.fragments;

import java.util.Timer;
import java.util.TimerTask;

import ru.dinter.magiccave.MainActivity;
import ru.dinter.magiccave.R;
import ru.dinter.magiccave.IO.ResourceLoader;
import ru.dinter.magiccave.view.BigCandleView;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainMenuFragment extends Fragment {

    private static final String TAG = "MainMenuFragment";
    
    private Timer timer;
    private static int timerNum = 0;
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        ResourceLoader rl = ((MainActivity)getActivity()).RESOURCE_LOADER;
        BigCandleView bigCandleView = (BigCandleView) getActivity().findViewById(R.id.bigCandleView1);
        bigCandleView.setResources(rl);
        bigCandleView = (BigCandleView) getActivity().findViewById(R.id.bigCandleView2);
        bigCandleView.setResources(rl);
        
        Button button = (Button) getActivity().findViewById(R.id.choose_level_btn);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        return inflater.inflate(R.layout.main_menu_layout, container, false);
    }
    
    @Override
    public void onPause() {
        super.onPause();

        timer.cancel();
        Log.d(TAG, "Timer stopped");
    }

    @Override
    public void onResume() {
        super.onStart();

        Log.d(TAG, "Timer started: " + ++timerNum);
        final Handler handler = new Handler();
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        if (getView() != null) {
                            getView().invalidate();
                        }
                    }
                });
            }
        }, 0, MainActivity.UPDATE_RATE);
    }
}
