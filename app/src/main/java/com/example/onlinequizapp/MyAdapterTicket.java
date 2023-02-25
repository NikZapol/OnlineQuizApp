package com.example.onlinequizapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyAdapterTicket extends RecyclerView.Adapter<MyAdapterTicket.MyViewHolder> {
    Context context;
    ArrayList<Ticket> list;
    DatabaseReference reference;

    public MyAdapterTicket(Context context, ArrayList<Ticket> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view_ticket, parent, false);
        return new MyViewHolder(itemView, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Ticket ticket = list.get(position);
        holder.buttonticket.setText(ticket.getTicketid());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }






    public class MyViewHolder extends RecyclerView.ViewHolder {

        Button buttonticket;
        Context context;



        public MyViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            buttonticket = itemView.findViewById(R.id.buttonticket);
            reference = FirebaseDatabase.getInstance().getReference();
            buttonticket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, com.example.onlinequizapp.StartActivity.class);
                    if(buttonticket.getText().toString() == "Билет 1"){
                        intent.putExtra("message", "ticket1");
                    }
                    if(buttonticket.getText().toString() == "Билет 2"){
                        intent.putExtra("message", "ticket2");
                    }

                    context.startActivity(intent);
                }
            });

        }

    }
}


