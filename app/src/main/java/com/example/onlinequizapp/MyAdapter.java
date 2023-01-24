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
    DatabaseReference reference;

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
        holder.dateTV.setText(lesson.getDate());
        holder.timeTV.setText(lesson.getTime());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }






    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView instructorTV, dateTV, timeTV;


        RelativeLayout deleteRecordLayout, deleteRecordLayoutAgain;
        Button deleteRecordButtonAgain, deleteRecordButton;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            instructorTV = itemView.findViewById(R.id.instructorUser);
            dateTV = itemView.findViewById(R.id.dateUser);
            timeTV = itemView.findViewById(R.id.timeUser);
            deleteRecordButton = itemView.findViewById(R.id.deleteRecordButton);
            deleteRecordButtonAgain = itemView.findViewById(R.id.deleteRecordButtonAgain);
            deleteRecordLayout = itemView.findViewById(R.id.deleteRecordLayout);
            deleteRecordLayoutAgain = itemView.findViewById(R.id.deleteRecordLayoutAgain);
            reference = FirebaseDatabase.getInstance().getReference();
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

                    reference.child("lessons").child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("key", String.valueOf(task));
                            } else {
                                Log.d("key", String.valueOf(task));
                            }
                        }
                    });
                    Log.d("key", key);
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
