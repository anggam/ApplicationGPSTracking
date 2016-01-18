package com.example.runrun.applicationgpstracking.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.runrun.applicationgpstracking.R;
import com.example.runrun.applicationgpstracking.model.User;

import java.util.ArrayList;

/**
 * Created by Wahid Hasan Ariyanto
 * on 1/8/16.
 */
public class FriendsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<User> contents;

    public FriendsAdapter(Context context) {
        this.context = context;
        contents = new ArrayList<>();
    }

    public void setContent(ArrayList<User> newContent) {
        contents = newContent;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return contents.size();
    }

    @Override
    public User getItem(int i) {
        return contents.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_friend_list_item, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.nameTV = (TextView) view.findViewById(R.id.friend_name_tv);
            viewHolder.informationTV = (TextView) view.findViewById(R.id.information_tv);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.nameTV.setText(contents.get(i).getName());

        return view;
    }

    private class ViewHolder {
        TextView nameTV;
        TextView informationTV;
    }
}
