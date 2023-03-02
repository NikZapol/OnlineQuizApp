package com.example.onlinequizapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register, forgotpass;
    private EditText editTextEmail, editTextPassword;
    private MaterialButton signin;
    private ImageView facebookiv;
    public LinearLayout fastlogin;
    public Button ivanlogpassbtn, nikitalogpassbtn, oleglogpassbtn;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        signin = (MaterialButton) findViewById(R.id.loginbtn);
        signin.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);

        progressBar=(ProgressBar) findViewById(R.id.progressbar);

        facebookiv = findViewById(R.id.facebookiv);
        fastlogin = findViewById(R.id.fastlogin);

        forgotpass = findViewById(R.id.forgotpass);

        ivanlogpassbtn = findViewById(R.id.ivanlogpassbtn);
        nikitalogpassbtn = findViewById(R.id.nikitalogpassbtn);
        oleglogpassbtn = findViewById(R.id.oleglogpassbtn);

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Password reset email sent successfully
                            Toast.makeText(getApplicationContext(), "Успешно отправлено письмо с сбросом пароля", Toast.LENGTH_SHORT).show();
                        } else {
                            // Password reset email failed to send
                            Toast.makeText(getApplicationContext(), "Произошла ошибка при отправлении письма с сбросом пароля", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        facebookiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fastlogin.setVisibility(View.VISIBLE);
            }
        });



        ivanlogpassbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextEmail.setText("notbigsurpise@yandex.ru");
                editTextPassword.setText("12345678");
            }
        });

        nikitalogpassbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextEmail.setText("niknik623@yandex.ru");
                editTextPassword.setText("123456");
            }
        });

        oleglogpassbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextEmail.setText("nikitazsite@yandex.ru");
                editTextPassword.setText("123456");
            }
        });
     }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
           // startActivity(new Intent(LoginActivity.this, BottomNavigationActivity.class));
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                startActivity(new Intent(this, com.example.onlinequizapp.RegisterUser.class));
                break;

            case R.id.loginbtn:
                userLogin();
                break;
        }
    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(email.isEmpty()){
            editTextEmail.setError("Вы не ввели вашу почту");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Вы ввели неправильный формат почты");
            editTextEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editTextPassword.setError("Вы не ввели пароль");
            editTextPassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            editTextPassword.setError("Минимальное количество символов = 6");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()){
                        //redirect to user profile
                        startActivity(new Intent(LoginActivity.this, BottomNavigationActivity.class));
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(LoginActivity.this, "Проверьте почту для верификации почты", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }

                }else{
                    Toast.makeText(LoginActivity.this, "Ошибка! Проверьте логин и пароль, или подключение к интернету", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}