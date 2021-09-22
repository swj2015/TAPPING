package com.tapping.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tapping.R;
import com.tapping.adapter.BasketAdapter;
import com.tapping.adapter.ReceiptAdapter;
import com.tapping.databinding.ActivityBasketBinding;
import com.tapping.databinding.ActivityPaymentBinding;
import com.tapping.dto.ItemDto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import retrofit2.http.Url;

public class PaymentActivity extends AppCompatActivity {
    ActivityPaymentBinding binding;
    ArrayList<ItemDto> items;
    SharedPreferences sharedPreferences;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("pref",MODE_PRIVATE);
        id= sharedPreferences.getString("id","");
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        items = new ArrayList<>();
        View view = binding.getRoot();
        setContentView(view);
        new ServiceThread().execute();
    }
    class ServiceThread extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            StringBuilder Buffer = new StringBuilder();
            String get_json = "";
            try {
                String urlAddr = "http://34.212.36.216:8080/receipt.jsp";
                urlAddr+="?id="+id;
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
                JSONArray jsonArray = new JSONArray(get_json);
                for(int i=0;i<jsonArray.length();i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    ItemDto item = new ItemDto();
                    String no = jsonObject.getString("no");
                    item.setNo(jsonObject.getString("no"));
                    item.setId(id);
                    item.setName(jsonObject.getString("name"));
                    item.setPrice(jsonObject.getInt("price"));
                    item.setCount(jsonObject.getInt("count"));
                    item.setImg(jsonObject.getString("img"));
                    url = new
                            URL("https://wooritapping.s3-us-west-2.amazonaws.com/thumbnail/"+no+".png");
                    URLConnection con = url.openConnection();
                    con.connect();
                    BufferedInputStream bis = new
                            BufferedInputStream(con.getInputStream());
                    Bitmap bm = BitmapFactory.decodeStream(bis);
                    bis.close();
                    item.setBm(bm);
                    items.add(item);
                }


            } catch (Exception e) {
                // TODO: handle exception
                Log.e("에러 ", e.getMessage());
            }
            Log.e("개수새구ㅐㅜ새",items.size()+"");
            return get_json;
        }



        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ReceiptAdapter adapter = new ReceiptAdapter(PaymentActivity.this,items);
            binding.listview1.setAdapter(adapter);
            //리스트 어댑터 연결
        }
    }
}