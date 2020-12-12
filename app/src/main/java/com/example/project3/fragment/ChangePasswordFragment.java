package com.example.project3.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.project3.R;
import com.example.project3.dao.DAOUsers;
import com.example.project3.model.Users;

import java.util.ArrayList;

public class ChangePasswordFragment extends Fragment {
    EditText txtCTk, txtCpass, txtNewPass;
    Button btChangePass, btNhapLai;
    DAOUsers tkDao;
    Animation animation;
    LinearLayout linearLayout;
    ArrayList<Users> listTk = new ArrayList<>();
    View view;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_changepassword, container, false);
        init();

        animation = AnimationUtils.loadAnimation(getContext(), R.anim.ogin_signin_animation);
        linearLayout.setAnimation(animation);

        btChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean xetTk = false, xetMk = true;
                tkDao = new DAOUsers(getContext());
                String tk = txtCTk.getText().toString();
                String mk = txtCpass.getText().toString();
                String mkk = txtNewPass.getText().toString();
                Users tkmkMoi = new Users(tk, mkk);
                listTk = tkDao.getALl();

                //Check tk, mk có khớp vs tk trong list k
                for (int i = 0; i < listTk.size(); i++) {
                    Users tkx = listTk.get(i);
                    if (tkx.getUsername().matches(tk) && tkx.getPassword().matches(mk)) {
                        xetTk = true;
                        break;
                    }
                }

                if (mk.matches(mkk)) {
                    xetMk = false;
                } else {
                    xetMk = true;
                }

                if (tk.isEmpty()) {
                    Toast.makeText(getContext(), "Tên tài khoản không được để trống!", Toast.LENGTH_SHORT).show();
                } else {
                    if (mk.isEmpty() || mkk.isEmpty()) {
                        Toast.makeText(getContext(), "Mật khẩu không được để trống!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (xetTk == true) {
                            if (xetMk == true) {
                                tkDao.changePass(tkmkMoi);
                                Toast.makeText(getContext(), "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Mật khẩu cũ không được trùng với mật khẩu mới!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Tên tài khoản hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        });

        btNhapLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtCTk.setText("");
                txtCpass.setText("");
                txtNewPass.setText("");
            }
        });
        return view;
    }

    private void init() {
        txtCTk = view.findViewById(R.id.edtCUser);
        txtCpass = view.findViewById(R.id.edtCPass);
        txtNewPass = view.findViewById(R.id.edtNewPass);
        btChangePass = view.findViewById(R.id.btnChange);
        btNhapLai = view.findViewById(R.id.btnRelay);
        linearLayout = view.findViewById(R.id.linearLayoutchange);
    }

}
