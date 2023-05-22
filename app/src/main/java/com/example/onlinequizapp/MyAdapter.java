package com.example.onlinequizapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    Context context;
    ArrayList<Lesson> list;
    DatabaseReference reference, referenceUsers, referenceInstructors;

    public MyAdapter(Context context, ArrayList<Lesson> list) {
        this.context = context;
        this.list = list;
        this.reference = FirebaseDatabase.getInstance().getReference();
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
        DatabaseReference reference;
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
                        instructorTV.setVisibility(View.GONE);
                        instructorUser.setVisibility(View.GONE);
                        deleteRecordButton.setVisibility(View.GONE);

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

                    // Get the lesson data from "lessons" reference
                    reference.child("Lessons").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // Copy the lesson data to "lessons_archive" reference
                            reference.child("Lessons_archive").child(key).setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    if (error != null) {
                                        Log.d("CopyLesson", "Failed to copy lesson: " + error.getMessage());
                                    } else {
                                        Log.d("CopyLesson", "Lesson copied successfully");
                                    }
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d("CopyLesson", "Failed to read lesson data: " + error.getMessage());
                        }
                    });

                    // Remove the lesson from "lessons" reference
                    reference.child("Lessons").child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
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
