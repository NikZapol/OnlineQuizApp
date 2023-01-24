package com.example.onlinequizapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class QuizResult extends AppCompatActivity {

    private  List<QuestionsList> questionsLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        final TextView scoreTV = findViewById(R.id.scoreTV);
        final TextView totalScoreTV = findViewById(R.id.totalScoreTV);
        final AppCompatButton shareBtn = findViewById(R.id.shareBtn);
        final AppCompatButton reTakeBtn = findViewById(R.id.reTakeQuizBtn);
        //получение листа вопросов из mainActivity
        questionsLists = (List<QuestionsList>) getIntent().getSerializableExtra("questions");

        totalScoreTV.setText("/"+questionsLists.size());
        scoreTV.setText(getCorrectAnswers() +"");

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //вернуться в главное меню
                startActivity(new Intent(QuizResult.this, BottomNavigationActivity.class));
            }
        });

        reTakeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuizResult.this, QuizActivity.class));
                finish();
            }
        });
    }

    private int getCorrectAnswers(){
        int correctAnswer = 0;

        for(int i =0; i< questionsLists.size(); i++){
            int getUserSelectedOption = questionsLists.get(i).getUserSelectedAnswer(); //получить ответ выбранный пользователем
            int getQuestionAnswer = questionsLists.get(i).getAnswer(); //получить правильный ответ для воопроса
            if(getQuestionAnswer == getUserSelectedOption){
                correctAnswer++; //увеличить количество правильных ответов
            }
        }
        return  correctAnswer;
    }
}