package com.example.runrun.applicationgpstracking;

<<<<<<< HEAD
import android.content.Intent;
=======
import android.os.Build;
>>>>>>> 197a24e8f4228080e243bc7ce96f19dcbf651b28
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.runrun.applicationgpstracking.adapters.FriendsAdapter;
import com.example.runrun.applicationgpstracking.helpers.HttpHelper;
import com.example.runrun.applicationgpstracking.model.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MenuActivity extends AppCompatActivity {
    private Toolbar actionBarToolbar;
    private FriendsAdapter friendsAdapter;
    private ListView friendsLV;
    private ProgressBar friendsPB;
    private DrawerLayout drawerLayout;
    private View drawerMenu;
    LinearLayout contentFrame;

    private View friendPage;
    private View mapPage;

<<<<<<< HEAD
=======
    private GoogleMap mMaps;
    private float lastTranslate = 0.0f;

>>>>>>> 197a24e8f4228080e243bc7ce96f19dcbf651b28
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        initViews();
        initActionbar();
        initListView();
        getFriends();

        TextView textView = (TextView) findViewById(R.id.tvMaps);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                Intent i = new Intent(MenuActivity.this, MapsActivity.class);
                i.putExtra("pesan", "Form Activity Login");
                startActivity(i);
            }
        });

    }

    private void initViews() {
        actionBarToolbar = (Toolbar) findViewById(R.id.action_bar);
        friendPage = findViewById(R.id.friend_page);
        mapPage = findViewById(R.id.map_page);
        friendsLV = (ListView) findViewById(R.id.friend_lv);
        friendsPB = (ProgressBar) findViewById(R.id.friend_list_pb);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerMenu = findViewById(R.id.left_drawer);
        contentFrame = (LinearLayout) findViewById(R.id.content_frame);
    }

    private void getFriends() {
        HttpHelper.get("db_gpstracking/get_friend.php", null, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                friendsPB.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                friendsPB.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                ArrayList<User> newUsers = new ArrayList<User>();
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        User user = new User();
                        user.setName(jsonObject.getString("user_name"));
                        newUsers.add(user);

                    }
                    friendsAdapter.setContent(newUsers);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initListView() {
        friendsAdapter = new FriendsAdapter(this);
        friendsLV.setAdapter(friendsAdapter);
    }

    private void initActionbar() {
        setSupportActionBar(actionBarToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, actionBarToolbar, R.string.drawer_opened, R.string.drawer_closed){
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                //we translate the content along with the navigation menu sliding
                //ref : http://stackoverflow.com/questions/23783496/how-to-slide-the-actionbar-along-with-the-navigationdrawer
                float moveFactor = drawerView.getWidth() * slideOffset;

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                    contentFrame.setTranslationX(moveFactor);
                } else {
                    TranslateAnimation translateAnimation = new TranslateAnimation(lastTranslate, moveFactor, 0.0f, 0.0f);
                    translateAnimation.setDuration(0);
                    translateAnimation.setFillAfter(true);
                    contentFrame.startAnimation(translateAnimation);

                    lastTranslate = moveFactor;
                }
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    public void openFriendPage(View view) {
        friendPage.setVisibility(View.VISIBLE);
        mapPage.setVisibility(View.GONE);
        drawerLayout.closeDrawer(drawerMenu);
    }

    public void openMap(View view) {
        friendPage.setVisibility(View.GONE);
        mapPage.setVisibility(View.VISIBLE);
        drawerLayout.closeDrawer(drawerMenu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuactivity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
