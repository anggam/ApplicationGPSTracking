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
    private TextView emailET;
    private TextView usernameET;
    private TextView ageET;
    private TextView phoneET;
    private TextView addressET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        emailET = (EditText) findViewById(R.id.etEmail);
        usernameET = (EditText) findViewById(R.id.etUsername);
        ageET = (EditText) findViewById(R.id.etAge);
        phoneET = (EditText) findViewById(R.id.etPhone);
        addressET = (EditText) findViewById(R.id.etAddress);

        Intent intent = getIntent();
        if(intent != null) {
            int userId = intent.getIntExtra("user_id", 0);
            getUserDetail(userId);
        }
    }

    private void getUserDetail(int userId) {
        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        HttpHelper.get("get_user_detail.php", params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String user_email = response.getString("user_email");
                    String user_name = response.getString("user_name");
                    String user_age = response.getString("user_age");
                    String user_phone = response.getString("user_phone");
                    String user_address = response.getString("user_address");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
