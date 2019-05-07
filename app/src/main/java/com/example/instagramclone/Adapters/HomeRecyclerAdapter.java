package com.example.instagramclone.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramclone.Modals.Post;
import com.example.instagramclone.R;

import java.util.List;


public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.MyViewHolder> {

    private List<Post> itemLists;

    public HomeRecyclerAdapter(List<Post> itemLists) {
        this.itemLists = itemLists;
    }

    @NonNull
    @Override
    public HomeRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.post_card, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRecyclerAdapter.MyViewHolder myViewHolder, int i) {
        Post itemList = itemLists.get(i);
        myViewHolder.textViewBy.setText(itemList.getUser());
        myViewHolder.textViewText.setText(itemList.getText());
        myViewHolder.textViewTime.setText(itemList.getTime());
    }

    @Override
    public int getItemCount() {
        return itemLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewBy, textViewText, textViewTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewBy = itemView.findViewById(R.id.postBy);
            textViewText = itemView.findViewById(R.id.postText);
            textViewTime = itemView.findViewById(R.id.postTime);
        }
    }
}