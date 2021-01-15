package com.example.project3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.project3.dao.DAOUsers;

public class LoginActivity extends AppCompatActivity {
    private Button btReg, btLogin;
    private EditText edtUsername, edtPassword;
    private CheckBox cbAutoLogin;
    private LinearLayout linearLayout;
    private Animation animation;
    private boolean doubleBackToExitPressedOnce = false;
    private DAOUsers daoUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();
                if (userName.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Không được bỏ trống!", Toast.LENGTH_SHORT).show();
                } else {
                    daoUsers.userLogin(userName, password, LoginActivity.this, cbAutoLogin.isChecked());
                }
            }
        });
        btReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(i, 999);
                overridePendingTransition(R.anim.ani_intent, R.anim.ani_intenexit);
            }
        });
    }

    private void getUserData() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        boolean check = sharedPreferences.getBoolean("autoLogin", false);
        cbAutoLogin.setChecked(check);
        if (check) {
            String userName = sharedPreferences.getString("userName", "");
            String pass = sharedPreferences.getString("pass", "");
            edtUsername.setText(userName);
            edtPassword.setText(pass);
            daoUsers.userLogin(userName, pass, LoginActivity.this, cbAutoLogin.isChecked());
        } else {
            edtUsername.setText("");
            edtPassword.setText("");
        }
    }

    private void init() {
        daoUsers = new DAOUsers();
        //check if user is logged in --> sign out
        if (daoUsers.userIsLoggedIn() == true) {
            Log.d(Constants.TAG, "user existed");
            daoUsers.userSignOut();
        }
        linearLayout = findViewById(R.id.linearLayoutlogin);
        edtUsername = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);
        btLogin = findViewById(R.id.btnLogin);
        btReg = findViewById(R.id.btnRegister);
        cbAutoLogin = findViewById(R.id.cbLuuThongTin);
        animation = AnimationUtils.loadAnimation(this, R.anim.ogin_signin_animation);
        linearLayout.startAnimation(animation);
        //get data from SharedRef
        getUserData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999 && resultCode == RESULT_OK) {
            String tk = data.getStringExtra("userName");
            String mk = data.getStringExtra("pass");
            edtUsername.setText(tk);
            edtPassword.setText(mk);
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            if (daoUsers.userIsLoggedIn() == true) {
                daoUsers.userSignOut();
            }
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Nhấn Back một lần nữa để thoát!", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 1500);
    }
}


