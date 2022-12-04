package com.n21.choizy;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.NumberFormat;
import java.util.Currency;

public class DBClass {

    FirebaseDatabase db;
    DatabaseReference myRef;
    FirebaseStorage firebaseStorage ;
    StorageReference myStorage;
    static Context context;

    public static final String ChoizySP = "Choizy_Student_SharedPreference";


    public DBClass(Context context) {
        this.context = context;
        db = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        myStorage = firebaseStorage.getReference();
        myRef = db.getReference();

    }

    public static Context getContext() {
        return context;
    }


}
