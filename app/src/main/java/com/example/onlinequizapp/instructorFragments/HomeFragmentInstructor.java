package com.example.onlinequizapp.instructorFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinequizapp.Lesson;
import com.example.onlinequizapp.MyAdapter;
import com.example.onlinequizapp.R;
import com.example.onlinequizapp.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragmentInstructor extends Fragment {

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
        View rootView = inflater.inflate(R.layout.fragment_home_instructor, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        referenceUsers = FirebaseDatabase.getInstance().getReference("Instructors");
        referenceLessons = FirebaseDatabase.getInstance().getReference("Lessons");
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
        Query query = referenceLessons.orderByChild("instructor").equalTo(fullnameTV.getText().toString());
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