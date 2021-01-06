package com.example.project3.dao;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.project3.Constant;
import com.example.project3.MainActivity;
import com.example.project3.R;
import com.example.project3.RegisterActivity;
import com.example.project3.adapter.AccountTypeAdapter;
import com.example.project3.database.Database;
import com.example.project3.model.AccountType;
import com.example.project3.model.Transactions;
import com.example.project3.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class DAOUsers {
    private Database database;
    private boolean check = false;
    private List<AccountType> accountTypeList;
    private AccountTypeAdapter accountTypeAdapter;

    public DAOUsers() {
        this.database = new Database();
        accountTypeList = new ArrayList<>();
    }

    public long getTotalMoney() {
        long total = 0;
        for (AccountType x: accountTypeList) {
            total += x.getAmountMoney();
        }
        return total;
    }

    public AccountTypeAdapter getAccountTypeAdapter() {
        return accountTypeAdapter;
    }

    public List<AccountType> getAccountTypeList() {
        return accountTypeList;
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

    public void createAccountTypeAdapter(Context context) {
        accountTypeAdapter = new AccountTypeAdapter(context, accountTypeList, R.layout.oneitem_recylerview, DAOUsers.this);
    }
    
    public void addAccountTypeListener() {
        database.getDatabase().child("Users").child(database.getAuthentication().getCurrentUser().getUid().toString()).child("account").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                AccountType accountType = snapshot.getValue(AccountType.class);
                accountTypeList.add(accountType);
                accountTypeAdapter.notifyItemInserted(accountTypeList.indexOf(accountType));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                AccountType accountType = snapshot.getValue(AccountType.class);
                for (AccountType x: accountTypeList) {
                    if (x.getAccountTypeID().matches(accountType.getAccountTypeID())) {
                        accountTypeList.set(accountTypeList.indexOf(x), accountType);
                        accountTypeAdapter.notifyItemChanged(accountTypeList.indexOf(accountType));
                        break;
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                AccountType accountType = snapshot.getValue(AccountType.class);
                for (AccountType x: accountTypeList) {
                    if (x.getAccountTypeID().matches(accountType.getAccountTypeID())) {
                        accountTypeAdapter.notifyItemRemoved(accountTypeList.indexOf(x));
                        accountTypeList.remove(x);
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addAccountType(String accountType, long money, final Context context) {
        AccountType accountType1 = new AccountType(database.getDatabase().push().getKey(), accountType, money);
        database.getDatabase().child("Users").child(database.getAuthentication().getCurrentUser().getUid().toString()).child("account").child(accountType1.getAccountTypeID()).setValue(accountType1)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void deleteAccountType(final Context context, AccountType accountType, final TextView tvMessage, final ProgressBar progressBar, final Dialog dialog) {
        database.getDatabase().child("Users").child(database.getAuthentication().getCurrentUser().getUid().toString()).child("account")
                .child(accountType.getAccountTypeID()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    tvMessage.setText("");
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        }
                    }, 1000);
                } else {
                    Toast.makeText(context, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void editAccountType(AccountType accountType, final Context context, final Dialog dialog) {
        database.getDatabase().child("Users").child(database.getAuthentication().getCurrentUser().getUid().toString()).child("account")
                .child(accountType.getAccountTypeID()).setValue(accountType).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Sửa thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Sửa thất bại!", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }
}
