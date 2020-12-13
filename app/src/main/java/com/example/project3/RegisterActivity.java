package com.example.project3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.project3.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.concurrent.Executor;

public class RegisterActivity extends AppCompatActivity {
    private RelativeLayout rlayout;
    private Animation animation;
    EditText edtRegUsername, edtRegPassword, edtRegPassCheck;
    Button btnRegister, btnEraseAll;

    DatabaseReference mData;
    FirebaseAuth mAuth;

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
                String userName = edtRegUsername.getText().toString();
                String password = edtRegPassword.getText().toString();
                String confirmPass = edtRegPassCheck.getText().toString();

                boolean accountCheck = true, passCheck = false;


                if (password.matches(confirmPass)) {
                    passCheck = true;
                } else {
                    passCheck = false;
                }

                if (userName.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Tên tài khoản không được để trống!", Toast.LENGTH_SHORT).show();
                } else {
                    if (password.isEmpty() || confirmPass.isEmpty()) {
                        Toast.makeText(RegisterActivity.this, "Mật khẩu không được để trống!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (accountCheck == true) {
                            if (passCheck == true) {

                                addUser(userName, password);


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

    private void addUser(String userName, String password) {
        final String usn = userName + "@gmail.com";
        final String pwd = password;
        mAuth.createUserWithEmailAndPassword(usn, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Users tempUser = new Users(usn.substring(0, usn.length() - 10), pwd, FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                            //add user in db
                            mData.push().setValue(tempUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Thành công!", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent();
                                        i.putExtra("userName", usn.substring(0, usn.length() - 10));
                                        i.putExtra("pass", pwd);
                                        setResult(RESULT_OK, i);
                                        finish();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Thất bại DB!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                        } else {
                            Toast.makeText(RegisterActivity.this, "Thất bại AUTHEN!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void init() {

        mData = FirebaseDatabase.getInstance().getReference("Users");
        mAuth = FirebaseAuth.getInstance();

        edtRegUsername = findViewById(R.id.edtRegUser);
        edtRegPassword = findViewById(R.id.edtRegPassword);
        edtRegPassCheck = findViewById(R.id.edtRePassword);
        btnRegister = findViewById(R.id.btnReg);
        btnEraseAll = findViewById(R.id.btnRelay);
        rlayout = findViewById(R.id.rlayout);
    }
}
