package com.project.numerojyotish.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.project.numerojyotish.Fragment.AntardashaFragment;
import com.project.numerojyotish.Fragment.BasicInfoFragment;
import com.project.numerojyotish.Fragment.DashaFragment;
import com.project.numerojyotish.R;
import com.project.numerojyotish.session.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        if (android.os.Build.VERSION.SDK_INT >= 21)
        {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        }
        Toolbar toolbar = findViewById(R.id.toolbarmain);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        ImageView plusimage =  toolbar.findViewById(R.id.plusimage);
        mTitle.setText("Home");
        plusimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        sessionManager = new SessionManager(getApplicationContext());

        setupViewPager(viewPager);
        setupTabIcons();



    }
    @SuppressLint("ResourceType")
    private void setupTabIcons() {


        TextView tabThree = (TextView)
                LayoutInflater.from(this).inflate(R.layout.custom_tab, tabLayout, false);
        tabThree.setText("Basic Info");
        tabThree.setTextColor(getResources().getColorStateList(R.drawable.selector_textview));
        tabThree.setTextSize(15);
        tabLayout.getTabAt(0).setCustomView(tabThree);

               TextView tabOne = (TextView)
                LayoutInflater.from(this).inflate(R.layout.custom_tab, tabLayout, false);
        tabOne.setText("Dasha");
        tabOne.setTextColor(getResources().getColorStateList(R.drawable.selector_textview));
        tabOne.setTextSize(15);
        tabLayout.getTabAt(1).setCustomView(tabOne);


        TextView tabTwo = (TextView)
                LayoutInflater.from(this).inflate(R.layout.custom_tab, tabLayout, false);
        tabTwo.setText("Antardasha");
        tabTwo.setTextColor(getResources().getColorStateList(R.drawable.selector_textview));
        tabTwo.setTextSize(15);
        tabLayout.getTabAt(2).setCustomView(tabTwo);


    }

    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BasicInfoFragment(), "Basic Info");
        adapter.addFragment(new DashaFragment(), "Dasha");
        adapter.addFragment(new AntardashaFragment(), "Antardasha");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
