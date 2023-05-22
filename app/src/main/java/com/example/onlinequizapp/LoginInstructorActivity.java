package com.example.onlinequizapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginInstructorActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register;
    private EditText editTextEmail, editTextPassword, editTextPin;
    private MaterialButton signin;
    private ImageView facebookiv;
    public LinearLayout fastlogin;
    public Button ivanlogpassbtn, nikitalogpassbtn, oleglogpassbtn;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    int pincode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_instructor);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        signin = (MaterialButton) findViewById(R.id.loginbtn);
        signin.setOnClickListener(this);


        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        editTextPin = (EditText) findViewById(R.id.pin);
        progressBar=(ProgressBar) findViewById(R.id.progressbar);

        facebookiv = findViewById(R.id.facebookiv);
        fastlogin = findViewById(R.id.fastlogin);


        ivanlogpassbtn = findViewById(R.id.ivanlogpassbtn);
        nikitalogpassbtn = findViewById(R.id.nikitalogpassbtn);
        oleglogpassbtn = findViewById(R.id.oleglogpassbtn);




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
                editTextEmail.setText("niknik623@mail.ru");
                editTextPassword.setText("12345678");
                editTextPin.setText("1234");
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
            // startActivity(new Intent(LoginInstructorActivity.this, BottomNavigationActivity.class));
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                startActivity(new Intent(this, com.example.onlinequizapp.RegisterUser.class));
                break;

            case R.id.instructorlogin:
                startActivity(new Intent(this, com.example.onlinequizapp.LoginInstructorActivity.class));
                break;

            case R.id.loginbtn:
                userLogin();
                break;
        }
    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String pin = editTextPin.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Вы не ввели вашу почту");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Вы ввели неправильный формат почты");
            editTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editTextPassword.setError("Вы не ввели пароль");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editTextPassword.setError("Минимальное количество символов = 6");
            editTextPassword.requestFocus();
            return;
        }
        if (pin.isEmpty()) {
            editTextPin.setError("Вы не ввели PIN-код");
            editTextPin.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.VISIBLE);
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    Log.d("LoginInstructorActivity", "userUID: " + user.getUid());

                    if (user.isEmailVerified()) {
                        // Check pincode from the database
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Instructors").child(user.getUid());
                        Log.d("LoginInstructorActivity", "userUID: " + user.getUid());

                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild("pincode")) {
                                    String pincode = dataSnapshot.child("pincode").getValue(String.class);
                                    Log.d("LoginInstructorActivity", "pincode: " + pincode);
                                    if (pincode.equals(editTextPin.getText().toString())) {
                                        // Redirect to user profile
                                        startActivity(new Intent(LoginInstructorActivity.this, BottomNavigationActivityInstructor.class));
                                    } else {
                                        editTextPin.setError("Неверный PIN-код");
                                        editTextPin.requestFocus();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                } else {
                                    Toast.makeText(LoginInstructorActivity.this, "Ошибка! Пин-код не найден", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(LoginInstructorActivity.this, "Ошибка! Пин-код не найден", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }

                        });
                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(LoginInstructorActivity.this, "Проверьте почту для верификации почты", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(LoginInstructorActivity.this, "Ошибка! Проверьте логин и пароль, или подключение к интернету", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });


    }
}