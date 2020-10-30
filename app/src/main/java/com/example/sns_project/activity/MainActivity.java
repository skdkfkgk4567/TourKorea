package com.example.sns_project.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.sns_project.R;
import com.example.sns_project.adapter.ViewPagerAdapter;
import com.example.sns_project.databinding.Cul2Binding;
import com.example.sns_project.fragment.MapFragment;
import com.example.sns_project.fragment.MyPostsFragment;
import com.example.sns_project.fragment.MyTopPostsFragment;
import com.example.sns_project.fragment.NowLocationFrag;
import com.example.sns_project.fragment.RecentPostsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends BaseActivity
{
    private static final String TAG = "MainActivity";
    public static int Num_ITEMS = 4;
    public static String PageName ="";
    private ViewPager viewPager;
    BottomNavigationView bottomNavigationView;

    FragmentPagerAdapter PagerAdapter;

    private RecentPostsFragment recentPostsFragment;
    private MyPostsFragment myPostsFragment;
    private MyTopPostsFragment myTopPostsFragment;
    private MapFragment mapTest;
    private MenuItem prevMenuItem;

    private Intent it;

    private TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Cul2Binding binding = Cul2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        checkSelfPermission();
        it = new Intent(this, NowLocation.class);

        //ViewPager 설정
        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setOffscreenPageLimit(4);

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
                        startActivity(it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
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


        //Fragment 생성
        recentPostsFragment = new RecentPostsFragment();
        myPostsFragment = new MyPostsFragment();
        myTopPostsFragment = new MyTopPostsFragment();
        mapTest = new MapFragment();

        //ViewPageAdapter와 Fragment연결

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),0);
        viewPagerAdapter.addFragment(recentPostsFragment,"홈");
        viewPagerAdapter.addFragment(myPostsFragment,"내 게시물");
        viewPagerAdapter.addFragment(myTopPostsFragment,"내 인기게시물");
        viewPagerAdapter.addFragment(mapTest,"지도");
        viewPager.setAdapter(viewPagerAdapter);

        //TabLayout 아이콘 설정

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_action_post);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_action_starpost);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_map);


        binding.fabNewPost.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MainActivity.this, NewPostActivity.class));
            }
        });
        //setupViewPager(viewPager);

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
/*
    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),0);

        adapter.addFragment(recentPostsFragment);
        adapter.addFragment(myPostsFragment);
        adapter.addFragment(myTopPostsFragment);
        adapter.addFragment(mapTest);
        viewPager.setAdapter(adapter);
    }
*/
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

    public void checkSelfPermission()
    {
        String temp = "";

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            temp += Manifest.permission.READ_EXTERNAL_STORAGE + " ";
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            temp += Manifest.permission.ACCESS_FINE_LOCATION + " ";
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            temp += Manifest.permission.ACCESS_COARSE_LOCATION + " ";
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            temp += Manifest.permission.CAMERA + " ";
        }

        if (TextUtils.isEmpty(temp) == false)
        {
            ActivityCompat.requestPermissions(this, temp.trim().split(" "), 1);
        }
        else
        {
            Toast.makeText(this, "권한을 모두 허용", Toast.LENGTH_SHORT).show();
        }
    }

}

