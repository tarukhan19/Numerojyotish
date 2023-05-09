package com.numerojyotish.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.numerojyotish.Fragment.AntardashaFragment;
import com.numerojyotish.Fragment.BasicInfoFragment;
import com.numerojyotish.Fragment.ChartFragment;
import com.numerojyotish.Fragment.DailyDashaFragment;
import com.numerojyotish.Fragment.DashaFragment;
import com.numerojyotish.Fragment.MoreFragment;
import com.numerojyotish.Fragment.NameDetailsFragment;
import com.numerojyotish.Fragment.PartyantarDashaFragment;
import com.numerojyotish.R;
import com.numerojyotish.databinding.ActivityHomePageBinding;
import com.numerojyotish.session.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {

    SessionManager sessionManager;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    TextView mTitle;
    private Handler mHandler;
    private static final String TAG_HOME = "home";
    private static final String TAG_CHART = "chart";
    private static final String TAG_DETAIL = "detailfromname";
    private static final String TAG_MORE = "more";

    public static int navItemIndex = 0;
    public static String CURRENT_TAG = TAG_HOME;
    ImageView plusimage,logout;
    ActivityHomePageBinding binding;
    Fragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_page);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        mTitle = toolbar.findViewById(R.id.toolbar_title);
        plusimage = toolbar.findViewById(R.id.plusimage);
        logout= toolbar.findViewById(R.id.logout);
        plusimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in7 = new Intent(HomePageActivity.this, BasicInfoActivity.class);
                in7.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(in7);
                overridePendingTransition(R.anim.trans_left_in,
                        R.anim.trans_left_out);
            }
        });



        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogOutDialog();

            }
        });

        sessionManager = new SessionManager(getApplicationContext());
        progressDialog = new ProgressDialog(this);
        requestQueue = Volley.newRequestQueue(HomePageActivity.this);
        mHandler = new Handler();

        binding.tabs.setupWithViewPager(binding.viewpager);

        setupViewPager(binding.viewpager);
        setupTabIcons();

        CURRENT_TAG = TAG_HOME;
        navItemIndex = 0;
        mTitle.setText("Home");
        loadHomeFragment();
      //  hideItem();
        binding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.first_fragment:
                    CURRENT_TAG = TAG_HOME;
                    binding.appbarLL.setVisibility(View.VISIBLE);
                    binding.viewpager.setVisibility(View.VISIBLE);
                    navItemIndex = 0;
                    mTitle.setText("Home");

                    loadHomeFragment();
                    return true;
                case R.id.second_fragment:
                    CURRENT_TAG = TAG_CHART;
                    binding.appbarLL.setVisibility(View.GONE);
                    binding.viewpager.setVisibility(View.GONE);
                    navItemIndex = 1;
                    mTitle.setText("Chart");

                    loadHomeFragment();
                    return true;
                case R.id.third_fragment:
                    CURRENT_TAG = TAG_DETAIL;
                    binding.appbarLL.setVisibility(View.GONE);
                    binding.viewpager.setVisibility(View.GONE);
                    navItemIndex = 2;
                    mTitle.setText("Name Details");

                    loadHomeFragment();
                    return true;
                case R.id.fourth_fragment:
                    CURRENT_TAG = TAG_MORE;
                    binding.appbarLL.setVisibility(View.GONE);
                    binding.viewpager.setVisibility(View.GONE);
                    navItemIndex = 3;
                    mTitle.setText("More");

                    loadHomeFragment();
                    return true;

            }
            return false;
        }
    };
    private void showLogOutDialog()
    {
        final Dialog dialog = new Dialog(this, R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_logout);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        TextView descriptionTV = dialog.findViewById(R.id.descriptionTV);
        TextView titleTV = dialog.findViewById(R.id.titleTV);
        ImageView imageView = dialog.findViewById(R.id.imageView);
        Button yesBTN = dialog.findViewById(R.id.yesBTN);
        Button noBtn = dialog.findViewById(R.id.noBtn);


        yesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
                sessionManager.logoutUser();
                Intent in7 = new Intent(HomePageActivity.this, LoginActivity.class);
                in7.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(in7);
                overridePendingTransition(R.anim.trans_left_in,
                        R.anim.trans_left_out);

            }
        });

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();

            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in7 = new Intent(HomePageActivity.this, BasicInfoActivity.class);
        in7.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(in7);
        overridePendingTransition(R.anim.trans_left_in,
                R.anim.trans_left_out);
    }

    @SuppressLint("ResourceType")
    private void setupTabIcons()
    {

        TextView tabone = (TextView)
                LayoutInflater.from(this).inflate(R.layout.custom_tab, binding.tabs, false);
        tabone.setText("Basic Info");
        tabone.setTextColor(getResources().getColorStateList(R.drawable.selector_textview));
        tabone.setTextSize(15);
        binding.tabs.getTabAt(0).setCustomView(tabone);

        TextView tabTwo = (TextView)
                LayoutInflater.from(this).inflate(R.layout.custom_tab, binding.tabs, false);
        tabTwo.setText("Dasha");
        tabTwo.setTextColor(getResources().getColorStateList(R.drawable.selector_textview));
        tabTwo.setTextSize(15);
        binding.tabs.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView)
                LayoutInflater.from(this).inflate(R.layout.custom_tab, binding.tabs, false);
        tabThree.setText("Antardasha");
        tabThree.setTextColor(getResources().getColorStateList(R.drawable.selector_textview));
        tabThree.setTextSize(15);
        binding.tabs.getTabAt(2).setCustomView(tabThree);


        TextView tabFour = (TextView)
                LayoutInflater.from(this).inflate(R.layout.custom_tab, binding.tabs, false);
        tabFour.setText("Partyantar Dasha");
        tabFour.setTextColor(getResources().getColorStateList(R.drawable.selector_textview));
        tabFour.setTextSize(15);
        binding.tabs.getTabAt(3).setCustomView(tabFour);

        TextView tabFive = (TextView)
                LayoutInflater.from(this).inflate(R.layout.custom_tab, binding.tabs, false);
        tabFive.setText("Daily Dasha");
        tabFive.setTextColor(getResources().getColorStateList(R.drawable.selector_textview));
        tabFive.setTextSize(15);
        binding.tabs.getTabAt(4).setCustomView(tabFive);




    }

    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BasicInfoFragment(), "Basic Info");
        adapter.addFragment(new DashaFragment(), "Dasha");
        adapter.addFragment(new AntardashaFragment(), "Antardasha");
        adapter.addFragment(new PartyantarDashaFragment(), "Partyantar Dasha");
        adapter.addFragment(new DailyDashaFragment(), "Daily Dasha");

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

    private void loadHomeFragment()
    {
        // selecting appropriate nav menu item
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }


        invalidateOptionsMenu();
    }



    private Fragment getHomeFragment() {
        switch (navItemIndex) {


            case 0:

                BasicInfoFragment basicInfoFragment = new BasicInfoFragment();
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                return basicInfoFragment;


            case 1:

                ChartFragment chartFragment = new ChartFragment();
                navItemIndex = 1;
                CURRENT_TAG = TAG_CHART;
                return chartFragment;


            case 2:

                NameDetailsFragment nameDetailsFragment = new NameDetailsFragment();
                navItemIndex = 2;
                CURRENT_TAG = TAG_DETAIL;
                return nameDetailsFragment;

            case 3:

                MoreFragment moreFragment = new MoreFragment();
                navItemIndex = 3;
                CURRENT_TAG = TAG_MORE;
                return moreFragment;

//            case 4:
//
//                ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
//                navItemIndex = 4;
//                CURRENT_TAG = TAG_CHANGEPASSWORD;
//                return changePasswordFragment;


            default:
                return new BasicInfoFragment();
        }


    }



}
