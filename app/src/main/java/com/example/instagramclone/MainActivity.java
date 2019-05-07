package com.example.instagramclone;

import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;

import com.example.instagramclone.Adapters.ViewPagerAdapter;
import com.example.instagramclone.MainActivityFragments.HomeFragment;
import com.example.instagramclone.MainActivityFragments.ProfileFragment;
import com.example.instagramclone.MainActivityFragments.SearchFragment;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;

    //Fragments
    HomeFragment homeFragment;
    SearchFragment searchFragment;
    ProfileFragment profileFragment;

    MenuItem prevMenuItem;

    final Fragment fragment1 = new HomeFragment();
    final Fragment fragment2 = new SearchFragment();
    final Fragment fragment3 = new ProfileFragment();
    final FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment active = fragment1;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragmentManager.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_search:
                    fragmentManager.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_profile:
                    fragmentManager.beginTransaction().hide(active).show(fragment3).commit();
                    active = fragment3;
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        viewPager = findViewById(R.id.viewpager);

        setupViewPager(viewPager);

        fragmentManager.beginTransaction().add(R.id.main_container, fragment3, "3")
                .hide(fragment3)
                .commit();
        fragmentManager.beginTransaction().add(R.id.main_container, fragment2, "2")
                .hide(fragment2)
                .commit();
        fragmentManager.beginTransaction().add(R.id.main_container,fragment1, "1")
                .commit();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else {
                    navView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: "+i);
                navView.getMenu().getItem(i).setChecked(true);
                prevMenuItem = navView.getMenu().getItem(i);

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        homeFragment=new HomeFragment();
        searchFragment=new SearchFragment();
        profileFragment=new ProfileFragment();
        adapter.addFragment(homeFragment);
        adapter.addFragment(searchFragment);
        adapter.addFragment(profileFragment);
        viewPager.setAdapter(adapter);
    }



}
