package com.tapping.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.tapping.R;
import com.tapping.databinding.ActivityModifiactivity2Binding;

import org.json.JSONException;
import org.json.JSONObject;

public class ModifiActivity2 extends AppCompatActivity {
    ActivityModifiactivity2Binding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityModifiactivity2Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        try {
            JSONObject jsonObject = new JSONObject(getSharedPreferences("pref",MODE_PRIVATE).getString("user",""));
            binding.id.setText(jsonObject.getString("id"));
            binding.name.setText(jsonObject.getString("name"));
            binding.address.setText(jsonObject.getString("address"));
            binding.phonenumber.setText(jsonObject.getString("phone"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);
        setContentView(view);

    }
}