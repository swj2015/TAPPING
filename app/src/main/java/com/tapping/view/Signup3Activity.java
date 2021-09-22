package com.tapping.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tapping.R;
import com.tapping.databinding.ActivitySignup3Binding;
import com.tapping.dto.UserDto;

public class Signup3Activity extends AppCompatActivity {
    ActivitySignup3Binding binding;
    boolean check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        check=false;
        binding = ActivitySignup3Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.bankbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check=true;
            }
        });
        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check){
                    UserDto user = (UserDto)getIntent().getSerializableExtra("user");
                    user.setAccount(binding.banktext.getText().toString());
                    Intent intent = new Intent(Signup3Activity.this,Signup4Activity.class);
                    intent.putExtra("user",user);
                    startActivity(intent);
                }
            }
        });

    }
}