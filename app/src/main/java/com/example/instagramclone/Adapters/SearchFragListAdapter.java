package com.example.instagramclone.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.instagramclone.Modals.SearchModal;
import com.example.instagramclone.R;

import java.util.ArrayList;

public class SearchFragListAdapter extends BaseAdapter {

    Context context;
    ArrayList<SearchModal> arrayList;

    public SearchFragListAdapter(Context context, ArrayList<SearchModal> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.search_row,null);
        }

        TextView tv1 = convertView.findViewById(R.id.userNameTextView);
        TextView tv2 = convertView.findViewById(R.id.follow_following);

        SearchModal currentItem = (SearchModal) getItem(position);

        if(!currentItem.isPresent()) {
            tv2.setText("Follow");
            tv2.setTextColor(Color.parseColor("#33cc33"));
        } else {
            tv2.setText("Following");
            tv2.setTextColor(Color.parseColor("#ff0000"));
        }

        tv2.setVisibility(View.VISIBLE);
        tv1.setText(currentItem.getUsername());


        return convertView;
    }
}
