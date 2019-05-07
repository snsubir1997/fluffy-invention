package com.example.instagramclone.MainActivityFragments;


import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instagramclone.Adapters.SearchFragListAdapter;
import com.example.instagramclone.LoginActivity;
import com.example.instagramclone.Modals.SearchModal;
import com.example.instagramclone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ArrayList<SearchModal> searchList;

    private ArrayList<String> usersIfollow;
    private ArrayList<String> allUsersExceptMe;

    private ListView listView;

    public SearchFragment() {
        // Required empty public constructor
        allUsersExceptMe = new ArrayList<>();
        usersIfollow = new ArrayList<>();
        searchList = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        usersIfollow.clear();
        allUsersExceptMe.clear();
        searchList.clear();

        loadUsersIfollow();

        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = view.findViewById(R.id.listViewSearchFrag);


        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.search_fragment_name);
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
                                startActivity(new Intent(getContext(), LoginActivity.class));
                        }
                        return false;
                    }
                });

        Log.d(TAG,searchList.toString());


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position,
                                    long id) {

                TextView tv = v.findViewById(R.id.userNameTextView);
                String name = tv.getText().toString();

                Toast.makeText(getContext(),name,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAllUsersExceptMe() {
        allUsersExceptMe.clear();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String uid = document.get("uid").toString();

                                if(!uid.equals(mAuth.getCurrentUser().getUid())) {
                                    allUsersExceptMe.add(document.get("username").toString());
                                }
                                loadAdapter();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void loadUsersIfollow() {
        usersIfollow.clear();


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String uid = document.get("uid").toString();
                                if(uid.equals(mAuth.getCurrentUser().getUid())) {
                                    usersIfollow = (ArrayList<String>)document.get("follows");

                                }
                            }
                            loadAllUsersExceptMe();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void loadAdapter() {

        Log.d(TAG,"All users except me : " + allUsersExceptMe.toString());
        Log.d(TAG,"Users I follow : " + usersIfollow.toString());

        for(String i : allUsersExceptMe)
        {
            for(String j : usersIfollow)
            {
                if(i.equals(j))
                    searchList.add(new SearchModal(i,true));
                else
                    searchList.add(new SearchModal(i,false));
            }
        }

        SearchFragListAdapter adapter = new SearchFragListAdapter(getContext(),searchList);
        listView.setAdapter(adapter);
    }




}
