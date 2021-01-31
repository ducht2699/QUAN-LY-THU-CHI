package com.example.project3.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.project3.Constants;
import com.example.project3.fragment.statistic_details.ie_situation.IESituationDetailsTabFragment;
import com.example.project3.fragment.statistic_details.ie_situation.IESituationNowTabFragment;


public class IESituation_ViewPagerAdapter extends FragmentStatePagerAdapter {
    private Bundle bundle;

    public IESituation_ViewPagerAdapter(@NonNull FragmentManager fm, Bundle bundle) {
        super(fm);
        this.bundle = bundle;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case Constants.NOW:
                IESituationNowTabFragment ieSituationNowTabFragment = new IESituationNowTabFragment();
                ieSituationNowTabFragment.setArguments(bundle);
                return ieSituationNowTabFragment;
            default:
                return new IESituationDetailsTabFragment(position);
        }
    }

    @Override
    public int getCount() {
        return Constants.TAB_IE_SITUATION;
    }
}
