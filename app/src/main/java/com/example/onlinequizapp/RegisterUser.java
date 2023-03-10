package com.example.onlinequizapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private AppCompatButton registerUser;
    private EditText EditEmail, EditPassword, EditFullname, EditAge;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        mAuth = FirebaseAuth.getInstance();



        registerUser = (AppCompatButton) findViewById(R.id.registerbtn);

        EditEmail=(EditText) findViewById(R.id.email);
        EditPassword=(EditText) findViewById(R.id.password);
        EditFullname=(EditText) findViewById(R.id.fullname);
        EditAge=(EditText) findViewById(R.id.age);

        progressBar=(ProgressBar) findViewById(R.id.progressbar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.banner:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.registerbtn:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String email= EditEmail.getText().toString().trim();
        String password= EditPassword.getText().toString().trim();
        String fullname= EditFullname.getText().toString().trim();
        String age= EditAge.getText().toString().trim();


        if(email.isEmpty()){
            EditEmail.setError("?????????????? ???????? ??????????!");
            EditEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            EditEmail.setError("???? ?????????? ?????????? ??????????????????????!");
            EditEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            EditPassword.setError("?????????????? ?????? ????????????!");
            EditPassword.requestFocus();
            return;
        }

        if(password.length() < 6 ){
            EditPassword.setError("?????????????? ?????? ?????????????? 6 ???????????????? ?? ????????????");
            EditPassword.requestFocus();
            return;
        }

        if(fullname.isEmpty()){
            EditFullname.setError("?????????????? ???????? ??????!");
            EditFullname.requestFocus();
            return;
        }

        if(age.isEmpty()){
            EditAge.setError("?????????????? ?????? ??????????????!");
            EditAge.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            com.example.onlinequizapp.User user = new com.example.onlinequizapp.User(fullname, age, email, password);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                Toast.makeText(RegisterUser.this, "???????????????????????? ?????? ?????????????? ??????????????????????????????!", Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.GONE);

                                                //?????????????????????? ?????????????? ?? ????????????
                                                startActivity(new Intent(RegisterUser.this, LoginActivity.class));
                                            }else{
                                                Toast.makeText(RegisterUser.this, "???????????????????????? ???? ?????? ??????????????????????????????. ???????????????????? ?????? ??????!", Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        }else{
                            Toast.makeText(RegisterUser.this, "???????????????????????? ???? ?????? ??????????????????????????????. ???????????????????? ?????? ??????!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

    }
}