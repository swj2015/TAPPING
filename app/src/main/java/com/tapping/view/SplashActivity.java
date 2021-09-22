package com.tapping.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.tapping.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(), 3000); // 1초 후에 hd handler 실행  3000ms = 3초

    }
    private class splashhandler implements Runnable{
        public void run(){
            if(getSharedPreferences("pref",MODE_PRIVATE).contains("id")) {
                startActivity(new Intent(getApplication(), After_MainActivity.class)); //로딩이 끝난 후, ChoiceFunction 이동
                SplashActivity.this.finish(); // 로딩페이지 Activity stack에서 제거
            }else {
                startActivity(new Intent(getApplication(), MainActivity.class)); //로딩이 끝난 후, ChoiceFunction 이동
                SplashActivity.this.finish(); // 로딩페이지 Activity stack에서 제거
            }
        }
    }

}