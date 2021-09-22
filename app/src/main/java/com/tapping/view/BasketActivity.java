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
import com.tapping.databinding.ActivityBasketBinding;
import com.tapping.dto.ItemDto;
import com.tapping.dto.UserDto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import retrofit2.http.Url;

public class BasketActivity extends AppCompatActivity {
    ActivityBasketBinding binding;
    int totalprice;
    ArrayList<ItemDto> items;
    SharedPreferences sharedPreferences;
    String id;
    String bank;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("pref",MODE_PRIVATE);
        id= sharedPreferences.getString("id","");
        try {
            bank = new JSONObject(sharedPreferences.getString("user","")).getString("account");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        totalprice=0;
        binding = ActivityBasketBinding.inflate(getLayoutInflater());
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
                String urlAddr = "http://34.212.36.216:8080/basket.jsp";
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
                    totalprice+=item.getPrice()*item.getCount();
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
            BasketAdapter adapter = new BasketAdapter(BasketActivity.this,items);
            binding.listview1.setAdapter(adapter);
            binding.item.setText(totalprice+"원");
            binding.cash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new buyThread().execute();
                }
            });
            //리스트 어댑터 연결
        }
    }
    class buyThread extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                String urlAddr = "http://34.212.36.216:8080/buyitem.jsp";
                urlAddr+="?id="+id;
                URL url = new URL(urlAddr);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                if (conn != null) {
                    conn.setConnectTimeout(20000);
                    conn.setUseCaches(false);
                    conn.getResponseCode();
                }

            } catch (Exception e) {
                // TODO: handle exception
                Log.e("에러 ", e.getMessage());
            }
            return "";
        }



        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new payThread().execute();
        }
    }
    class payThread extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("asdsadasd",s);
            if(s.equals("200")) {
                Intent intent = new Intent(BasketActivity.this, CompleteActivity.class);
                startActivity(intent);
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            URL url = null;
            try {
                url = new URL("https://openapi.wooribank.com:444/oai/wb/v1/trans/getWooriAcctToOtherAcct");
                HttpURLConnection conn = null;
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("appKey", "l7xxzrMc0ROpeJjpWQEnu0rg8a1uSPliltSU");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                conn.setDoOutput(true);

                /*String parameters = "{\n" +
                        "  \"dataHeader\": {\n" +
                        "    \"UTZPE_CNCT_IPAD\": \"\",\n" +
                        "    \"UTZPE_CNCT_MCHR_UNQ_ID\": \"\",\n" +
                        "    \"UTZPE_CNCT_TEL_NO_TXT\": \"\",\n" +
                        "    \"UTZPE_CNCT_MCHR_IDF_SRNO\": \"\",\n" +
                        "    \"UTZ_MCHR_OS_DSCD\": \"\",\n" +
                        "    \"UTZ_MCHR_OS_VER_NM\": \"\",\n" +
                        "    \"UTZ_MCHR_MDL_NM\": \"\",\n" +
                        "    \"UTZ_MCHR_APP_VER_NM\": \"\"\n" +
                        "  },\n" +
                        "  \"dataBody\": {\n" +
                        "    \"WDR_ACNO\": \""+bank+"\",\n" +
                        "    \"TRN_AM\": \""+totalprice+"\",\n" +
                        "    \"RCV_BKCD\": \"020\",\n" +
                        "    \"RCV_ACNO\": \"1002987654321\",\n" +
                        "    \"PTN_PBOK_PRNG_TXT\": \"보너스\"\n" +
                        "  }\n" +
                        "}";
                 */
                String parameters = "{\n" +
                        "  \"dataHeader\": {\n" +
                        "    \"UTZPE_CNCT_IPAD\": \"\",\n" +
                        "    \"UTZPE_CNCT_MCHR_UNQ_ID\": \"\",\n" +
                        "    \"UTZPE_CNCT_TEL_NO_TXT\": \"\",\n" +
                        "    \"UTZPE_CNCT_MCHR_IDF_SRNO\": \"\",\n" +
                        "    \"UTZ_MCHR_OS_DSCD\": \"\",\n" +
                        "    \"UTZ_MCHR_OS_VER_NM\": \"\",\n" +
                        "    \"UTZ_MCHR_MDL_NM\": \"\",\n" +
                        "    \"UTZ_MCHR_APP_VER_NM\": \"\"\n" +
                        "  },\n" +
                        "  \"dataBody\": {\n" +
                        "    \"WDR_ACNO\": \"1002123456789\",\n" +
                        "    \"TRN_AM\": \"500000\",\n" +
                        "    \"RCV_BKCD\": \"081\",\n" +
                        "    \"RCV_ACNO\": \"110123456789\",\n" +
                        "    \"PTN_PBOK_PRNG_TXT\": \"월급여\"\n" +
                        "  }\n" +
                        "}";
                DataOutputStream wr = null;

                wr = new DataOutputStream(conn.getOutputStream());

                wr.writeBytes(parameters);

                wr.flush();

                wr.close();
                return conn.getResponseCode()+"";

            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }
    }
}