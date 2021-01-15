package com.example.project3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.project3.dao.DAOUsers;

public class RegisterActivity extends AppCompatActivity {
    private RelativeLayout rLayout;
    private Animation animation;
    private EditText edtRegUsername, edtRegPassword, edtRegPassCheck;
    private Button btnRegister, btnEraseAll;
    private DAOUsers daoUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(Constants.TAG, "btn reg click");
                String userName = edtRegUsername.getText().toString();
                String password = edtRegPassword.getText().toString();
                String confirmPass = edtRegPassCheck.getText().toString();
                boolean accountCheck = true, passCheck = false;
                passCheck = password.matches(confirmPass);
                if (userName.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Tên tài khoản không được để trống!", Toast.LENGTH_SHORT).show();
                } else {
                    if (password.isEmpty() || confirmPass.isEmpty()) {
                        Toast.makeText(RegisterActivity.this, "Mật khẩu không được để trống!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (accountCheck) {
                            if (passCheck) {
                                daoUsers.userSignIn(userName, password, RegisterActivity.this);
                            } else {
                                Toast.makeText(RegisterActivity.this, "Mật khẩu không khớp nhau!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "Tên tài khoản không được trùng!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }


        });
        btnEraseAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtRegPassCheck.setText("");
                edtRegPassword.setText("");
                edtRegUsername.setText("");
            }
        });
    }

    private void init() {
        daoUsers = new DAOUsers();
        edtRegUsername = findViewById(R.id.edtRegUser);
        edtRegPassword = findViewById(R.id.edtRegPassword);
        edtRegPassCheck = findViewById(R.id.edtRePassword);
        btnRegister = findViewById(R.id.btnReg);
        btnEraseAll = findViewById(R.id.btnRelay);
        rLayout = findViewById(R.id.rlayout);
        animation = AnimationUtils.loadAnimation(this, R.anim.ogin_signin_animation);
        rLayout.setAnimation(animation);
    }
}
