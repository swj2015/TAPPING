package com.tapping.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.tapping.R;
import com.tapping.databinding.ActivitySignupBinding;
import com.tapping.dto.UserDto;

public class SignupActivity extends AppCompatActivity {
    ActivitySignupBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        String pw = binding.pwtext.getText().toString();
        String pw2 = binding.pwtext2.getText().toString();
        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!pw.equals(pw2))
                {
                    Toast.makeText(SignupActivity.this,"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT);
                    binding.pwtext.setText("");
                    binding.idtext.setText("");
                    binding.pwtext2.setText("");
                }else{
                    UserDto user = new UserDto();
                    user.setId(binding.idtext.getText().toString());
                    user.setPasswd(binding.pwtext.getText().toString());
                    Intent intent = new Intent(SignupActivity.this,Signup2Activity.class);
                    intent.putExtra("user",user);
                    startActivity(intent);

                }
            }
        });

    }
}