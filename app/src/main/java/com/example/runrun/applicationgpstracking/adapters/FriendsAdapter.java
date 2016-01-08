package com.example.runrun.applicationgpstracking.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.runrun.applicationgpstracking.R;

/**
 * Created by Wahid Hasan Ariyanto
 * on 1/8/16.
 */
public class FriendsAdapter extends BaseAdapter {
    private Context context;

    public FriendsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.layout_friend_list_item, viewGroup, false);
        return view;
    }
}
