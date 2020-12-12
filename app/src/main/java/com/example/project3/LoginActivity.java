package com.example.project3;

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

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private Button btReg, btLogin;
    EditText edtUsername, edtPassword;
    CheckBox cdAutoLogin;
    DAOUsers daoUsers;
    ArrayList<Users> usersList = new ArrayList<>();
    LinearLayout linearLayout;
    Animation animation;
    boolean doubleBackToExitPressedOnce = false;
    boolean autoLogin = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        animation = AnimationUtils.loadAnimation(this, R.anim.ogin_signin_animation);
        linearLayout.startAnimation(animation);

        layThongTin();

        if (autoLogin) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }


        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checkAccount = false;
                daoUsers = new DAOUsers(LoginActivity.this);
                String tenTK = edtUsername.getText().toString();
                String mk = edtPassword.getText().toString();
                usersList = daoUsers.getALl();
                for (int i = 0; i < usersList.size(); i++) {
                    Users tkx = usersList.get(i);
                    if (tkx.getUsername().matches(tenTK) && tkx.getPassword().matches(mk)) {
                        checkAccount = true;
                        break;
                    }
                }
                if (checkAccount == true) {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    saveUserData();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    overridePendingTransition(R.anim.ani_intent, R.anim.ani_intenexit);

                } else {
                    Toast.makeText(LoginActivity.this, "Tên tài khoản hoặc mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
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

    private void saveUserData() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String ten = edtUsername.getText().toString();
        String pass = edtPassword.getText().toString();
        boolean check = cdAutoLogin.isChecked();
        autoLogin = check;
        if (!check) {
            editor.clear();
        } else {
            editor.putString("tennguoidung", ten);
            editor.putString("matkhau", pass);
            editor.putBoolean("checkstatus", check);
            editor.putBoolean("autoLogin", autoLogin);
        }
        editor.commit();

    }

    private void layThongTin() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);

        boolean check = sharedPreferences.getBoolean("checkstatus", false);
        if (check) {
            String tenNguoiDung = sharedPreferences.getString("tennguoidung", "");
            String matKhau = sharedPreferences.getString("matkhau", "");
            autoLogin = sharedPreferences.getBoolean("autoLogin", false);
            edtUsername.setText(tenNguoiDung);
            edtPassword.setText(matKhau);
        } else {
            edtUsername.setText("");
            edtPassword.setText("");
        }
        cdAutoLogin.setChecked(check);
    }

    private void init() {
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
            String tk = data.getStringExtra("taikhoan");
            String mk = data.getStringExtra("matkhau");
            edtUsername.setText(tk);
            edtPassword.setText(mk);
        }

    }
}


