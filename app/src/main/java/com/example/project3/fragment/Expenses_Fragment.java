package com.example.project3.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.project3.R;
import com.example.project3.adapter.Expenses_ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;


public class Expenses_Fragment extends Fragment {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private View view;

    public Expenses_Fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_expenses, container, false);
        tabLayout = view.findViewById(R.id.tablayout_khoanchi);
        viewPager = view.findViewById(R.id.viewpager_khoanchi);
        tabLayout.addTab(tabLayout.newTab().setText("KHOẢN CHI"));
        tabLayout.addTab(tabLayout.newTab().setText("LOẠI CHI"));
        Expenses_ViewPagerAdapter adapter = new Expenses_ViewPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.getTabAt(0).setIcon(R.drawable.khoanchi);
        tabLayout.getTabAt(1).setIcon(R.drawable.loaichi);
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
}
