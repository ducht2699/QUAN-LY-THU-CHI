package com.example.project3.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.project3.fragment.statistic_details.expense_analysis.IEADayFragment;
import com.example.project3.fragment.statistic_details.expense_analysis.IEAMonthFragment;
import com.example.project3.fragment.statistic_details.expense_analysis.IEAYearFragment;

public class IEAnalysis_viewPagerAdapter extends FragmentStatePagerAdapter {
private int IEType;
    public IEAnalysis_viewPagerAdapter(@NonNull FragmentManager fm, int IEType) {
        super(fm);
        this.IEType = IEType;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new IEADayFragment(IEType);
            case 1:
                return new IEAMonthFragment(IEType);
            case 2:
                return new IEAYearFragment(IEType);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
