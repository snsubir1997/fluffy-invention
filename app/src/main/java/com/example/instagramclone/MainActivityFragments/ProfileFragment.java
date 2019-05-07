package com.example.instagramclone.MainActivityFragments;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.instagramclone.LoginActivity;
import com.example.instagramclone.MainActivity;
import com.example.instagramclone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private EditText e1,e2,e3;
    private FirebaseUser user;
    private ProgressDialog progress;
    private String old_email_id;
    private EditText oldEmail,pwd;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        user = FirebaseAuth.getInstance().getCurrentUser();
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.profile_fragment_name);
        toolbar.inflateMenu(R.menu.top_menu);
        toolbar.getMenu().removeItem(R.id.reload);
        toolbar.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.logout:
                                FirebaseAuth.getInstance().signOut();
                                getActivity().finish();
                                startActivity(new Intent (getContext(), LoginActivity.class));
                        }
                        return false;
                    }
                });

        e1 = view.findViewById(R.id.nameChng);
        e2 = view.findViewById(R.id.email_idChng);
        e3 = view.findViewById(R.id.passwordChng);

        getUserDetails();

        Button b1 = view.findViewById(R.id.submitChngButton);



        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(e1.getText().toString().equals("") ||
                    e2.getText().toString().equals("") ||
                    e3.getText().toString().equals(""))
                    Toast.makeText(getContext(),"Enter Proper User Details",Toast.LENGTH_SHORT)
                        .show();


                if(!e2.getText().toString().equals(old_email_id))
                    changeEmail();


                //showProgressDialog("Changing Things");
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(e1.getText().toString())
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(),"User profile updated.",Toast.LENGTH_SHORT)
                                            .show();
                                    Log.d(TAG, "User profile updated.");
                                }
                            }
                        });
            }
        });

        //hideProgressDialog();
    }

    private void getUserDetails() {
        e1.setText(user.getDisplayName());
        e2.setText(user.getEmail());
        e3.setText("**********");

        old_email_id = user.getEmail();
    }


    private void changeEmail()
    {
            final Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.re_auth);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog.setTitle("Re-Authenticate to change email");

            oldEmail= dialog.findViewById(R.id.reAuthEmail);
            pwd = dialog.findViewById(R.id.reAuthPassword);
            final Button postButton = dialog.findViewById(R.id.reAuthButton);
            postButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    AuthCredential credential = EmailAuthProvider
                            .getCredential(oldEmail.getText().toString(),pwd.getText().toString());

                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d(TAG, "User re-authenticated.");
                                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                        FirebaseAuth.getInstance().getCurrentUser().updateEmail(e2.getText().toString())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification()
                                                                    .addOnCompleteListener(Objects.requireNonNull(getActivity()), new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                Toast.makeText(getContext(),
                                                                                        "Verification email sent to " + FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                                                                                        Toast.LENGTH_SHORT).show();
                                                                                FirebaseAuth.getInstance().signOut();
                                                                                getActivity().finish();
                                                                                startActivity(new Intent(getContext(),LoginActivity.class));

                                                                            } else {
                                                                                Log.e(TAG, "sendEmailVerification", task.getException());
                                                                                Toast.makeText(getContext(),
                                                                                        "Failed to send verification email.",
                                                                                        Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                    });
                                                            Log.d(TAG, "User email address updated.");
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });
                }
            });

            dialog.show();


    }




}
