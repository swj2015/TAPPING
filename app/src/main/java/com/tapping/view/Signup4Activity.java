package com.tapping.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.tapping.R;
import com.tapping.databinding.ActivitySignup4Binding;
import com.tapping.dto.UserDto;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Signup4Activity extends AppCompatActivity {
    ActivitySignup4Binding binding;
    UserDto user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignup4Binding.inflate(getLayoutInflater());
        user=(UserDto)getIntent().getSerializableExtra("user");
        View view = binding.getRoot();
        binding.addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //주소 찾기
            }
        });
        binding.signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setAddress(binding.address1.getText().toString()+binding.address2.getText().toString());
                new ServiceThread().execute();
            }
        });
        setContentView(view);
    }
    class ServiceThread extends AsyncTask<Void, Void, String> {
        StringBuilder Buffer = new StringBuilder();

        @Override
        protected String doInBackground(Void... voids) {
            String get_json = "";
            try {
                String urlAddr = "http://34.212.36.216:8080/signup.jsp";
                urlAddr+="?id="+user.getId()+"&passwd="+user.getPasswd()
                            +"&name="+user.getName()+"&phone="+user.getPhone()
                        +"&address="+user.getAddress()+"&account="+user.getAccount();
                Log.i("zzzzzzz",urlAddr);
                URL url = new URL(urlAddr);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                if (conn != null) {
                    conn.setRequestMethod("POST");
                    conn.setConnectTimeout(20000);
                    conn.setUseCaches(false);
                    conn.getResponseCode();
                    conn.disconnect();
                }
            } catch (Exception e) {
                // TODO: handle exception
                Log.e("에러 ", e.getMessage());
            }
            return get_json;
        }
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
                Intent intent= new Intent(Signup4Activity.this,LoginActivity.class);
                startActivity(intent);

        }
    }
}