package com.tapping.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.tapping.databinding.ActivityLoginBinding;
import com.tapping.dto.UserDto;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("zzzzzzzz","123123123123123");
                new ServiceThread().execute();
            }
        });

    }

    class ServiceThread extends AsyncTask<Void, Void, String>{

        Document doc =null;
        String url = "http://34.212.36.216:8080/login.jsp";
        StringBuilder Buffer = new StringBuilder();

        @Override
        protected String doInBackground(Void... voids) {
            String get_json = "";
            try {
                String urlAddr = "http://34.212.36.216:8080/login.jsp";
                urlAddr+="?id="+binding.idtext.getText()+"&passwd="+binding.pwtext.getText();
                URL url = new URL(urlAddr);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                if (conn != null) {
                    conn.setConnectTimeout(20000);
                    conn.setUseCaches(false);
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        // 서버에서 읽어오기 위한 스트림 객체
                        InputStreamReader isr = new InputStreamReader(
                                conn.getInputStream());
                        // 줄단위로 읽어오기 위해 BufferReader로 감싼다.
                        BufferedReader br = new BufferedReader(isr);
                        // 반복문 돌면서읽어오기
                        while (true) {
                            String line = br.readLine();
                            if (line == null) {
                                break;
                            }
                            Buffer.append(line);
                        }
                        br.close();
                        conn.disconnect();
                    }
                }
                get_json = Buffer.toString();
                Log.i("zzzzz",get_json);

            } catch (Exception e) {
                // TODO: handle exception
                Log.e("에러 ", e.getMessage());
            }
            return get_json;
        }



        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String id ="";
            Gson gson = new Gson();
            try {
                UserDto user = new UserDto();
                JSONObject jsonObject = new JSONObject(result);
                id = jsonObject.getString("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(id.equals("")) {
                binding.idtext.setText("");
                binding.pwtext.setText("");
            }else {
                SharedPreferences pref = getSharedPreferences("pref",MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("user",result);
                editor.putString("id",binding.idtext.getText().toString());
                editor.commit();
                Intent intent= new Intent(LoginActivity.this,After_MainActivity.class);
                startActivity(intent);
            }

        }
    }
}






