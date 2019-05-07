package com.example.instagramclone.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.instagramclone.LoginActivityFragments.LoginFragment;
import com.example.instagramclone.LoginActivityFragments.RegisterFragment;

public class ViewPageAdpaterLogin extends FragmentStatePagerAdapter {

    private int numOfTabs;

    public ViewPageAdpaterLogin(FragmentManager fm) {
        super(fm);
    }

    public ViewPageAdpaterLogin(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int i) {

        switch (i) {
            case 0:
                return new LoginFragment();
            case 1:
                return new RegisterFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
