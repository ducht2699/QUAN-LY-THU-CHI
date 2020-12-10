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

import com.example.project3.dao.DaoTaiKhoan;
import com.example.project3.model.TaikhoanMatKhau;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {
    private RelativeLayout rlayout;
    private Animation animation;
    EditText txtRegTk, txtRegMk, txtRegConfirmMk;
    Button btDangKy, btNhapLai;
    ArrayList<TaikhoanMatKhau> accList = new ArrayList<>();
    DaoTaiKhoan tkDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();

        animation = AnimationUtils.loadAnimation(this, R.anim.dangnhap_dangky_animation);
        rlayout.setAnimation(animation);

        btDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tkDao = new DaoTaiKhoan(RegisterActivity.this);

                String tk = txtRegTk.getText().toString();
                String mk = txtRegMk.getText().toString();
                String confirmMk = txtRegConfirmMk.getText().toString();

                boolean accountCheck = true, passCheck = false;
                TaikhoanMatKhau tkmk = new TaikhoanMatKhau(tk, mk);

                accList = tkDao.getALl();

                if (mk.matches(confirmMk)) {
                    passCheck = true;
                } else {
                    passCheck = false;
                }

                for (int i = 0; i < accList.size(); i++) {
                    TaikhoanMatKhau tkx = accList.get(i);
                    if (tkx.getTenTaiKhoan().matches(tk)) {
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
                                tkDao.Add(tkmk);
                                Toast.makeText(RegisterActivity.this, "Thêm tài khoản thành công!", Toast.LENGTH_SHORT).show();

                                Intent i = new Intent();
                                i.putExtra("taikhoan",tk);
                                i.putExtra("matkhau",mk);
                                setResult(RESULT_OK,i);
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

        btNhapLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtRegConfirmMk.setText("");
                txtRegMk.setText("");
                txtRegTk.setText("");
            }
        });
    }
    private void init() {

        txtRegTk = findViewById(R.id.edtRegUser);
        txtRegMk = findViewById(R.id.edtRegPassword);
        txtRegConfirmMk = findViewById(R.id.edtRePassword);
        btDangKy = findViewById(R.id.btnReg);
        btNhapLai = findViewById(R.id.btnRelay);
        rlayout = findViewById(R.id.rlayout);
    }
}
