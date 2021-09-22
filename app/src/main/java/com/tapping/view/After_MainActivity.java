package com.tapping.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

import com.google.gson.JsonObject;
import com.tapping.R;
import com.tapping.alarm.AlarmReceiver;
import com.tapping.databinding.ActivityAferMainBinding;
import com.tapping.databinding.ActivityLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class After_MainActivity extends AppCompatActivity {
    ActivityAferMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAferMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        try {
            String name = new JSONObject(getSharedPreferences("pref",MODE_PRIVATE).getString("user","")).getString("name");
            binding.maintext.setText(name+"님\n\n 어서오세요.");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        binding.alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(After_MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String state = "AM";
                        // 선택한 시간이 12를 넘을경우 "PM"으로 변경 및 -12시간하여 출력 (ex : PM 6시 30분)
                        if (selectedHour > 12) {
                            selectedHour -= 12;
                            state = "PM";
                        }
                        Calendar calender = Calendar.getInstance();
                        calender.set(Calendar.HOUR_OF_DAY,hour);
                        calender.set(Calendar.MINUTE,minute);
                        calender.set(Calendar.SECOND, 0);
                        calender.set(Calendar.MILLISECOND, 0);
                        Intent alarmIntent = new Intent(After_MainActivity.this, AlarmReceiver.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(After_MainActivity.this, 0, alarmIntent, 0);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(),  AlarmManager.INTERVAL_DAY, pendingIntent);
                        // EditText에 출력할 형식 지정
                    }
                }, hour, minute, false); // true의 경우 24시간 형식의 TimePicker 출현
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        binding.itemsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(After_MainActivity.this,ItemsettingActivity.class);
                startActivity(intent);
            }
        });
        binding.modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(After_MainActivity.this,ModifiActivity2.class);
                startActivity(intent);
            }
        });
        binding.basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(After_MainActivity.this,BasketActivity.class);
                startActivity(intent);
            }
        });
        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(After_MainActivity.this,PaymentActivity.class);
                startActivity(intent);
            }
        });

    }
}