package com.example.project3.database;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Database {
    private DatabaseReference database;
    private FirebaseAuth authentication;

    public Database() {
        this.database = FirebaseDatabase.getInstance().getReference();
        this.authentication = FirebaseAuth.getInstance();
    }

    public DatabaseReference getDatabase() {
        return database;
    }

    public FirebaseAuth getAuthentication() {
        return authentication;
    }
}
