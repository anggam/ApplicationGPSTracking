package com.example.runrun.applicationgpstracking;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

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

public class MenuActivity extends FragmentActivity implements OnMapReadyCallback {
    private Toolbar actionBarToolbar;
    private FriendsAdapter friendsAdapter;
    private ListView friendsLV;
    private ProgressBar friendsPB;
    private DrawerLayout drawerLayout;
    private View drawerMenu;

    private View friendPage;
    private View mapPage;

    private GoogleMap mMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        initViews();
        initActionbar();
        initListView();
        getFriends();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);
    }

    private void initViews() {
        actionBarToolbar = (Toolbar) findViewById(R.id.action_bar);
        friendPage = findViewById(R.id.friend_page);
        mapPage = findViewById(R.id.map_page);
        friendsLV = (ListView) findViewById(R.id.friend_lv);
        friendsPB = (ProgressBar) findViewById(R.id.friend_list_pb);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerMenu = findViewById(R.id.left_drawer);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMaps = googleMap;
        // Add a marker in Indoneisa and move the camera
        LatLng indonesia = new LatLng(-6.914744, 107.609810);
        mMaps.addMarker(new MarkerOptions().position(indonesia).title("Marker in Indonesia"));
        mMaps.moveCamera(CameraUpdateFactory.newLatLng(indonesia));
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
