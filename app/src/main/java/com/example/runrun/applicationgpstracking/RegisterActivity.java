package com.example.runrun.applicationgpstracking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.runrun.applicationgpstracking.helpers.HttpHelper;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class RegisterActivity extends AppCompatActivity {
    private Button registerBtn;
    private EditText emailET;
    private EditText usernameET;
    private EditText passwordET;
    private EditText phoneNumberET;
    private EditText addressET;
    private EditText ageET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerBtn = (Button) findViewById(R.id.bRegister);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        emailET = (EditText) findViewById(R.id.etEmail);
        usernameET = (EditText) findViewById(R.id.etUsername);
        passwordET = (EditText) findViewById(R.id.etPassword);
        phoneNumberET = (EditText) findViewById(R.id.etPhoneNumber);
        addressET = (EditText) findViewById(R.id.etAddress);
        ageET = (EditText) findViewById(R.id.etAge);
    }

    private void login() {
        RequestParams params = new RequestParams();
        params.put("user_email", emailET.getText().toString());
        params.put("user_name", usernameET.getText().toString());
        params.put("user_pass", passwordET.getText().toString());
        params.put("user_phone", phoneNumberET.getText().toString());
        params.put("user_address", addressET.getText().toString());
        params.put("user_age", ageET.getText().toString());
        HttpHelper.post("db_gpstracking/register.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("ERROR BRAY", responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getInt("code") == 1) {
                        //redirect ke halaman login
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        //tampilkan pesan error
                        Toast.makeText(RegisterActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {

                }
            }
        });
    }
}
