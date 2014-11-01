package ru.dinter.magiccave.UI;

import ru.dinter.magiccave.MainActivity;
import ru.dinter.magiccave.R;
import ru.dinter.magiccave.IO.ResourceLoader;
import ru.dinter.magiccave.view.BigCandleView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainMenuFragment extends Fragment {

    private static final String TAG = "MainMenuFragment";


    private Button chsLvlButton;
    private Button rulesButton;
    private Button quitButton;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(TAG, "Setting up main menu buttons");
        
        ResourceLoader rl = ((MainActivity) getActivity()).RESOURCE_LOADER;
        BigCandleView bigCandleView = (BigCandleView) getActivity().findViewById(R.id.bigCandleView1);
        bigCandleView.setResources(rl);
        bigCandleView = (BigCandleView) getActivity().findViewById(R.id.bigCandleView2);
        bigCandleView.setResources(rl);

        quitButton = (Button) getActivity().findViewById(R.id.quit_btn);
        chsLvlButton = (Button) getActivity().findViewById(R.id.choose_level_btn);
        rulesButton = (Button) getActivity().findViewById(R.id.rules_btn);

        quitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        rulesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(android.R.id.content, new RulesPageFragment())
                        .addToBackStack(null).commit();
            }
        });
        
        // TODO
        chsLvlButton.getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        return inflater.inflate(R.layout.main_menu_layout, container, false);
    }
}
