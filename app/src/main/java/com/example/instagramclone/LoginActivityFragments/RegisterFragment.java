package com.example.instagramclone.LoginActivityFragments;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.instagramclone.Adapters.FirestoreAdapter;
import com.example.instagramclone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private boolean isRegistered = false;

    private EditText e1,e2,e3,e4,e5,e6,e7;
    TextInputLayout iL1,iL2;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private static final String TAG = "EmailPassword";
    private ProgressDialog progress;
    private FirestoreAdapter firestoreAdapter;
    private String email,password,name,confirm_password,mobile_number,username,date_of_birth;
    private int mYear, mMonth, mDay, mHour, mMinute;

    public RegisterFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();

        db.setFirestoreSettings(settings);
        firestoreAdapter = new FirestoreAdapter(db);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        iL1 = view.findViewById(R.id.passwordInputLayout);
        iL2 = view.findViewById(R.id.confirm_passwordInputLayout);

        e1 = view.findViewById(R.id.person_nameEditText);
        e2 = view.findViewById(R.id.user_nameEditText);
        e3 = view.findViewById(R.id.email_idEditText);
        e4 = view.findViewById(R.id.passwordEditText);
        e5 = view.findViewById(R.id.confirm_passwordEditText);
        e6 = view.findViewById(R.id.date_of_birthEditText);
        e7 = view.findViewById(R.id.mobile_noEditText);

        e6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                e6.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        Button b1 = view.findViewById(R.id.registerButton);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateForm()) {
                    return;
                }

                getFieldData();

                isUserPresent();

            }
        });
    }

    private void getFieldData() {
        name = e1.getText().toString();
        username = e2.getText().toString();
        email = e3.getText().toString();
        password = e4.getText().toString();
        confirm_password = e5.getText().toString();
        date_of_birth = e6.getText().toString();
        mobile_number = e7.getText().toString();
    }

    @Override
    public void onStart() {
        super.onStart();
        //FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void signOut() {
        mAuth.signOut();
    }

    private void createAccount(String email, String password) {

        Log.d(TAG, "createAccount:" + email);

        showProgressDialog("Signing You Up");
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(Objects.requireNonNull(getActivity()), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                            //FirebaseUser user = mAuth.getCurrentUser();
                            sendEmailVerification();
                            //clearFields();

                            signOut();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //add alert Dialog Box
                        }
                        hideProgressDialog();
                    }
                });
    }

    private void sendEmailVerification() {

        final FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(Objects.requireNonNull(getActivity()), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(),
                                        "Verification email sent to " + user.getEmail(),
                                        Toast.LENGTH_SHORT).show();
                                firestoreAdapter.addData(name, username, email, mobile_number, date_of_birth, user.getUid());
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name)
                                        .build();
                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    /*Toast.makeText(getContext(),"User profile updated.",Toast.LENGTH_SHORT)
                                                            .show();*/
                                                    Log.d(TAG, "User profile updated.");
                                                }
                                            }
                                        });

                            } else {
                                Log.e(TAG, "sendEmailVerification", task.getException());
                                Toast.makeText(getContext(),
                                        "Failed to send verification email.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void clearFields() {
        e1.setText("");
        e2.setText("");
        e3.setText("");
        e4.setText("");
        e5.setText("");
        e6.setText("");
        e7.setText("");
    }

    private void showProgressDialog(String message) {
        if (progress == null) {
            progress = new ProgressDialog(getContext());
            progress.setMessage(message);
            progress.setIndeterminate(true);
        }
        progress.show();
    }


    private void hideProgressDialog() {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }





    private boolean validateForm() {
        boolean valid = true;

        String fullName = e1.getText().toString();
        if (TextUtils.isEmpty(fullName)) {
            e1.setError("Required.");
            valid = false;
        } else if (!isFullname(fullName)){
            e1.setError("Please enter a valid name");
            valid = false;
        } else {
            e1.setError(null);
        }

        String username = e2.getText().toString();
        if (TextUtils.isEmpty(username)) {
            e2.setError("Required.");
            valid = false;
        } else if (!isUsername(username)){
            e2.setError("Please enter a valid username");
            valid = false;
        } else {
            e2.setError(null);
        }


        String emailId = e3.getText().toString();
        if (TextUtils.isEmpty(emailId)) {
            e3.setError("Required.");
            valid = false;
        } else if (!isEmail(emailId)){
            e3.setError("Please enter a valid email");
            valid = false;
        } else {
            e3.setError(null);
        }

        String password = e4.getText().toString();
        if (TextUtils.isEmpty(password)) {
            iL1.setPasswordVisibilityToggleEnabled(false);
            iL1.setError("Required.");
            valid = false;
        } else if (!isGoodPassword(password)){
            iL1.setPasswordVisibilityToggleEnabled(false);
            iL1.setError("Please enter a valid password");
            valid = false;
        } else {
            iL1.setError(null);
            iL1.setPasswordVisibilityToggleEnabled(true);
        }

        String confirm_password = e5.getText().toString();
        if (TextUtils.isEmpty(password)) {
            iL2.setPasswordVisibilityToggleEnabled(false);
            iL2.setError("Required.");
            valid = false;
        } else if (!confirm_password.equals(password)) {
            iL2.setPasswordVisibilityToggleEnabled(false);
            iL2.setError("Passwords Do not match.");
            valid = false;
        } else {
            e5.setError(null);
            iL2.setPasswordVisibilityToggleEnabled(true);
        }

        String mobile = e7.getText().toString();
        if (TextUtils.isEmpty(mobile)) {
            e7.setError("Required.");
            valid = false;
        } else if (!isGoodMobileNumber(mobile)){
            e7.setError("Please enter a valid mobile number.");
            valid = false;
        } else {
            e7.setError(null);
        }

        return valid;
    }

    public static boolean isFullname(String str) {
        String expression = "^[a-zA-Z\\s]+";
        return str.matches(expression);
    }
    public static boolean isUsername(String str) {
        String expression = "^[a-zA-Z0-9._-]{3,}$";
        return str.matches(expression);
    }
    public static boolean isEmail(String str) {
        String expression = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        return str.matches(expression);
    }
    public static boolean isGoodPassword(String str) {
        String expression = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
        return str.matches(expression);
    }
    public static boolean isGoodMobileNumber(String str) {
        String expression = "^[6-9]\\d{9}$";
        return str.matches(expression);
    }


    private void isUserPresent()
    {

        showProgressDialog(getString(R.string.username_verify));

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference yourCollRef = rootRef.collection("users");
        Query query = yourCollRef.whereEqualTo("username", username);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    if(task.getResult().size()==0) {
                        createAccount(email,password);
                    } else {
                        Toast.makeText(getContext(), "Username Already in Use",
                                Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());

                }

            }
        });

        hideProgressDialog();
    }

}
