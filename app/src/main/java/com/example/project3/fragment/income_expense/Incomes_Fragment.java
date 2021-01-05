package com.example.project3.fragment.income_expense;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project3.R;
import com.example.project3.adapter.Incomes_ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;


public class Incomes_Fragment extends Fragment {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private View view;

    public Incomes_Fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_incomes, container, false);
        viewPager = view.findViewById(R.id.viewpager_khoanthu);
        tabLayout = view.findViewById(R.id.tablayout_khoanthu);
        tabLayout.addTab(tabLayout.newTab().setText("KHOẢN THU"));
        tabLayout.addTab(tabLayout.newTab().setText("LOẠI THU"));
        Incomes_ViewPagerAdapter adapter = new Incomes_ViewPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.getTabAt(0).setIcon(R.drawable.cash);
        tabLayout.getTabAt(1).setIcon(R.drawable.money);
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
