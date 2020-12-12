package com.example.project3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.project3.dao.DAOUsers;
import com.example.project3.model.Users;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {
    private RelativeLayout rlayout;
    private Animation animation;
    EditText edtRegUsername, edtRegPassword, edtRegPassCheck;
    Button btnRegister, btnEraseAll;
    ArrayList<Users> usersList = new ArrayList<>();
    DAOUsers daoUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();

        animation = AnimationUtils.loadAnimation(this, R.anim.ogin_signin_animation);
        rlayout.setAnimation(animation);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daoUsers = new DAOUsers(RegisterActivity.this);

                String tk = edtRegUsername.getText().toString();
                String mk = edtRegPassword.getText().toString();
                String confirmMk = edtRegPassCheck.getText().toString();

                boolean accountCheck = true, passCheck = false;
                Users tkmk = new Users(tk, mk);

                usersList = daoUsers.getALl();

                if (mk.matches(confirmMk)) {
                    passCheck = true;
                } else {
                    passCheck = false;
                }

                for (int i = 0; i < usersList.size(); i++) {
                    Users tkx = usersList.get(i);
                    if (tkx.getUsername().matches(tk)) {
                        accountCheck = false;
                        break;
                    }
                }

                if (tk.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Tên tài khoản không được để trống!", Toast.LENGTH_SHORT).show();
                } else {
                    if (mk.isEmpty() || confirmMk.isEmpty()) {
                        Toast.makeText(RegisterActivity.this, "Mật khẩu không được để trống!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (accountCheck == true) {
                            if (passCheck == true) {
                                daoUsers.addUser(tkmk);
                                Toast.makeText(RegisterActivity.this, "Thêm tài khoản thành công!", Toast.LENGTH_SHORT).show();

                                Intent i = new Intent();
                                i.putExtra("taikhoan", tk);
                                i.putExtra("matkhau", mk);
                                setResult(RESULT_OK, i);
                                finish();

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

        edtRegUsername = findViewById(R.id.edtRegUser);
        edtRegPassword = findViewById(R.id.edtRegPassword);
        edtRegPassCheck = findViewById(R.id.edtRePassword);
        btnRegister = findViewById(R.id.btnReg);
        btnEraseAll = findViewById(R.id.btnRelay);
        rlayout = findViewById(R.id.rlayout);
    }
}
