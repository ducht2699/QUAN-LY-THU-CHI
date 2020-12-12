package com.example.project3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.project3.fragment.Expenses_Fragment;
import com.example.project3.fragment.ChangePasswordFragment;
import com.example.project3.fragment.Intro_Fragment;
import com.example.project3.fragment.Incomes_Fragment;
import com.example.project3.fragment.Statistic_Fragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    boolean doubleBackToExitPressedOnce = false;

    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();


        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupDrawerContent(navigationView);


        setTitle("KHOẢN THU");
        replaceFragment(new Incomes_Fragment());


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("autoLogin", false);
            editor.commit();
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Nhấn Back một lần nữa để logout!", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                selectedItemDrawer(item);
                return true;
            }
        });
    }

    private void selectedItemDrawer(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.khoanthu:
                setTitle("KHOẢN THU");
                Incomes_Fragment incomes_fragment = new Incomes_Fragment();
                replaceFragment(incomes_fragment);
                break;
            case R.id.khoanchi:
                setTitle("KHOẢN CHI");
                Expenses_Fragment expenses_fragment = new Expenses_Fragment();
                replaceFragment(expenses_fragment);
                break;
            case R.id.thongke:
                setTitle("THỐNG KÊ");
                Statistic_Fragment searchFragment = new Statistic_Fragment();
                replaceFragment(searchFragment);
                break;
            case R.id.gioithieu:
                setTitle("GIỚI THIỆU");
                Intro_Fragment settingsFragment = new Intro_Fragment();
                replaceFragment(settingsFragment);
                break;

            case R.id.doimatkhau:
                setTitle("ĐỔI MẬT KHẨU");
                ChangePasswordFragment changepasswordFragment = new ChangePasswordFragment();
                replaceFragment(changepasswordFragment);
                break;
            case R.id.logout:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("autoLogin", false);
                editor.commit();
                finish();
                break;

        }

        item.setChecked(true);

        drawerLayout.closeDrawers();
    }

    private void init() {
        frameLayout = findViewById(R.id.frame_layout);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
    }


}
