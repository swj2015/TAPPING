package com.tapping.view;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.tapping.R;
import com.tapping.databinding.ActivityCompleteBinding;

public class CompleteActivity extends AppCompatActivity {
    ActivityCompleteBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityCompleteBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        View view = binding.getRoot();
        setContentView(view);
        binding.complete22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveTaskToBack(true); // 태스크를 백그라운드로 이동
                finishAndRemoveTask(); // 액티비티 종료 + 태스크 리스트에서 지우기
                System.exit(0);
            }
        });
    }
}