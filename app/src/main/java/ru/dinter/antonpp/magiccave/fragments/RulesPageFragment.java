package ru.dinter.antonpp.magiccave.fragments;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.dinter.antonpp.magiccave.MainApplication;
import ru.dinter.antonpp.magiccave.R;

/**
 * Created by Anton on 23.02.2015.
 */
public class RulesPageFragment extends Fragment {

    public static final String TAG = "RulesPageFragment";

    private static final int PAGE_STRINGS_RESOURCES_ID[] = { R.string.modes_label, R.string.modes_text,
            R.string.task_label, R.string.task_text, R.string.control_label, R.string.control_text };

    private static final int PAGES_COUNT = 3;

    private List<View> pages;

    private OnRulesPageInteractionListener mListener;

    public static RulesPageFragment newInstance() {
        RulesPageFragment fragment = new RulesPageFragment();
        return fragment;
    }

    public RulesPageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_rules_page, container, false);

        final ViewPager pager = (ViewPager) rootView.findViewById(R.id.rules_pager);
        pager.setAdapter(new RulesPageAdapter(getFragmentManager()));

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnRulesPageInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnRulesPageInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnRulesPageInteractionListener {
        // TODO: Update argument type and name
        public void onRulesPageInteraction();
    }

    public static final class PageFragment extends Fragment {
        static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

        int mPageNumber;

        static PageFragment newInstance(int page) {
            PageFragment pageFragment = new PageFragment();
            Bundle arguments = new Bundle();
            arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
            pageFragment.setArguments(arguments);
            return pageFragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mPageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final View page = inflater.inflate(R.layout.layout_rules_page, null);
            final Typeface type = MainApplication.RESOURCE_LOADER.getTypeface();

            TextView textView = (TextView) page.findViewById(R.id.rules_page_label);
            textView.setText(PAGE_STRINGS_RESOURCES_ID[mPageNumber * 2]);
            textView.setTypeface(type, Typeface.BOLD);
            textView = (TextView) page.findViewById(R.id.rules_page_text);
            textView.setText(PAGE_STRINGS_RESOURCES_ID[mPageNumber * 2 + 1]);
            textView.setTypeface(type);
            return page;
        }
    }

    private static final class RulesPageAdapter extends FragmentStatePagerAdapter {

        public RulesPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PageFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return PAGES_COUNT;
        }
    }
}
