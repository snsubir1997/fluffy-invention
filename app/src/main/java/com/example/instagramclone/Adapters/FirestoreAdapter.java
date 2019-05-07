package com.example.instagramclone.Adapters;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FirestoreAdapter {

    private static final String TAG = "Firestore";
    private FirebaseFirestore db;

    public FirestoreAdapter(FirebaseFirestore db) {
        this.db = db;
    }

    public void addData(String name, String username, String email, String mobile_number, String dob, String uid) {

        int age = calcAge();

        Map<String, Object> user = new HashMap<>();
        user.put("uid",uid);
        user.put("name", name);
        user.put("username", username);
        user.put("email", email);
        user.put("phone_number", mobile_number);
        user.put("date_of_birth", dob);
        user.put("posts", Collections.emptyList());
        user.put("follows", Collections.emptyList());
        user.put("age",age);

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    private int calcAge() {
        LocalDate today = LocalDate.now();                          //Today's date
        LocalDate birthday = LocalDate.of(1997, Month.DECEMBER, 4);  //Birth date

        Period p = Period.between(birthday, today);

        return p.getYears();
    }
}
