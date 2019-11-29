package com.guftgue.adapter;


import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.guftgue.fragment.FragmentByContact;
import com.guftgue.fragment.FragmentMyEncription;
import com.guftgue.fragment.FragmentMyProfile;


public class TabAdapter1 extends FragmentPagerAdapter {

    private String[] titles={"UserList","Encryptions","My Profile"};

    private Context mActivity;

    public TabAdapter1(FragmentManager fragmentManager, Context activity) {
        super(fragmentManager);
        this.mActivity=activity;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                FragmentByContact fragment = new FragmentByContact();
                return fragment;
            case 1:
                FragmentMyEncription fragment1 = new FragmentMyEncription();
                return fragment1;
              //  return  FragmentProfileDetails.newInstance(titles[position]);
            case 2:
                FragmentMyProfile fragment2 = new FragmentMyProfile();
                return fragment2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return titles[position];
    }


}