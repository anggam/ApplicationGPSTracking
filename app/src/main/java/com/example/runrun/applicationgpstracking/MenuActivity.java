package com.example.runrun.applicationgpstracking;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.runrun.applicationgpstracking.adapters.FriendsAdapter;
import com.example.runrun.applicationgpstracking.helpers.HttpHelper;
import com.example.runrun.applicationgpstracking.model.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class MenuActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Toolbar actionBarToolbar;
    private FriendsAdapter friendsAdapter;
    private FriendsAdapter chooseFriendsAdapter;
    private ListView friendsLV;
    private ProgressBar friendsPB;
    private DrawerLayout drawerLayout;
    private View drawerMenu;
    LinearLayout contentFrame;

    private View friendPage;
    private View mapPage;
    private View mapsGoogleView;

    private GoogleMap mMaps;
    private float lastTranslate = 0.0f;

    private SupportMapFragment mapFragment;
    private GPSService gpsService;
    private ListView chooseFriendsLV;

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

        connectToService();
    }

    private void connectToService() {
        Intent intent = new Intent(this, GPSService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            GPSService.GPSServiceBinder binder = (GPSService.GPSServiceBinder) iBinder;
            gpsService = binder.getService();
            Toast.makeText(MenuActivity.this, "Service connected", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            gpsService = null;
        }
    };

    private void initViews() {
        actionBarToolbar = (Toolbar) findViewById(R.id.action_bar);
        friendPage = findViewById(R.id.friend_page);
        mapPage = findViewById(R.id.map_page);
        mapsGoogleView = findViewById(R.id.maps_google);
        friendsLV = (ListView) findViewById(R.id.friend_lv);
        chooseFriendsLV = (ListView) findViewById(R.id.choose_friend_lv);
        friendsPB = (ProgressBar) findViewById(R.id.friend_list_pb);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerMenu = findViewById(R.id.left_drawer);
        contentFrame = (LinearLayout) findViewById(R.id.content_frame);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps_google);
        mapFragment.getMapAsync(this);
    }

    private void getFriends() {
        HttpHelper.get("get_friend.php", null, new JsonHttpResponseHandler() {
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
                ArrayList<User> newUsers1 = new ArrayList<User>();
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);

                        User user = new User();
                        user.setName(jsonObject.getString("user_name"));
                        user.setUser_id(jsonObject.getInt("user_id"));
                        newUsers.add(user);

                        User user1 = new User();
                        user1.setName(jsonObject.getString("user_name"));
                        user.setUser_id(jsonObject.getInt("user_id"));
                        newUsers1.add(user1);
                    }
                    friendsAdapter.setContent(newUsers);
                    chooseFriendsAdapter.setContent(newUsers1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initListView() {
        friendsAdapter = new FriendsAdapter(this);
        chooseFriendsAdapter = new FriendsAdapter(this);
        friendsLV.setAdapter(friendsAdapter);
        friendsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MenuActivity.this, ProfileActivity.class);
                i.putExtra("user_id", friendsAdapter.getItem(position).getUser_id());
                startActivity(i);
            }
        });

        chooseFriendsLV.setAdapter(chooseFriendsAdapter);
        chooseFriendsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chooseFriendsLV.setVisibility(View.INVISIBLE);
                mapsGoogleView.setVisibility(View.VISIBLE);

                User user = chooseFriendsAdapter.getItem(position);
                createPath(user.getUser_id());
            }
        });
    }

    private void createPath(int userId) {
        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        HttpHelper.post("get_history.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    PolylineOptions options = new PolylineOptions();
                    MarkerOptions markerOptions = new MarkerOptions();

                    for (int index = 0; index < response.length(); index++) {
                        JSONObject object = response.getJSONObject(index);
                        double latitude = object.getDouble("latitude");
                        double longitude = object.getDouble("longitude");
                        LatLng latLng = new LatLng(latitude, longitude);
                        options.add(latLng);

                        if(index == response.length() - 1) {
                            markerOptions.position(latLng);
                            markerOptions.title("Last Position");
                        }
                    }

                    Polyline line = mMaps.addPolyline(options);
                    line.setWidth(5);
                    line.setColor(Color.RED);

                    if(markerOptions.getPosition() != null) {
                        mMaps.addMarker(markerOptions);
                        mMaps.moveCamera(CameraUpdateFactory.newLatLng(markerOptions.getPosition()));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

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
        chooseFriendsLV.setVisibility(View.VISIBLE);
        mapsGoogleView.setVisibility(View.INVISIBLE);
        drawerLayout.closeDrawer(drawerMenu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuactivity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        if(menuId == R.id.setting) {
            updateLocationToServer();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

    private void updateLocationToServer() {
        if(gpsService.canGetLocation()) {
            double longitude = gpsService.getLongitude();
            double latitude = gpsService.getLatitude();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(System.currentTimeMillis());
            String currentDate = simpleDateFormat.format(date);

            simpleDateFormat.applyPattern("HH:mm:ss");
            String currentTime = simpleDateFormat.format(date);

            int myUserId = GPSTrackingApplication.getInstance().getMyUserId();

            RequestParams params = new RequestParams();
            params.put("history_time", currentTime);
            params.put("history_date", currentDate);
            params.put("user_id", myUserId);
            params.put("latitude", latitude);
            params.put("longitude", longitude);
            HttpHelper.post("history.php", params, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        Toast.makeText(MenuActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        } else {
            Toast.makeText(MenuActivity.this, "Cannot get Location", Toast.LENGTH_LONG).show();
        }

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
    }
}
