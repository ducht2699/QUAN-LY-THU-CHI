package com.example.project3.fragment.statistic_details.expense_analysis;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project3.Constants;
import com.example.project3.R;
import com.example.project3.adapter.IEAnalysis_viewPagerAdapter;
import com.google.android.material.tabs.TabLayout;


public class ExpenseAnalysisFragment extends Fragment {
    private View view;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    public ExpenseAnalysisFragment() {
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
        view = inflater.inflate(R.layout.fragment_tab_layout, container, false);
        init();
        tabLayout.addTab(tabLayout.newTab().setText("NGÀY"));
        tabLayout.addTab(tabLayout.newTab().setText("THÁNG"));
        tabLayout.addTab(tabLayout.newTab().setText("NĂM"));
        IEAnalysis_viewPagerAdapter adapter = new IEAnalysis_viewPagerAdapter(getActivity().getSupportFragmentManager(), Constants.EXPENSES);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        return view;
    }
    private void init() {
        tabLayout = view.findViewById(R.id.tabLayout_ieSituation);
        viewPager = view.findViewById(R.id.viewPager_ieSituation);
    }
}