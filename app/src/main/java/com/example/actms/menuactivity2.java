package com.example.actms;


import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;

public class menuactivity2 extends AppCompatActivity {
    private static final String NAV_ITEM_ID = "nav_index";
    TextView versiontv;
    GlobalVariable globalVariable;
    DrawerLayout drawerLayout;
    private monitorfragment monitorfragment;
    PageFragment pageFragment;
    private FragmentManager mFragmentMgr;

    int CurrentMenuItem = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main2);
        globalVariable=(GlobalVariable)getApplicationContext();
        versiontv=(TextView)findViewById(R.id.versiontv);
        versiontv.setText("Ver "+globalVariable.getVersion());
        mFragmentMgr=getSupportFragmentManager();

        monitorfragment=new monitorfragment();
        pageFragment=new PageFragment();
        mFragmentMgr.beginTransaction()
                .replace(R.id.contentView, monitorfragment, "monitorfragment")
                .commit();
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
//        drawerLayout.setScrimColor(Color.TRANSPARENT);
        NavigationView view = (NavigationView) findViewById(R.id.navigationView);

        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if(!(menuItem == view.getMenu().getItem(CurrentMenuItem))) {//判斷使者者是否點擊當前畫面的項目，若不是，根據所按的項目做出分別的動作
                    switch (menuItem.getItemId()) {
                        case R.id.monitor:

                            if(monitorfragment.isAdded())
                                mFragmentMgr.beginTransaction().show(monitorfragment);
                            else {
                                mFragmentMgr.beginTransaction()
                                        .replace(R.id.contentView, monitorfragment, "monitorfragment")

                                        .commit();
                            }

                            CurrentMenuItem=0;
                            break;
                        case R.id.logout:
                            finish();
                            break;

                    }
                }
                drawerLayout.closeDrawers();


                return true;
            }
        });
//
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawers();
            }else finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
