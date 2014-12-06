package ru.dinter.magiccave.UI;

import ru.dinter.magiccave.MainActivity;
import ru.dinter.magiccave.MainApplication;
import ru.dinter.magiccave.R;
import ru.dinter.magiccave.IO.ResourceLoader;
import ru.dinter.magiccave.view.CaveView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RandomLevelFragment extends Fragment {

	
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		
		// Here should be the code that saves the graph.
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.random_level_layout, container, false);
		
		ResourceLoader rl = MainApplication.RESOURCE_LOADER;
		CaveView caveView = (CaveView) rootView.findViewById(R.id.random_level_cave_view);
		caveView.setResources(rl);
		
		return rootView;
	}
	
}
