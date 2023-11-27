package com.example.actms;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {



    // Setting your tabs
    private String[] titles = new String[]{"User", "Role"};


    public MyFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);


    }

    @Override
    public Fragment getItem(int position) {
        // Setting your activity on each different tab,
        // and transporting other values that you want at here.
        Log.d("kevin","position:"+position);
//        if(position==0){
//            return new usermain();
//        }
        return new PageFragment().newInstance(position+1, titles[position]);
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
