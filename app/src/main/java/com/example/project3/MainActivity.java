package com.example.project3;

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

import com.example.project3.fragment.AccountTypeFragment;
import com.example.project3.fragment.ChangePasswordFragment;
import com.example.project3.fragment.income_expense.Expenses_Fragment;
import com.example.project3.fragment.Intro_Fragment;
import com.example.project3.fragment.income_expense.Incomes_Fragment;
import com.example.project3.fragment.Statistic_Fragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private boolean doubleBackToExitPressedOnce = false;
    private FrameLayout frameLayout;

    @Override
    protected void onStop() {
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("loggedIn").setValue("false");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        FirebaseAuth.getInstance().signOut();
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("loggedIn").setValue("true");
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupDrawerContent(navigationView);
        setTitle("THỐNG KÊ");
        replaceFragment(new Statistic_Fragment());
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
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count != 0) {
            super.onBackPressed();
        } else {
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
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                setSelectedItemDrawer(item);
                return true;
            }
        });
    }

    private void setSelectedItemDrawer(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accountType:
                setTitle("VÍ CÁ NHÂN");
                AccountTypeFragment accountTypeFragment = new AccountTypeFragment();
                replaceFragment(accountTypeFragment);
                break;
            case R.id.income:
                setTitle("KHOẢN THU");
                Incomes_Fragment incomes_fragment = new Incomes_Fragment();
                replaceFragment(incomes_fragment);
                break;
            case R.id.expense:
                setTitle("KHOẢN CHI");
                Expenses_Fragment expenses_fragment = new Expenses_Fragment();
                replaceFragment(expenses_fragment);
                break;
            case R.id.statistic:
                setTitle("THỐNG KÊ");
                Statistic_Fragment searchFragment = new Statistic_Fragment();
                replaceFragment(searchFragment);
                break;
            case R.id.introduce:
                setTitle("GIỚI THIỆU");
                Intro_Fragment settingsFragment = new Intro_Fragment();
                replaceFragment(settingsFragment);
                break;
            case R.id.changePass:
                setTitle("ĐỔI MẬT KHẨU");
                ChangePasswordFragment changepasswordFragment = new ChangePasswordFragment();
                replaceFragment(changepasswordFragment);
                break;
            case R.id.logout:
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
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }
}
