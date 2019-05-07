package com.example.instagramclone.MainActivityFragments;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import com.example.instagramclone.Adapters.HomeRecyclerAdapter;
import com.example.instagramclone.LoginActivity;
import com.example.instagramclone.Modals.Post;
import com.example.instagramclone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private FirebaseAuth mAuth;
    private ProgressDialog progress;
    private String username;

    @ServerTimestamp
    private Date time;

    private List<Post> itemLists = new ArrayList<>();
    private HomeRecyclerAdapter adapter;

    private FirebaseFirestore db;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mAuth = FirebaseAuth.getInstance();

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.homepageRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new HomeRecyclerAdapter(itemLists);
        recyclerView.setAdapter(adapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        itemLists.clear();
        adapter.notifyDataSetChanged();
        getDocumentsFromCollection();

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        username = getCurrentUserUsername();

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.home_fragment_name);
        toolbar.inflateMenu(R.menu.top_menu);
        toolbar.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.reload:
                                itemLists.clear();
                                adapter.notifyDataSetChanged();
                                getDocumentsFromCollection();
                                return true;
                            case R.id.logout:
                                mAuth.signOut();
                                getActivity().finish();
                                startActivity(new Intent (getContext(), LoginActivity.class));
                        }
                        return false;
                    }
                });

        FloatingActionButton fab = view.findViewById(R.id.addPost);
        
        fab.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.post_text);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.setTitle("Tell us what's on your mind.");


                final EditText eTxt = dialog.findViewById(R.id.postText);
                final Button postButton = dialog.findViewById(R.id.postButton);
                postButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        createPost(eTxt.getText().toString());
                    }
                });

                dialog.show();



            }
        });
    }

    private String getCurrentUserUsername() {
        db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String uid = document.get("uid").toString();
                                if(uid.equals(mAuth.getCurrentUser().getUid()))
                                    username = document.get("username").toString();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return username;
    }

    private void getDocumentsFromCollection() {

        db = FirebaseFirestore.getInstance();

        db.collection("posts")
                .orderBy("time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String by = document.get("by").toString();
                                String text = document.get("text").toString();
                                String time = document.get("time").toString();

                                /*Date date = (Date) document.get("time");
                                String time = date.toString() + " at " + date.getTime();*/


                                Post post = new Post(by,text,"Post Time : " + time);
                                Log.d(TAG, "data : "+by+"\n"+text+"\n"+time+"\n");
                                /*Toast.makeText(getContext(),post.getUser()+" "+post.getText()+" "+
                                post.getTime(),Toast.LENGTH_SHORT).show();*/
                                itemLists.add(post);
                                adapter.notifyDataSetChanged();

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void createPost(String posttext) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String date_time = (dateFormat.format(date));


        Map<String, Object> post = new HashMap<>();
        post.put("by", "By : " + username);
        post.put("text", "Post : " + posttext);
        post.put("time", date_time);

        // Add a new document with a generated ID
        FirebaseFirestore db;

        db = FirebaseFirestore.getInstance();

        db.collection("posts")
                .add(post)
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

}
