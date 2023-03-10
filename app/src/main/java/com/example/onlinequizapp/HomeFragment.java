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

            //???????????? ?????? ?????????????? https://firebasestorage.googleapis.com/v0/b/quizappadmob-e5ef2.appspot.com/o/4.png?alt=media&token=0fd0bac7-5a98-4fdb-bcf3-c74a318603fe

        menubtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                referenceTickets.child("time").setValue("1200");

                String ticketnumber = "ticket2";
                String questionnumber = "4";


                String imageurl = "https://firebasestorage.googleapis.com/v0/b/quizappadmob-e5ef2.appspot.com/o/t2%2Ft2_4.png?alt=media&token=795220a0-e12b-4601-96e2-5a4eab55ca82";
                referenceTickets.child(ticketnumber).child(questionnumber).child("image").setValue(imageurl);

                referenceTickets.child(ticketnumber).child(questionnumber).child("answer").setValue("3");


                referenceTickets.child(ticketnumber).child(questionnumber).child("option1").setValue("1. ???????????????? ?????????? ???????????????????????? ??????????????.");
                referenceTickets.child(ticketnumber).child(questionnumber).child("option2").setValue("2. ???????????????? ???????? ???????????????????????? ?????????????? ???? ?????????????????? ???? ?????????? 20 ????/??.");
                referenceTickets.child(ticketnumber).child(questionnumber).child("option3").setValue("3. ???????????????? ???????????????????????? ???????????????????????? ??????????????.");
                referenceTickets.child(ticketnumber).child(questionnumber).child("option4").setValue("4. ???????? ????????????");
                //referenceTickets.child(ticketnumber).child(questionnumber).child("option4").setValue("4. ???????? ????????????");
                referenceTickets.child(ticketnumber).child(questionnumber).child("question").setValue("?????? ?????????????????? ?? ???????? ???????????????? ?????????? ???????????");

                Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
    private void getTicketsData() {
                ArrayList<Ticket> tickets = new ArrayList<>();
                // Add the TicketList here
                TicketList ticketList = new TicketList();
                ticketList.add(new Ticket("?????????? 1"));
                ticketList.add(new Ticket("?????????? 2"));
                ticketList.add(new Ticket("?????????? 3"));
                ticketList.add(new Ticket("?????????? 4"));
                ticketList.add(new Ticket("?????????? 5"));
                ticketList.add(new Ticket("?????????? 6"));
                ticketList.add(new Ticket("?????????? 7"));
                ticketList.add(new Ticket("?????????? 8"));
                ticketList.add(new Ticket("?????????? 9"));
                ticketList.add(new Ticket("?????????? 10"));
                ticketList.add(new Ticket("?????????? 11"));
                ticketList.add(new Ticket("?????????? 12"));
                ticketList.add(new Ticket("?????????? 13"));
                ticketList.add(new Ticket("?????????? 14"));
                ticketList.add(new Ticket("?????????? 16"));
                ticketList.add(new Ticket("?????????? 17"));
                ticketList.add(new Ticket("?????????? 18"));
                ticketList.add(new Ticket("?????????? 19"));
                ticketList.add(new Ticket("?????????? 20"));


                tickets.addAll(ticketList.getTickets());
                list.addAll(tickets);
                myAdapterTicket.notifyDataSetChanged();


    }




}
