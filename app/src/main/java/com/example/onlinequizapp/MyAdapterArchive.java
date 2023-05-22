package com.example.onlinequizapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyAdapterArchive extends RecyclerView.Adapter<MyAdapterArchive.MyViewHolder> {
    Context context;
    ArrayList<Lesson> list;
    DatabaseReference reference, referenceUsers, referenceInstructors;

    public MyAdapterArchive(Context context, ArrayList<Lesson> list) {
        this.context = context;
        this.list = list;
        this.reference = FirebaseDatabase.getInstance().getReference().child("Lessons_archive");
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_view,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Lesson lesson = list.get(position);
        holder.instructorTV.setText(lesson.getInstructor());
        holder.fullnameTV.setText(lesson.getFullname());
        holder.dateTV.setText(lesson.getDate());
        holder.timeTV.setText(lesson.getTime());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }







    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView instructorTV, dateTV, timeTV,fullnameTV,instructorUser;
        String userID;
        FirebaseUser user;
        DatabaseReference reference, referenceLessonsArchive;
        RelativeLayout deleteRecordLayout, deleteRecordLayoutAgain;
        Button deleteRecordButtonAgain, deleteRecordButton;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            user = FirebaseAuth.getInstance().getCurrentUser();
            reference = FirebaseDatabase.getInstance().getReference("Lessons");
            referenceUsers = FirebaseDatabase.getInstance().getReference("Users");
            referenceInstructors = FirebaseDatabase.getInstance().getReference("Instructors");
            instructorTV = itemView.findViewById(R.id.instructorUser);
            instructorUser = itemView.findViewById(R.id.instructorTitle);
            userID = user.getUid();
            dateTV = itemView.findViewById(R.id.dateUser);
            timeTV = itemView.findViewById(R.id.timeUser);
            fullnameTV= itemView.findViewById(R.id.fullnameUser);
            deleteRecordButton = itemView.findViewById(R.id.deleteRecordButton);
            deleteRecordButtonAgain = itemView.findViewById(R.id.deleteRecordButtonAgain);
            deleteRecordLayout = itemView.findViewById(R.id.deleteRecordLayout);
            deleteRecordLayoutAgain = itemView.findViewById(R.id.deleteRecordLayoutAgain);
            reference = FirebaseDatabase.getInstance().getReference();




            referenceInstructors.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot Snapshot) {
                    User userProfile = Snapshot.getValue(User.class);
                    if(userProfile != null){
                        String fullname = userProfile.fullName;
                        if(fullname == instructorTV.getText().toString());
                        //Log.d("MyAdapter", "fullname: " + fullname);
                        //Log.d("MyAdapter", "instructorTV: " + instructorTV.getText().toString());

                        deleteRecordButton.setText("Потвердить заявку на отмену записи");


                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {


                }
            });
            deleteRecordButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteRecordLayout.setBackgroundResource(R.drawable.round_back_selected_option_red);
                    final long changeTime = 150L;
                    deleteRecordButton.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            deleteRecordLayout.setVisibility(View.GONE);
                            deleteRecordLayoutAgain.setVisibility(View.VISIBLE);
                            deleteRecordButtonAgain.setText("Вы уверены что хотите потвердить заявку на отмену записи?");
                        }
                    }, changeTime);
                }
            });
            deleteRecordButtonAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteRecordLayoutAgain.setBackgroundResource(R.drawable.round_back_selected_option_red);
                    final long changeTime = 150L;
                    deleteRecordButton.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            deleteRecordLayoutAgain.setVisibility(View.GONE);
                        }
                    }, changeTime);

                    int position = getAdapterPosition();
                    String key = list.get(position).getLessonId();

                    // Remove the lesson from "lessons" reference
                    reference.child("Lessons_archive").child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("RemoveLesson", "Lesson removed successfully");
                            } else {
                                Log.d("RemoveLesson", "Failed to remove lesson: " + task.getException().getMessage());
                            }
                        }
                    });

                    list.remove(position);
                    notifyItemRemoved(position);
                }
            });
        }

    }
}

//-----------------------------------------------------------------------
        /*







        */
