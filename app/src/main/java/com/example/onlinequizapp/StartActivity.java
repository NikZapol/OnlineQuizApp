package com.example.onlinequizapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        final AppCompatButton startQuizBtn = findViewById(R.id.startQuizBtn);
        final AppCompatButton quitBtn = findViewById(R.id.quitBtn);
        Intent intent = getIntent();
        String message = intent.getStringExtra("message");
        //Toast.makeText(StartActivity.this, message, Toast.LENGTH_SHORT).show();

        startQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //запустить активити
                Intent intent = new Intent(StartActivity.this, QuizActivity.class);
                startActivity(intent);
                intent.putExtra("message", message);
                startActivity(intent);
            }
        });

        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //закрыть активити
                finish();
            }
        });
    }
}