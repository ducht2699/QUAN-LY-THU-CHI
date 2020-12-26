package com.example.project3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.example.project3.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LOGIN ACTIVITY TAG ";
    private Button btReg, btLogin;
    EditText edtUsername, edtPassword;
    CheckBox cbAutoLogin;
    LinearLayout linearLayout;
    Animation animation;
    boolean doubleBackToExitPressedOnce = false;
    DatabaseReference mData;
    FirebaseAuth mAuth;

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
                } else
                    login(userName, password);
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

    private void autoLogin() {
        login(edtUsername.getText().toString(), edtPassword.getText().toString());
    }

    private void login(String email, String password) {
        String em = email + "@gmail.com";
        mAuth.signInWithEmailAndPassword(em, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //change log in state in DB
                            mData.child("Users").child(mAuth.getCurrentUser().getUid().toString()).child("loggedIn").setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                        saveUserData();
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        overridePendingTransition(R.anim.ani_intent, R.anim.ani_intenexit);
                                    } else {
                                        Log.d(TAG, "add DB failed + " + task.getException());
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(LoginActivity.this, "Tên tài khoản hoặc mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "login failed - " + task.getException());
                        }
                    }
                });
    }

    private void saveUserData() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String userName = edtUsername.getText().toString();
        String pass = edtPassword.getText().toString();
        boolean check = cbAutoLogin.isChecked();
        if (!check) {
            editor.clear();
        } else {
            editor.putString("userName", userName);
            editor.putString("pass", pass);
            editor.putBoolean("autoLogin", check);
        }
        editor.commit();
    }

    private void init() {
        mData = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        //check if user is logged in --> sign out
        if (mAuth.getCurrentUser() != null) {
            Log.d(TAG, "user existed");
            mAuth.signOut();
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
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        boolean check = sharedPreferences.getBoolean("autoLogin", false);
        cbAutoLogin.setChecked(check);
        if (check) {
            String userName = sharedPreferences.getString("userName", "");
            String pass = sharedPreferences.getString("pass", "");
            edtUsername.setText(userName);
            edtPassword.setText(pass);
            autoLogin();
        } else {
            edtUsername.setText("");
            edtPassword.setText("");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Resume end");
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
            if (mAuth.getCurrentUser() != null) {
                mAuth.signOut();
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
        }, 2000);
    }
}


