package com.tapping.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.tapping.R;
import com.tapping.databinding.ActivityTappingBinding;
import com.tapping.dto.ItemDto;
import com.tapping.dto.UserDto;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

public class TappingActivity extends AppCompatActivity {
    ActivityTappingBinding binding;
    String no;
    String id;
    int count;
    int price;
    int totalprice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityTappingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        id=getSharedPreferences("pref",MODE_PRIVATE).getString("id","");
        Intent intent = getIntent();
        Uri uri = intent.getData();
        no = uri.getQueryParameter("no");
        super.onCreate(savedInstanceState);
        new ServiceThread().execute();
        setContentView(view);
    }
    class ServiceThread extends AsyncTask<Void, Void, String> {
        StringBuilder Buffer = new StringBuilder();
        Bitmap bm;
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
                url = new
                        URL("https://wooritapping.s3-us-west-2.amazonaws.com/"+no+".png");
                URLConnection con = url.openConnection();
                con.connect();
                BufferedInputStream bis = new
                        BufferedInputStream(con.getInputStream());
                bm = BitmapFactory.decodeStream(bis);
                bis.close();

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
                item.setId(id);
                item.setName(jsonObject.getString("name"));
                item.setPrice(jsonObject.getInt("price"));
                count=1;
                price = item.getPrice();
                totalprice = count*price;
                item.setImg(jsonObject.getString("img"));
                binding.item.setText(item.getName());
                binding.price.setText(price+"원");
                binding.number.setText(count+"");
                binding.totalprice.setText(totalprice+"원");
                binding.itemimage.setImageBitmap(bm);
                binding.plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        count++;
                        binding.number.setText(count+"");
                        totalprice+=price;
                        binding.totalprice.setText(totalprice+"원");
                    }
                });
                binding.subtract.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(count>1)
                        {
                            count--;
                            binding.number.setText(count+"");
                            totalprice-=price;
                            binding.totalprice.setText(totalprice+"원");
                        }
                    }
                });
                binding.extra.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new ItemThread().execute();
                    }
                });
                binding.cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        moveTaskToBack(true); // 태스크를 백그라운드로 이동
                        finishAndRemoveTask(); // 액티비티 종료 + 태스크 리스트에서 지우기
                        System.exit(0);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    class ItemThread extends AsyncTask<Void, Void, String>{

        Document doc =null;
        String url = "http://34.212.36.216:8080/login.jsp";
        StringBuilder Buffer = new StringBuilder();

        @Override
        protected String doInBackground(Void... voids) {
            String get_json = "";
            try {
                String urlAddr = "http://34.212.36.216:8080/addbasket.jsp";
                urlAddr+="?id="+id+"&no="+no+"&count="+count;
                URL url = new URL(urlAddr);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                if (conn != null) {
                    conn.setConnectTimeout(20000);
                    conn.setUseCaches(false);
                    conn.getResponseCode();
                    conn.disconnect();

                }
                get_json = Buffer.toString();
                Log.i("zzzzz",get_json);

            } catch (Exception e) {
                // TODO: handle exception
                Log.e("에러 ", e.getMessage());
            }
            return get_json;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            AlertDialog.Builder oDialog = new AlertDialog.Builder(TappingActivity.this,
                    android.R.style.Theme_DeviceDefault_Light_Dialog);

            oDialog.setMessage("장바구니에 추가되었습니다.")
                    .setTitle("TAPPING")
                    .setNeutralButton("종료", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            moveTaskToBack(true); // 태스크를 백그라운드로 이동
                            finishAndRemoveTask(); // 액티비티 종료 + 태스크 리스트에서 지우기
                            System.exit(0);
                        }
                    })
                    .setCancelable(false) // 백버튼으로 팝업창이 닫히지 않도록 한다.


                    .show();




        }
    }

}