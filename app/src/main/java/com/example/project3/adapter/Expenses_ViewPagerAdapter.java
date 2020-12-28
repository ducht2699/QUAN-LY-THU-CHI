package com.example.project3.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.project3.fragment.Tab_Expenses_Fragment;
import com.example.project3.fragment.Tab_ExpensesType_Fragment;


public class Expenses_ViewPagerAdapter extends FragmentStatePagerAdapter {
    int numberTab = 2;

    public Expenses_ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Tab_Expenses_Fragment tab_expenses_fragment = new Tab_Expenses_Fragment();
                return tab_expenses_fragment;
            case 1:
                Tab_ExpensesType_Fragment tab_expensesType_fragment = new Tab_ExpensesType_Fragment();
                return tab_expensesType_fragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return numberTab;
    }
}
