package ru.dinter.magiccave.UI;

import java.util.ArrayList;
import java.util.List;

import ru.dinter.magiccave.MainActivity;
import ru.dinter.magiccave.R;
import ru.dinter.magiccave.IO.ResourceLoader;
import ru.dinter.magiccave.view.BigCandleView;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RulesPageFragment extends Fragment {

	private static final String TAG = "RulesPageFragment";
	private static final int PAGE_STRINGS_RESOURCES_ID[] = { R.string.modes_label, R.string.modes_text,
			R.string.task_label, R.string.task_text, R.string.control_label, R.string.control_text };

	private static final int PAGES_NUMBER = 3;

	private List<View> pages;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.rules_page_layout, container, false);

		ResourceLoader rl = ((MainActivity) getActivity()).RESOURCE_LOADER;
		BigCandleView bigCandleView = (BigCandleView) rootView.findViewById(R.id.bigCandleView1);
		bigCandleView.setResources(rl);
		bigCandleView = (BigCandleView) rootView.findViewById(R.id.bigCandleView2);
		bigCandleView.setResources(rl);

		final ViewPager pager = (ViewPager) rootView.findViewById(R.id.rules_pager);
		pager.setAdapter(new RulesPageAdapter(createPages()));

		return rootView;
	}

	private List<View> createPages() {
		if (pages == null) {
			pages = new ArrayList<>();

			LayoutInflater inflater = LayoutInflater.from(getActivity());

			View page;
			TextView textView;
			Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Parchment MF.ttf");

			for (int i = 0; i < PAGES_NUMBER; ++i) {
				page = inflater.inflate(R.layout.rules_page, null);
				textView = (TextView) page.findViewById(R.id.rules_page_label);
				textView.setText(PAGE_STRINGS_RESOURCES_ID[i * 2]);
				textView.setTypeface(type, Typeface.BOLD);
				textView = (TextView) page.findViewById(R.id.rules_page_text);
				textView.setText(PAGE_STRINGS_RESOURCES_ID[i * 2 + 1]);
				textView.setTypeface(type);
				pages.add(page);
			}
		}
		return pages;
	}

	private final class RulesPageAdapter extends PagerAdapter {

		private final List<View> pages;

		public RulesPageAdapter(List<View> pages) {
			this.pages = pages;
			Log.d(TAG, "Pager adapter created");
		}

		public Object instantiateItem(View collection, int position) {
			View v = pages.get(position);
			((ViewPager) collection).addView(v, 0);
			return v;
		}

		@Override
		public void destroyItem(View collection, int position, Object view) {
			((ViewPager) collection).removeView((View) view);
		}

		@Override
		public int getCount() {
			return pages.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

	}

}
