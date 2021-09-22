package com.tapping.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.tapping.databinding.ActivitySignup2Binding;
import com.tapping.dto.UserDto;



import java.io.DataOutputStream;
import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.URL;

public class Signup2Activity extends AppCompatActivity {
    boolean check;
    ActivitySignup2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignup2Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        check = false;
        binding.certbutton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  new sendmessage().execute();
              }
        });



        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new checkmessage().execute();
            }
        });
    }

    class sendmessage extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("postpost",s);
            if(s.equals("200")) {
                Log.i("asdsadasd","asdsadasdasd");
                binding.certt.setText("인증번호가 발송되었습니다.");
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            URL url = null;
            try {
                url = new URL("https://openapi.wooribank.com:444/oai/wb/v1/login/getCellCerti");
                HttpURLConnection conn = null;
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("appKey", "l7xxzrMc0ROpeJjpWQEnu0rg8a1uSPliltSU");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                conn.setDoOutput(true);

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
                        "    \"COMC_DIS\": \"1\",\n" +
                        "    \"HP_NO\": \""+binding.phonetext.getText().toString()+"\",\n" +
                        "    \"HP_CRTF_AGR_YN\": \"Y\",\n" +
                        "    \"FNM\": \""+binding.nametext.getText().toString()+"\",\n" +
                        "    \"RRNO_BFNB\": \"930216\",\n" +
                        "    \"ENCY_RRNO_LSNM\": \"1234567\"\n" +
                        "  }\n" +
                        "}";
                DataOutputStream wr = null;

                wr = new DataOutputStream(conn.getOutputStream());

                wr.writeBytes(parameters);

                wr.flush();

                wr.close();
                Log.i("zzzzzzz",conn.getResponseCode()+"");
                return conn.getResponseCode()+"";

            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }
    }
    class checkmessage extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("postpost",s);
            if(s.equals("200")) {
                UserDto user = (UserDto) getIntent().getSerializableExtra("user");
                user.setName(binding.nametext.getText().toString());
                user.setPhone(binding.phonetext.getText().toString());
                Intent intent = new Intent(Signup2Activity.this, Signup3Activity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            URL url = null;
            try {
                url = new URL("https://openapi.wooribank.com:444/oai/wb/v1/login/executeCellCerti");
                HttpURLConnection conn = null;
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("appKey", "l7xxzrMc0ROpeJjpWQEnu0rg8a1uSPliltSU");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                conn.setDoOutput(true);

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
                        "    \"RRNO_BFNB\": \"930216\",\n" +
                        "    \"ENCY_RRNO_LSNM\": \"1234567\",\n" +
                        "    \"ENCY_SMS_CRTF_NO\": \""+binding.certtext.getText().toString()+"\",\n" +
                        "    \"CRTF_UNQ_NO\": \"MG12345678901234567890\"\n" +
                        "  }\n" +
                        "}";
                DataOutputStream wr = null;

                wr = new DataOutputStream(conn.getOutputStream());

                wr.writeBytes(parameters);

                wr.flush();

                wr.close();
                Log.i("zzzzzzz",conn.getResponseCode()+"");
                return conn.getResponseCode()+"";

            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }
    }
}

