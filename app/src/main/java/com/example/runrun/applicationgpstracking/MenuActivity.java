package com.example.runrun.applicationgpstracking;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.runrun.applicationgpstracking.adapters.FriendsAdapter;

public class MenuActivity extends AppCompatActivity {
    private Toolbar actionBarToolbar;
    private FriendsAdapter friendsAdapter;
    private ListView friendsLV;
    private ProgressBar friendsPB;

    private View friendPage;
    private View mapPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initViews();
        initActionbar();
        initListView();
    }

    private void initViews() {
        actionBarToolbar = (Toolbar) findViewById(R.id.action_bar);
        friendPage = findViewById(R.id.friend_page);
        mapPage = findViewById(R.id.map_page);
        friendsLV = (ListView) findViewById(R.id.friend_lv);
        friendsPB = (ProgressBar) findViewById(R.id.friend_list_pb);
        friendsLV.setEmptyView(friendsPB);
    }

    private void initListView() {
        friendsAdapter = new FriendsAdapter(this);
        friendsLV.setAdapter(friendsAdapter);
    }

    private void initActionbar() {
        setSupportActionBar(actionBarToolbar);
    }

    public void openFriendPage(View view) {
        friendPage.setVisibility(View.VISIBLE);
        mapPage.setVisibility(View.GONE);
    }

    public void openMap(View view) {
        friendPage.setVisibility(View.GONE);
        mapPage.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuactivity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
