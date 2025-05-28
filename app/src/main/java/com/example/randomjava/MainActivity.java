package com.example.randomjava;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

enum NAVIGATION_CONSTANTS{
    RANDOM,
    LIST
};

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    private Fragment[] fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //Setup the fragments and show the first
        fragments = new Fragment[2];
        fragments[0] = new RandomFragment();
        fragments[1] = new ListFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, fragments[0]);
        fragmentTransaction.add(R.id.fragment_container, fragments[1]);
        fragmentTransaction.hide(fragments[1]);
        fragmentTransaction.commit();
    }


    private void show_fragment(NAVIGATION_CONSTANTS navigation_request) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (navigation_request){
            case RANDOM:
                fragmentTransaction.show(fragments[0]);
                fragmentTransaction.hide(fragments[1]);
                break;
            case LIST:
                fragmentTransaction.show(fragments[1]);
                fragmentTransaction.hide(fragments[0]);
        }
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.nav_random) {
            show_fragment(NAVIGATION_CONSTANTS.RANDOM);
        } else if (itemId == R.id.nav_list) {
            show_fragment(NAVIGATION_CONSTANTS.LIST);
        } else {
            throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}