package com.codegama.todolistapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.codegama.todolistapplication.R;

import androidx.appcompat.app.AppCompatActivity;

public class StaffActivity extends AppCompatActivity{
    Button btBackhome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);

        btBackhome = findViewById(R.id.bt_backhome);
        btBackhome.setOnClickListener(this::onClick);

    }

    private void onClick(View v) {
        Intent intent = new Intent(StaffActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}
