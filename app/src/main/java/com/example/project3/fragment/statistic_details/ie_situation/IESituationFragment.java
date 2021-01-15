package com.example.project3.fragment.statistic_details.ie_situation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.project3.R;
import com.example.project3.adapter.IESituation_ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class IESituationFragment extends Fragment {
    private View view;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    public IESituationFragment() {
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
        view = inflater.inflate(R.layout.fragment_i_e_situation, container, false);
        init();
        tabLayout.addTab(tabLayout.newTab().setText("HIỆN TẠI"));
        tabLayout.addTab(tabLayout.newTab().setText("THÁNG"));
        tabLayout.addTab(tabLayout.newTab().setText("QUÝ"));
        tabLayout.addTab(tabLayout.newTab().setText("NĂM"));
        final Bundle bundle = getArguments();
        IESituation_ViewPagerAdapter adapter = new IESituation_ViewPagerAdapter(getActivity().getSupportFragmentManager(), bundle);
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