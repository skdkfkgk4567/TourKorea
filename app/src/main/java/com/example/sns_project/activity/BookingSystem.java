package com.example.sns_project.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.sns_project.R;
import com.example.sns_project.adapter.ViewPagerAdapter;
import com.example.sns_project.databinding.BookingBinding;
import com.example.sns_project.fragment.DayTimeFragment;
import com.example.sns_project.fragment.FestivalMap;
import com.example.sns_project.fragment.FinalClass;
import com.example.sns_project.fragment.PersonFragment;
import com.example.sns_project.fragment.PlaceFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class BookingSystem extends BaseActivity
{
    private static final String TAG = "MainActivity";
    public static int Num_ITEMS = 4;
    public static String PageName ="";
    private ViewPager viewPager;
    BottomNavigationView bottomNavigationView;

    FragmentPagerAdapter PagerAdapter;

    private PlaceFragment placeFragment;
    private PersonFragment personFragment;
    private FestivalMap festivalMap;
    private FinalClass finalClass;
    private DayTimeFragment dayTimeFragment;
    private MenuItem prevMenuItem;
    private int person;


    private Intent it, it2;

    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        BookingBinding binding = BookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        it = new Intent(this, NowLocation.class);
        it2 = new Intent(this, SearchActivity.class);



        //ViewPager 설정
        viewPager = findViewById(R.id.container);
        viewPager.setOffscreenPageLimit(6);

        //TabLayout 설정
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();

                switch (pos)
                {
                    case 3:
                        person = FinalClass.person;
                        startActivity(it2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int pos = tab.getPosition();

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int pos = tab.getPosition();

            }
        });


        placeFragment = new PlaceFragment();
        personFragment = new PersonFragment();
        finalClass = new FinalClass();
        festivalMap = new FestivalMap();
        dayTimeFragment = new DayTimeFragment();


        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),0);
        viewPagerAdapter.addFragment(dayTimeFragment,"일정");
        viewPagerAdapter.addFragment(personFragment,"인원");
        viewPagerAdapter.addFragment(festivalMap,"축제지도");
        viewPagerAdapter.addFragment(placeFragment,"위치");
        viewPagerAdapter.addFragment(finalClass,"최종확인");
        viewPager.setAdapter(viewPagerAdapter);

        //TabLayout 아이콘 설정

        tabLayout.getTabAt(0).setIcon(R.drawable.cal);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_group_black_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_map);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_action_search);
        tabLayout.getTabAt(4).setIcon(R.drawable.ic_action_complete);

        tabLayout.getTabAt(0).setText("일정");
        tabLayout.getTabAt(1).setText("인원");
        tabLayout.getTabAt(2).setText("축제지도");
        tabLayout.getTabAt(3).setText("위치");
        tabLayout.getTabAt(4).setText("최종확인");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int i = item.getItemId();
        if (i == R.id.action_logout)
        {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        }
        else if (i == R.id.action_user_init)
        {
            startActivity(new Intent(this, MemberInitActivity.class));
            return true;
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        //권한을 허용 했을 경우
        if (requestCode == 1)
        {
            int length = permissions.length;
            for (int i = 0; i < length; i++)
            {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED)
                {
                    Log.d("MainActivity", "권한 허용 : " + permissions[i]);
                }
            }
        }
    }
}