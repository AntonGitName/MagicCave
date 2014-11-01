package ru.dinter.magiccave.UI;

import java.util.ArrayList;
import java.util.List;

import ru.dinter.magiccave.MainActivity;
import ru.dinter.magiccave.R;
import ru.dinter.magiccave.IO.ResourceLoader;
import ru.dinter.magiccave.view.BigCandleView;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RulesPageFragment extends Fragment {

    private static final String TAG = "RulesPageFragment";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ResourceLoader rl = ((MainActivity) getActivity()).RESOURCE_LOADER;
        BigCandleView bigCandleView = (BigCandleView) getActivity().findViewById(R.id.bigCandleView1);
        bigCandleView.setResources(rl);
        bigCandleView = (BigCandleView) getActivity().findViewById(R.id.bigCandleView2);
        bigCandleView.setResources(rl);
        
        ViewPager pager = (ViewPager) getActivity().findViewById(R.id.rules_pager);
        pager.setAdapter(new RulesPageAdapter());
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        
        return inflater.inflate(R.layout.rules_page_layout, container, false);
    }
    
    private final class RulesPageAdapter extends PagerAdapter {

        private final List<View> pages = new ArrayList<>();
        
        public RulesPageAdapter() {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            // And how can I fix the issue?
            pages.add(inflater.inflate(R.layout.rules_page_1, null));
            pages.add(inflater.inflate(R.layout.rules_page_2, null));
            pages.add(inflater.inflate(R.layout.rules_page_3, null));
            Log.d(TAG, "Pager adapter created");
        }
        
        public Object instantiateItem(View collection, int position){
            View v = pages.get(position);
            ((ViewPager) collection).addView(v, 0);
            return v;
        }
        
        @Override
        public void destroyItem(View collection, int position, Object view){
            ((ViewPager) collection).removeView((View) view);
        }
        
        @Override
        public int getCount(){
            return pages.size();
        }
        
        @Override
        public boolean isViewFromObject(View view, Object object){
            return view.equals(object);
        }

        @Override
        public void finishUpdate(View arg0){
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1){
        }

        @Override
        public Parcelable saveState(){
            return null;
        }

        @Override
        public void startUpdate(View arg0){
        }

        
    }
    
}
