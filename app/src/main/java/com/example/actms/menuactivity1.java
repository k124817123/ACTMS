package com.example.actms;

import android.os.Bundle;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.tabs.TabLayout;

public class menuactivity1  extends AppCompatActivity {
    // I did a test that the value, s, it can transport to other activities or not?
    String s = ".";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main1);

        // ViewPager mixed with Fragment.
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),
                this);
        viewPager.setAdapter(adapter);
//        viewPager.setCurrentItem(1);

        // TabLayout mixed with ViewPager.
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        FloatingActionMenu menu = findViewById(R.id.floatingActionMenu);

//        if(menu!=null) {
//            if (ev.getAction() == MotionEvent.ACTION_UP && menu.isOpened()) {
//                menu.close(true);
//            }
//        }
        return super.dispatchTouchEvent(ev);
    }
}
