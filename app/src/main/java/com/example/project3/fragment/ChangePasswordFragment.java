package com.example.project3.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
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

import com.example.project3.MainActivity;
import com.example.project3.R;
import com.example.project3.dao.DAOUsers;
import com.example.project3.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ChangePasswordFragment extends Fragment {
    private EditText edtUsername, edtOldPass, edtNewPass;
    private Button btnChangePass, btnEraseAll;
    private Animation animation;
    private LinearLayout linearLayout;
    private List<Users> usersList = new ArrayList<>();
    private View view;
    private boolean changePassCheck = false;

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
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean xetMk = true;
                String userName = edtUsername.getText().toString();
                String oldPass = edtOldPass.getText().toString();
                String newPass = edtNewPass.getText().toString();
                if (oldPass.matches(newPass)) {
                    xetMk = false;
                } else {
                    xetMk = true;
                }
                if (userName.isEmpty()) {
                    Toast.makeText(getContext(), "Tên tài khoản không được để trống!", Toast.LENGTH_SHORT).show();
                } else {
                    if (oldPass.isEmpty() || newPass.isEmpty()) {
                        Toast.makeText(getContext(), "Mật khẩu không được để trống!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (xetMk == true) {
                            changePass(userName, oldPass, newPass);
                        } else {
                            Toast.makeText(getContext(), "Mật khẩu cũ không được trùng với mật khẩu mới!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }


            }
        });
        btnEraseAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtUsername.setText("");
                edtOldPass.setText("");
                edtNewPass.setText("");
            }
        });
        return view;
    }

    private void changePass(String userName, String oldPass, final String newPass) {
        //TODO: check account and password
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference("Users");
        mData.child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("password").setValue(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    FirebaseAuth.getInstance().getCurrentUser().updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                                changePassCheck = true;
                            else
                                changePassCheck = false;
                        }
                    });
                } else changePassCheck = false;
            }
        });
        if (changePassCheck) {
            Toast.makeText(getContext(), "Thay đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Thay đổi mật khẩu thất bại!", Toast.LENGTH_SHORT).show();
        }
        edtUsername.setText("");
        edtOldPass.setText("");
        edtNewPass.setText("");
    }

    private void init() {
        edtUsername = view.findViewById(R.id.edtCUser);
        edtOldPass = view.findViewById(R.id.edtCPass);
        edtNewPass = view.findViewById(R.id.edtNewPass);
        btnChangePass = view.findViewById(R.id.btnChange);
        btnEraseAll = view.findViewById(R.id.btnRelay);
        linearLayout = view.findViewById(R.id.linearLayoutchange);
        animation = AnimationUtils.loadAnimation(getContext(), R.anim.ogin_signin_animation);
        linearLayout.setAnimation(animation);
    }
}
