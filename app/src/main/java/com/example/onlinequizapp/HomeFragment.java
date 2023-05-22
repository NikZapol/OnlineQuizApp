package com.example.onlinequizapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    FirebaseUser user;
    DatabaseReference referenceUsers, referenceTickets, referenceLessonsSecondRecord;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Tickets");
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Instructors").child("Nzub9Pge3NVbfmB1YMgFUCcb7Qd2");
    LinearLayout firstrecordlayout, secondrecordlayout;
    String userID;
    Button menubtn1, menubtn2, menubtn3, buttonbilet1,buttonbilet2;
    LinearLayout choicesLayout, choicesLayout2;

    RecyclerView recyclerView;
    ArrayList<Ticket> list;
    MyAdapterTicket myAdapterTicket;


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


        list = new ArrayList<>();
        myAdapterTicket = new MyAdapterTicket(getContext(), list);
        recyclerView = rootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        recyclerView.setAdapter(myAdapterTicket);






        menubtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menubtn1.setVisibility(View.GONE);
                menubtn2.setVisibility(View.GONE);
                menubtn3.setVisibility(View.GONE);
                getTicketsData();
                recyclerView.setVisibility(View.VISIBLE);

            }
        });
            //ticket1 = ticket 26 http://www.pdd24.com/pdd-onlain

            //вопрос без рисунка https://firebasestorage.googleapis.com/v0/b/quizappadmob-e5ef2.appspot.com/o/4.png?alt=media&token=0fd0bac7-5a98-4fdb-bcf3-c74a318603fe

        menubtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* referenceTickets.child("time").setValue("1200");

                String ticketnumber = "ticket11";

                String imageurl = "https://firebasestorage.googleapis.com/v0/b/quizappadmob-e5ef2.appspot.com/o/4.png?alt=media&token=0fd0bac7-5a98-4fdb-bcf3-c74a318603fe";
                String questionnumber = "20";
                referenceTickets.child(ticketnumber).child(questionnumber).child("image").setValue(imageurl);

                referenceTickets.child(ticketnumber).child(questionnumber).child("question").setValue("Какое расстояние проедет транспортное средство за время, равное среднему времени реакции водителя, при скорости движения около 90 км/час?");


                referenceTickets.child(ticketnumber).child(questionnumber).child("option1").setValue("1. Примерно 15 м.");
                referenceTickets.child(ticketnumber).child(questionnumber).child("option2").setValue("2. Примерно 25 м.");
                referenceTickets.child(ticketnumber).child(questionnumber).child("option3").setValue("3. Примерно 35 м.");
                referenceTickets.child(ticketnumber).child(questionnumber).child("option4").setValue("4. Нету ответа");
                //referenceTickets.child(ticketnumber).child(questionnumber).child("option4").setValue("4. Нету ответа");

                referenceTickets.child(ticketnumber).child(questionnumber).child("answer").setValue("2");
                */
                ref.child("pincode").setValue("1234");

                Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
    private void getTicketsData() {
                ArrayList<Ticket> tickets = new ArrayList<>();
                // Add the TicketList here
                TicketList ticketList = new TicketList();
                ticketList.add(new Ticket("Билет 1"));
                ticketList.add(new Ticket("Билет 2"));
                ticketList.add(new Ticket("Билет 3"));
                ticketList.add(new Ticket("Билет 4"));
                ticketList.add(new Ticket("Билет 5"));
                ticketList.add(new Ticket("Билет 6"));
                ticketList.add(new Ticket("Билет 7"));
                ticketList.add(new Ticket("Билет 8"));
                ticketList.add(new Ticket("Билет 9"));
                ticketList.add(new Ticket("Билет 10"));
                ticketList.add(new Ticket("Билет 11"));
                ticketList.add(new Ticket("Билет 12"));
                ticketList.add(new Ticket("Билет 13"));
                ticketList.add(new Ticket("Билет 14"));
                ticketList.add(new Ticket("Билет 16"));
                ticketList.add(new Ticket("Билет 17"));
                ticketList.add(new Ticket("Билет 18"));
                ticketList.add(new Ticket("Билет 19"));
                ticketList.add(new Ticket("Билет 20"));


                tickets.addAll(ticketList.getTickets());
                list.addAll(tickets);
                myAdapterTicket.notifyDataSetChanged();


    }




}
