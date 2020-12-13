package com.example.project3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.project3.dao.DAOUsers;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private Button btReg, btLogin;
    EditText edtUsername, edtPassword;
    CheckBox cdAutoLogin;

    List<Users> usersList;
    LinearLayout linearLayout;
    Animation animation;
    boolean doubleBackToExitPressedOnce = false;
    boolean autoLogin = false;

    DatabaseReference mData;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        animation = AnimationUtils.loadAnimation(this, R.anim.ogin_signin_animation);
        linearLayout.startAnimation(animation);

        layThongTin();

        getUserList();


        if (autoLogin) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }


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

    private void getUserList() {
        mData.child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Users temp = snapshot.getValue(Users.class);
                usersList.add(temp);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void login(String email, String password) {
        String em = email + "@gmail.com";
        mAuth.signInWithEmailAndPassword(em, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                            saveUserData();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            overridePendingTransition(R.anim.ani_intent, R.anim.ani_intenexit);
                            //TODO: edit on DB
                        } else {
                            Toast.makeText(LoginActivity.this, "Tên tài khoản hoặc mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveUserData() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String userName = edtUsername.getText().toString();
        String pass = edtPassword.getText().toString();
        boolean check = cdAutoLogin.isChecked();
        autoLogin = check;
        if (!check) {
            editor.clear();
        } else {
            editor.putString("userName", userName);
            editor.putString("pass", pass);
            editor.putBoolean("checkStatus", check);
            editor.putBoolean("autoLogin", autoLogin);
        }
        editor.commit();

    }

    private void layThongTin() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);

        boolean check = sharedPreferences.getBoolean("checkStatus", false);
        if (check) {
            String userName = sharedPreferences.getString("userName", "");
            String pass = sharedPreferences.getString("pass", "");
            autoLogin = sharedPreferences.getBoolean("autoLogin", false);
            edtUsername.setText(userName);
            edtPassword.setText(pass);
        } else {
            edtUsername.setText("");
            edtPassword.setText("");
        }
        cdAutoLogin.setChecked(check);
    }

    private void init() {
        mData = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        usersList = new ArrayList<>();
        linearLayout = findViewById(R.id.linearLayoutlogin);
        edtUsername = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);
        btLogin = findViewById(R.id.btnLogin);
        btReg = findViewById(R.id.btnRegister);
        cdAutoLogin = findViewById(R.id.cbLuuThongTin);
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
            mAuth.signOut();
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


