package com.example.runrun.applicationgpstracking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.widget.EditText;
import android.widget.TextView;

import com.example.runrun.applicationgpstracking.helpers.HttpHelper;
import com.example.runrun.applicationgpstracking.model.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {
    private TextView emailTV;
    private TextView usernameTV;
    private TextView ageTV;
    private TextView phoneTV;
    private TextView addressTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        emailTV = (TextView) findViewById(R.id.tvEmail);
        usernameTV = (TextView) findViewById(R.id.tvUsername);
        ageTV = (TextView) findViewById(R.id.tvAge);
        phoneTV = (TextView) findViewById(R.id.tvPhone);
        addressTV = (TextView) findViewById(R.id.tvAddress);

        Intent intent = getIntent();
        if(intent != null) {
            int userId = intent.getIntExtra("user_id", 0);
            getUserDetail(userId);
        }
    }
    private void setText() {
        emailTV.setText("tvEmail");
        usernameTV.setText("tvUsername");
        ageTV.setText("tvAge");
        phoneTV.setText("tvPhone");
        addressTV.setText("tvAddress");

    }

    private void getUserDetail(int userId) {
        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        HttpHelper.get("get_user_detail.php", params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String userEmail = response.getString("user_email");
                    emailTV.setText(userEmail);
                    String userName = response.getString("user_name");
                    usernameTV.setText(userName);
                    String userAge = response.getString("user_age");
                    ageTV.setText(userAge);
                    String userPhone = response.getString("user_phone");
                    phoneTV.setText(userPhone);
                    String userAddress = response.getString("user_address");
                    addressTV.setText(userAddress);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
