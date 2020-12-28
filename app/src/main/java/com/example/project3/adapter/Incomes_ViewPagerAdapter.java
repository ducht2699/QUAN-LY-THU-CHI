package com.example.project3.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.project3.Constant;
import com.example.project3.fragment.Tab_Incomes_Fragment;
import com.example.project3.fragment.Tab_IncomesType_Fragment;

public class Incomes_ViewPagerAdapter extends FragmentStatePagerAdapter {

    public Incomes_ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case Constant.TRANSACTIONS:
                Tab_Incomes_Fragment tab_incomes_fragment = new Tab_Incomes_Fragment();
                return tab_incomes_fragment;
            case Constant.TYPES:
                Tab_IncomesType_Fragment tab_incomesType_fragment = new Tab_IncomesType_Fragment();
                return tab_incomesType_fragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return Constant.TAB_IE;
    }

}