package com.example.group3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    Button login, signUp;
    EditText editUsername, editPassword;
    String getUsernameText, getPasswordText;
    RequestQueue requestQueue;
    JSONObject jsonBody;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        requestQueue = Volley.newRequestQueue(this);

        login = findViewById(R.id.loginButton);
        signUp = findViewById(R.id.signUpButton);
        editUsername = findViewById(R.id.usernameEdit);
        editPassword = findViewById(R.id.passwordEdit);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUsernameText = editUsername.getText().toString();
                getPasswordText = editPassword.getText().toString();

                jsonBody = new JSONObject();
                try {
                    jsonBody.put("username", getUsernameText);
                    jsonBody.put("password", getPasswordText);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                login("http://34.203.33.211/user/login");

                if(TextUtils.isEmpty(getUsernameText)) {
                    editUsername.setError("This cannot be empty");
                }
                if(TextUtils.isEmpty(getPasswordText)) {
                    editPassword.setError("This cannot be empty");
                }
                /*Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    String userName = extras.getString("username");
                    String passWord = extras.getString("password");
                    if(getUsernameText.equals(userName) &&
                            getPasswordText.equals(passWord)) {
                        //todo
                        Toast.makeText(getApplicationContext(),
                                "Redirecting...", Toast.LENGTH_SHORT).show();
                    }
                    else if(getUsernameText.equals(userName) &&
                            !getPasswordText.equals(passWord)) {
                        editPassword.setError("Incorrect password");
                    }
                    else if(getPasswordText.equals(passWord) &&
                            !getUsernameText.equals(userName)) {
                        editUsername.setError("Incorrect username");
                    }*/
            }

        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUpIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(signUpIntent);
            }
        });
    }
    private void login(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("mytag", "" + response);  // printtaan vaa vastauksen
                        try {

                            token = response.getString("token"); // hakee tokenin APIsta

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        CheckBox stayLoggedIn = findViewById(R.id.stayLoggedIn);

                        if(stayLoggedIn.isChecked()) {
                            SaveSharedPreference.setUserName(loginActivity.this, getUsernameText); //tallentaa usernamen sharedpreferencee
                        }

                        SaveSharedPreference.setToken(loginActivity.this, token); //tallentaa tokenin sharedpreferencee

                        Intent profileIntent = new Intent(loginActivity.this, ExampleProfile.class); //joku activity täs
                        startActivity(profileIntent);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("mytag", "" + error);
                        Toast.makeText(getApplicationContext(),
                                "Password or username incorrect", Toast.LENGTH_SHORT).show();
                        //todo
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }
}