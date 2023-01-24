package com.example.onlinequizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeFragment extends Fragment {
    FirebaseUser user;
    DatabaseReference referenceUsers, referenceTickets, referenceLessonsSecondRecord;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    LinearLayout firstrecordlayout, secondrecordlayout;
    String userID;
    Button menubtn1, menubtn2, menubtn3, buttonbilet1,buttonbilet2;
    LinearLayout choicesLayout, choicesLayout2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        referenceTickets = FirebaseDatabase.getInstance().getReference("Tickets");
        userID = user.getUid();

        menubtn1=rootView.findViewById(R.id.menubtn1);
        menubtn2=rootView.findViewById(R.id.menubtn2);
        menubtn3=rootView.findViewById(R.id.menubtn3);
        buttonbilet1=rootView.findViewById(R.id.buttonbilet1);
        buttonbilet2=rootView.findViewById(R.id.buttonbilet2);
        choicesLayout=rootView.findViewById(R.id.choices_layout);
        choicesLayout2=rootView.findViewById(R.id.choices_layout2);

        menubtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menubtn1.setVisibility(View.GONE);
                menubtn2.setVisibility(View.GONE);
                menubtn3.setVisibility(View.GONE);
                choicesLayout.setVisibility(View.VISIBLE);
                choicesLayout2.setVisibility(View.VISIBLE);
            }
        });
            //ticket1 = ticket 26 http://www.pdd24.com/pdd-onlain

            //вопрос без рисунка https://firebasestorage.googleapis.com/v0/b/quizappadmob-e5ef2.appspot.com/o/4.png?alt=media&token=0fd0bac7-5a98-4fdb-bcf3-c74a318603fe

        menubtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                referenceTickets.child("time").setValue("1200");

                String ticketnumber = "ticket2";
                String questionnumber = "4";


                String imageurl = "https://firebasestorage.googleapis.com/v0/b/quizappadmob-e5ef2.appspot.com/o/t2%2Ft2_4.png?alt=media&token=795220a0-e12b-4601-96e2-5a4eab55ca82";
                referenceTickets.child(ticketnumber).child(questionnumber).child("image").setValue(imageurl);

                referenceTickets.child(ticketnumber).child(questionnumber).child("answer").setValue("3");


                referenceTickets.child(ticketnumber).child(questionnumber).child("option1").setValue("1. Движение любых транспортных средств.");
                referenceTickets.child(ticketnumber).child(questionnumber).child("option2").setValue("2. Движение всех транспортных средств со скоростью не более 20 км/ч.");
                referenceTickets.child(ticketnumber).child(questionnumber).child("option3").setValue("3. Движение механических транспортных средств.");
                referenceTickets.child(ticketnumber).child(questionnumber).child("option4").setValue("4. Нету ответа");
                //referenceTickets.child(ticketnumber).child(questionnumber).child("option4").setValue("4. Нету ответа");
                referenceTickets.child(ticketnumber).child(questionnumber).child("question").setValue("Что запрещено в зоне действия этого знака?");

                Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
            }
        });

        buttonbilet1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), com.example.onlinequizapp.StartActivity.class);
                startActivity(intent);
                intent.putExtra("message", "ticket1");
                getActivity().startActivity(intent);
            }
        });

        buttonbilet2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), com.example.onlinequizapp.StartActivity.class);
                startActivity(intent);
                intent.putExtra("message", "ticket2");
                getActivity().startActivity(intent);
            }
        });
        return rootView;
    }

    public void updateDetail() {
        Intent intent = new Intent(getActivity(), com.example.onlinequizapp.StartActivity.class);
        startActivity(intent);
    }
    }
