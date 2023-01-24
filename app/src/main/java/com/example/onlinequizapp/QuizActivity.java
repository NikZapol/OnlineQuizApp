package com.example.onlinequizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.squareup.picasso.Picasso;

public class QuizActivity extends AppCompatActivity {
    //создание массива данных с списком вопросов
    private final List<QuestionsList> questionsLists = new ArrayList<>();

    private TextView quizTimer;

    private RelativeLayout option1Layout, option2Layout, option3Layout, option4Layout;
    private TextView option1TV, option2TV, option3TV, option4TV;
    private ImageView option1Icon, option2Icon, option3Icon, option4Icon;

    private TextView questionTV;
    private TextView totalquestionTV;
    private TextView currentQuestion, answertv;
    String selectedOptionIncorrect="0";

    ImageView quizImage;


    //создание референса базы данных из ссылки
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://quizappadmob-e5ef2-default-rtdb.firebaseio.com/");
    private final DatabaseReference referenceTickets = FirebaseDatabase.getInstance().getReference("Tickets");

    // таймер для теста
    private CountDownTimer countDownTimer;

    //позиция вопроса в настоящем времени по умолч.(первый вопрос)
    private int currentQuestionPosition = 0;

    //номер выбранного ответа. должен быть от 1 до 4(у нас 4 ответа), 0 значит никакой ответ не выбран
    private int selectedOption = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        quizTimer=findViewById(R.id.quizTimer);
        option1Layout=findViewById(R.id.option1Layout);
        option2Layout=findViewById(R.id.option2Layout);
        option3Layout=findViewById(R.id.option3Layout);
        option4Layout=findViewById(R.id.option4Layout);

        option1TV=findViewById(R.id.option1TV);
        option2TV=findViewById(R.id.option2TV);
        option3TV=findViewById(R.id.option3TV);
        option4TV=findViewById(R.id.option4TV);

        option1Icon=findViewById(R.id.option1icon);
        option2Icon=findViewById(R.id.option2icon);
        option3Icon=findViewById(R.id.option3icon);
        option4Icon=findViewById(R.id.option4icon);

        questionTV=findViewById(R.id.questionTV);
        totalquestionTV=findViewById(R.id.totalQuestionsTV);
        currentQuestion=findViewById(R.id.currentQuestionTV);

        quizImage=findViewById(R.id.quizImage);

        answertv=findViewById(R.id.answertv);

        final AppCompatButton nextBtn = findViewById(R.id.nextQuestionBtn);


        //показать инструкцию
        InstructionsDialog instructionsDialog = new InstructionsDialog(QuizActivity.this);
        instructionsDialog.setCancelable(false);
        instructionsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        instructionsDialog.show();

        //получить номер билета
        Intent intent = getIntent();
        String message = intent.getStringExtra("message");
        //Toast.makeText(QuizActivity.this, message, Toast.LENGTH_SHORT).show();


        //получение информации(вопросов и времени) из базы данных
        referenceTickets.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final int getQuizTime = Integer.parseInt(snapshot.child("time").getValue(String.class));

                for (DataSnapshot tickets : snapshot.child(message).getChildren()){
                    String getQuestion = tickets.child("question").getValue(String.class);
                    String getOption1 = tickets.child("option1").getValue(String.class);
                    String getOption2 = tickets.child("option2").getValue(String.class);
                    String getOption3 = tickets.child("option3").getValue(String.class);
                    String getOption4 = tickets.child("option4").getValue(String.class);

                    String getImageLink = tickets.child("image").getValue(String.class);
                    Picasso.get().load(getImageLink).into(quizImage);

                    final int getAnswer = Integer.parseInt(tickets.child("answer").getValue(String.class));




                    //создание обьекта "лист вопросов" и подробностей
                    QuestionsList questionsList = new QuestionsList(getQuestion, getOption1, getOption2, getOption3, getOption4, getImageLink, getAnswer);
                    questionsLists.add(questionsList);
                }
                totalquestionTV.setText("/"+questionsLists.size());

                //начать таймер
                startQuizTimer(getQuizTime);

                selectQuestion(currentQuestionPosition);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QuizActivity.this, "Failed to get data from firebase", Toast.LENGTH_SHORT).show();
            }
        });


        option1Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //выбран 1 ответ
                selectedOption =1;
                //выбрать ответ
                selectOption(option1Layout, option1Icon);
            }
        });
        option2Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //выбран 2 ответ
                selectedOption =2;
                //выбрать ответ
                selectOption(option2Layout, option2Icon);
            }
        });
        option3Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //выбран 3 ответ
                selectedOption =3;
                //выбрать ответ
                selectOption(option3Layout, option3Icon);
            }
        });
        option4Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //выбран 4 ответ
                selectedOption =4;
                //выбрать ответ
                selectOption(option4Layout, option4Icon);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //проверка если пользователь выбрал ответ или нет
                if(selectedOption!=0){

                    if(selectedOptionIncorrect=="0"){
                        questionsLists.get(currentQuestionPosition).setUserSelectedAnswer(selectedOption);
                    }


                    selectedOption=0;
                    selectedOptionIncorrect="0";
                    currentQuestionPosition++;
                    currentQuestion.setText(String.valueOf(currentQuestionPosition));
                    if(currentQuestionPosition < questionsLists.size()){
                        selectQuestion((currentQuestionPosition)); //выбрать вопрос
                    }

                    else{
                        //вопросы закончились поэтому заканчиваем тест
                        countDownTimer.cancel(); //остановить таймер
                        finishQuiz();//закончить тест
                    }
                }else{
                    Toast.makeText(QuizActivity.this, "Выберите ответ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void finishQuiz(){
        //создаем интент для открытия результатов теста
        Intent intent = new Intent(QuizActivity.this, QuizResult.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("questions", (Serializable) questionsLists);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
    private void startQuizTimer(int maxTimeInSeconds){

        countDownTimer = new CountDownTimer(maxTimeInSeconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                long getHour = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                long getMinute = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                long getSecond = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);

                String generateTime = String.format(Locale.getDefault(), "%02d:%02d:%02d",getHour,
                        getMinute - TimeUnit.HOURS.toMinutes(getHour),
                                getSecond - TimeUnit.MINUTES.toSeconds(getMinute));

                quizTimer.setText(generateTime);
            }

            @Override
            public void onFinish() {
                //закрыть тест когда время закончится
                finishQuiz();
            }
        };

        countDownTimer.start();
    }



    private void selectQuestion(int questionListPosition){
        //сбросить вопросы с прошлого вопроса
        resetOptions();
        //подставить данные из бд в разметку
        questionTV.setText(questionsLists.get(questionListPosition).getQuestion());
        option1TV.setText(questionsLists.get(questionListPosition).getOption1());
        option2TV.setText(questionsLists.get(questionListPosition).getOption2());
        option3TV.setText(questionsLists.get(questionListPosition).getOption3());
        option4TV.setText(questionsLists.get(questionListPosition).getOption4());
        answertv.setText(String.valueOf(questionsLists.get(questionListPosition).getAnswer()));
        Picasso.get().load(questionsLists.get(questionListPosition).getImageLink()).into(quizImage);

        //если всего 2 варианта ответа - прячем 3 кнопку ответа
        if(option3TV.getText().toString().equals("3. Нету ответа")){
            option3Layout.setVisibility(View.GONE);
        } else option3Layout.setVisibility(View.VISIBLE);

        //если всего 3 варианта ответа - прячем 4 кнопку ответа
        if(option4TV.getText().toString().equals("4. Нету ответа")){
            option4Layout.setVisibility(View.GONE);
        } else option4Layout.setVisibility(View.VISIBLE);
    }

    private int getCorrectAnswer(){
        int correctAnswer = 0;

        for(int i =0; i< questionsLists.size(); i++){
            int getUserSelectedOption = selectedOption; //получить ответ выбранный пользователем
            int getQuestionAnswer = Integer.valueOf(answertv.getText().toString()); //получить правильный ответ для воопроса
            if(getQuestionAnswer == getUserSelectedOption){ //если ответ пользователя совпадает с правильным ответом
                correctAnswer=1; //увеличить количество правильных ответов
            }
        }
        return  correctAnswer;
    }

    private void resetOptions(){
        //сбрасываем визуальное нажатие кнопки
        option1Layout.setBackgroundResource(R.drawable.round_back_white50_10);
        option2Layout.setBackgroundResource(R.drawable.round_back_white50_10);
        option3Layout.setBackgroundResource(R.drawable.round_back_white50_10);
        option4Layout.setBackgroundResource(R.drawable.round_back_white50_10);

        option1Icon.setImageResource(R.drawable.round_back_white50_100);
        option2Icon.setImageResource(R.drawable.round_back_white50_100);
        option3Icon.setImageResource(R.drawable.round_back_white50_100);
        option4Icon.setImageResource(R.drawable.round_back_white50_100);
    }

    private void selectOption(RelativeLayout selectedOptionLayout, ImageView selectedOptionIcon){
        //сбросить выбор для выбора нового
        resetOptions();
        if(getCorrectAnswer()==1){
            selectedOptionIcon.setImageResource(R.drawable.check_icon);
            selectedOptionLayout.setBackgroundResource(R.drawable.round_back_selected_option);
        } else {
            selectedOptionIcon.setImageResource(R.drawable.red_cross);
            selectedOptionLayout.setBackgroundResource(R.drawable.round_back_selected_option_red);
            selectedOptionIncorrect="1";
        }




    }
}