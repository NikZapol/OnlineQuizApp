package com.example.onlinequizapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.cache.DiskLruCache;


public class RecordsFragment extends Fragment {
    FirebaseUser user;
    DatabaseReference referenceUsers, referenceLessons, referenceLessonsSecondRecord;
    String userID;
    TextView greetingsTV, fullnameTV, lessonidTV, lessonidTVSecondRecord, useridTV, recordsTitle, lessonUIslotTV;
    String lessonid, userId;
    RecyclerView recyclerView;
    ArrayList<Lesson> list;
    MyAdapter myAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_records, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        referenceUsers = FirebaseDatabase.getInstance().getReference("Users");
        referenceLessons = FirebaseDatabase.getInstance().getReference("Lessons");
        referenceLessonsSecondRecord = FirebaseDatabase.getInstance().getReference("Lessons2");
        userID = user.getUid();


        useridTV = rootView.findViewById(R.id.useridTV);
        lessonidTV = rootView.findViewById(R.id.lessonidTV);
        lessonidTVSecondRecord = rootView.findViewById(R.id.lessonidTVSecondRecord);
        lessonUIslotTV = rootView.findViewById(R.id.lessonUIslotTV);
        greetingsTV = rootView.findViewById(R.id.greetingsTitle);
        fullnameTV = rootView.findViewById(R.id.fullnameUser);
        recordsTitle = rootView.findViewById(R.id.recordsTitle);


        recyclerView = rootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list = new ArrayList<>();
        myAdapter = new MyAdapter(getContext(), list);
        recyclerView.setAdapter(myAdapter);


        //подставление имени
        referenceUsers.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot Snapshot) {
                User userProfile = Snapshot.getValue(User.class);
                if (userProfile != null) {
                    String fullname = userProfile.fullName;
                    greetingsTV.setText("Здравствуйте, " + fullname + "!");
                    fullnameTV.setText(fullname);
                    getLessonsData();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Произошла ошибка сети", Toast.LENGTH_SHORT).show();

            }
        });

        return rootView;
    }

    private void getLessonsData() {
        Query query = referenceLessons.orderByChild("userId").equalTo(userID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Lesson lesson = snapshot.getValue(Lesson.class);
                    list.add(lesson);

                    userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    lessonid = referenceLessons.push().getKey();
                    useridTV.setText(userId);
                    lessonidTV.setText(lessonid);
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}


    /* //старая не рациональная функция получения урока
    public void getLessonsData() {
        referenceLessons.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    String pushkey = snapshot.getKey();
                    //поиск "своих"(юзера) записей ... (чтобы не находить чужие записи)
                    referenceLessons.child(pushkey).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            if (snapshot.getKey().equals("userId")) {
                                if (snapshot.getValue().toString().equals(userID)) {
                                    //проверка существования записи на первом слоте
                                    referenceLessons.child(pushkey).addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                            if (snapshot.getKey().equals("lessonUIslot")) {
                                                if (snapshot.getValue().toString().equals("1")){

                                                    //заполнение формы информацией из бд
                                                    lessonkey = pushkey;
                                                    //Toast.makeText(getActivity(), "У вас есть запись на урок", Toast.LENGTH_SHORT).show();
                                                    recordsTitle.setText("Ваши занятия: ");

                                                    referenceLessons.child(lessonkey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot Snapshot) {
                                                            Lesson lesson = Snapshot.getValue(Lesson.class);

                                                            if(lesson != null){
                                                                list.add(lesson);

                                                                userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                                lessonid = referenceLessons.push().getKey();
                                                                useridTV.setText(userId);
                                                                lessonidTV.setText(lessonid);

                                                            }
                                                            myAdapter.notifyDataSetChanged();
                                                        }
                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {
                                                        }
                                                    });
                                                    //Toast.makeText(getActivity(), "recordAlreadyHaveSlot1=1", Toast.LENGTH_SHORT).show();
                                                }*//* else {
                                                    //проверка существования записи на втором слоте
                                                    referenceLessons.child(pushkey).addChildEventListener(new ChildEventListener() {
                                                        @Override
                                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                            if (snapshot.getKey().equals("lessonUIslot")) {
                                                                if (snapshot.getValue().toString().equals("2")){
                                                                    recordAlreadyHaveSlot2="2";
                                                                    lessonUIslotTV.setText("2");
                                                                    //заполнение формы информацией из бд
                                                                    lessonkey = pushkey;
                                                                    //Toast.makeText(getActivity(), "У вас есть запись на урок", Toast.LENGTH_SHORT).show();
                                                                    recordsTitle.setText("Ваши занятия: ");
                                                                    referenceLessons.child(lessonkey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot Snapshot) {
                                                                            Lesson lesson = Snapshot.getValue(Lesson.class);
                                                                            if(lesson != null){
                                                                                list.add(lesson);

                                                                                *//**//*String instructor = lesson.instructor;
                                                                                String time = lesson.time;
                                                                                String date = lesson.date;
                                                                                instructorTVSecondRecord.setText(instructor);
                                                                                timeTVSecondRecord.setText(time);
                                                                                dateTVSecondRecord.setText(date);*//**//*

                                                                                userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                                                useridTV.setText(userId);
                                                                                lessonid = referenceLessons.push().getKey();
                                                                                lessonidTVSecondRecord.setText(lessonid);

                                                                            }
                                                                            myAdapter.notifyDataSetChanged();
                                                                        }
                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError error) {
                                                                        }
                                                                    });
                                                                    //Toast.makeText(getActivity(), "recordAlreadyHaveSlot2=2", Toast.LENGTH_SHORT).show();

                                                                } else {
                                                                    //проверка существования записи на третьем слоте
                                                                    referenceLessons.child(pushkey).addChildEventListener(new ChildEventListener() {
                                                                        @Override
                                                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                                            if (snapshot.getKey().equals("lessonUIslot")) {
                                                                                if (snapshot.getValue().toString().equals("3")){
                                                                                    recordAlreadyHaveSlot2="3";
                                                                                    lessonUIslotTV.setText("3");
                                                                                    //заполнение формы информацией из бд
                                                                                    lessonkey = pushkey;
                                                                                    //Toast.makeText(getActivity(), "У вас есть запись на урок", Toast.LENGTH_SHORT).show();
                                                                                    recordsTitle.setText("Ваши занятия: ");
                                                                                    referenceLessons.child(lessonkey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(@NonNull DataSnapshot Snapshot) {
                                                                                            Lesson lesson = Snapshot.getValue(Lesson.class);
                                                                                            if(lesson != null){
                                                                                                list.add(lesson);

                                                                                                *//**//*String instructor = lesson.instructor;
                                                                                                String time = lesson.time;
                                                                                                String date = lesson.date;
                                                                                                instructorTVSecondRecord.setText(instructor);
                                                                                                timeTVSecondRecord.setText(time);
                                                                                                dateTVSecondRecord.setText(date);*//**//*

                                                                                                userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                                                                useridTV.setText(userId);
                                                                                                lessonid = referenceLessons.push().getKey();
                                                                                                lessonidTVSecondRecord.setText(lessonid);

                                                                                            }
                                                                                            myAdapter.notifyDataSetChanged();
                                                                                        }
                                                                                        @Override
                                                                                        public void onCancelled(@NonNull DatabaseError error) {
                                                                                        }
                                                                                    });
                                                                                    //Toast.makeText(getActivity(), "recordAlreadyHaveSlot2=2", Toast.LENGTH_SHORT).show();

                                                                                } else {

                                                                                }
                                                                            }
                                                                        }
                                                                        @Override
                                                                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
                                                                        @Override
                                                                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
                                                                        @Override
                                                                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError error) {}
                                                                    });
                                                                }
                                                            }
                                                        }
                                                        @Override
                                                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
                                                        @Override
                                                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
                                                        @Override
                                                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {}
                                                    });
                                                }*//*
                                            }
                                        }
                                        @Override
                                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
                                        @Override
                                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
                                        @Override
                                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {}
                                    });
                                }
                            }
                        }
                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }*/

