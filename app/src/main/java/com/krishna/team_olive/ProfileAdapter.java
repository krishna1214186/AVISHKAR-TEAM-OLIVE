package com.krishna.team_olive;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ProfileAdapter extends FragmentPagerAdapter {

    private Context context;
    int totaltabs;

    public ProfileAdapter(@NonNull FragmentManager fm, Context context, int totaltabs) {
        super(fm);
        this.context = context;
        this.totaltabs = totaltabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                Profile_Info_Fragment profile_info_fragment = new Profile_Info_Fragment();
                return profile_info_fragment;
            case 1:
                ReviewFragment reviewFragment = new ReviewFragment();
                return reviewFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return totaltabs;
    }
}