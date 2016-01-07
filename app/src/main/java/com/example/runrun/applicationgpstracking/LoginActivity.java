package com.example.runrun.applicationgpstracking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.runrun.applicationgpstracking.helpers.HttpHelper;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {
    private Button loginBtn;
    private TextView registerTV;
    private EditText usernameET;
    private EditText passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkIsAlreadyLogin();
        setContentView(R.layout.activity_login);

        loginBtn = (Button) findViewById(R.id.bLogin);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu();
            }
        });

        TextView textView = (TextView) findViewById(R.id.tvRegister);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                i.putExtra("pesan", "Form Activity Login");
                startActivity(i);
            }
        });

        usernameET = (EditText) findViewById(R.id.etUsername);
        passwordET = (EditText) findViewById(R.id.etPassword);
    }

    /**
     * untuk cek apakah user sudah login
     */
    private void checkIsAlreadyLogin() {
        if(GPSTrackingApplication.getInstance().isLoggedIn()) {
            openMenuActivity();
        }
    }

    /**
     * redirect ke halaman MenuActivity
     */
    private void openMenuActivity() {
        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    public void menu() {
        RequestParams params = new RequestParams();
        params.put("user_name", usernameET.getText().toString());
        params.put("user_pass", passwordET.getText().toString());
        HttpHelper.post("db_gpstracking/login.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int StatusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("EROR BRAY", responseString);
            }

            @Override
            public void onSuccess(int StatusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getInt("code") == 1) {
                        //redirect ke halaman menu
                        int myUserId = response.getInt("user_id");
                        GPSTrackingApplication.getInstance().saveMyUserId(myUserId);
                        openMenuActivity();
                    } else {
                        //tampilkan pesan error
                        Toast.makeText(LoginActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                }
            }
        });
    }
}
