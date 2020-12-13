package com.example.project3.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.project3.MainActivity;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class DAOUsers {

    List<Users> usersList = new ArrayList<>();
    DatabaseReference mData;
    FirebaseAuth mAuth;

    boolean addUserCheck = false;
    boolean changePassCheck = false;

    public DAOUsers(DatabaseReference mData, FirebaseAuth mAuth) {
        this.mData = FirebaseDatabase.getInstance().getReference("Users");
        this.mAuth = FirebaseAuth.getInstance();
    }

    public void addListener() {
        mData.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Users tempUser = snapshot.getValue(Users.class);
                usersList.add(tempUser);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Users tempUser = snapshot.getValue(Users.class);
                for (Users user : usersList) {
                    if (user.getUID().equals(tempUser.getUID())) {
                        user = tempUser;
                        break;
                    }
                }
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

    public List<Users> getUserList() {
        return usersList;
    }
    public boolean addUser(String userName, String password) {

        //authentication
        mAuth.createUserWithEmailAndPassword(userName, password)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            addUserCheck = true;
                        } else {
                            addUserCheck = false;
                        }
                    }
                });
        if (addUserCheck) {
            //add user in list
            Users tempUser = new Users(userName, password, mAuth.getCurrentUser().getUid().toString());
            //add user in db
            mData.child(tempUser.getUID()).setValue(tempUser);
        }
        return addUserCheck;
    }


    public boolean changePass(Users user) {
        //change
        mData.child(user.getUID()).setValue(user);
        mAuth.getCurrentUser().updatePassword(user.getPassword()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    changePassCheck = true;
                }
                else {
                    changePassCheck = false;
                }
            }
        });
        return changePassCheck;
    }

    public void signOut() {
        mAuth.signOut();
    }
}
