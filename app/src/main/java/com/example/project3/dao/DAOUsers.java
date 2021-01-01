package com.example.project3.dao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.project3.Constant;
import com.example.project3.MainActivity;
import com.example.project3.R;
import com.example.project3.RegisterActivity;
import com.example.project3.database.Database;
import com.example.project3.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executor;

public class DAOUsers {
    private Database database;
    private boolean check = false;

    public DAOUsers() {
        this.database = new Database();
    }

    public void userLogin(final String email, final String password, final Context context, final boolean cb) {
        final String em = email + "@gmail.com";
        database.getAuthentication().signInWithEmailAndPassword(em, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //change log in state in DB
                            database.getDatabase().child("Users").child(database.getAuthentication().getCurrentUser().getUid().toString()).child("loggedIn").setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(context, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                        saveUserData(email, password, context, cb);
                                        context.startActivity(new Intent(context, MainActivity.class));
                                        Activity activity = (Activity) context;
                                        activity.overridePendingTransition(R.anim.ani_intent, R.anim.ani_intenexit);
                                    } else {

                                        Log.d(Constant.TAG, "add DB failed + " + task.getException());
                                    }
                                }
                            });
                        } else {
                            Log.d(Constant.TAG, "login failed - " + task.getException());
                        }
                    }
                });
    }

    private void saveUserData(String userName, String pass, Context context, boolean check) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserData", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (!check) {
            editor.clear();
        } else {
            editor.putString("userName", userName);
            editor.putString("pass", pass);
            editor.putBoolean("autoLogin", check);
        }
        editor.commit();
    }

    public void userSignIn(String userName, String password, final Context context) {
        final String usn = userName + "@gmail.com";
        final String pwd = password;
        database.getAuthentication().createUserWithEmailAndPassword(usn, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Users tempUser = new Users(usn.substring(0, usn.length() - 10), pwd, FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                            //add user in db
                            database.getDatabase().child("Users").child(tempUser.getUID()).setValue(tempUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(context, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent();
                                        i.putExtra("userName", usn.substring(0, usn.length() - 10));
                                        i.putExtra("pass", pwd);
                                        Activity activity = (Activity) context;
                                        activity.setResult(Activity.RESULT_OK, i);
                                        activity.finish();
                                    } else {
                                        Toast.makeText(context, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                                        Log.d(Constant.TAG, "add database failed - " + task.getException());
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(context, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                            Log.d(Constant.TAG, "auth failed - " + task.getException());
                        }
                    }
                });
    }

    public void userChangePass(String newPass) {

    }

    public boolean userIsLoggedIn() {
        if (database.getAuthentication().getCurrentUser() != null) {
            return true;
        }
        return false;
    }

    public void userSignOut() {
        database.getAuthentication().signOut();
    }
}
