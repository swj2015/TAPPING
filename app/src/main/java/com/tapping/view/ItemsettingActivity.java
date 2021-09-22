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
import com.tapping.databinding.ActivityItemsettingBinding;
import com.tapping.dto.ItemDto;
import com.tapping.dto.UserDto;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ItemsettingActivity extends AppCompatActivity {
    ActivityItemsettingBinding binding;
    int no=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemsettingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


    }
    class ServiceThread extends AsyncTask<Void, Void, String> {
        StringBuilder Buffer = new StringBuilder();

        @Override
        protected String doInBackground(Void... voids) {
            String get_json = "";
            try {
                String urlAddr = "http://34.212.36.216:8080/item.jsp";
                urlAddr+="?no="+no;
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

            } catch (Exception e) {
                // TODO: handle exception
                Log.e("에러 ", e.getMessage());
            }
            return get_json;
        }



        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                ItemDto item = new ItemDto();
                item.setNo(no+"");

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}