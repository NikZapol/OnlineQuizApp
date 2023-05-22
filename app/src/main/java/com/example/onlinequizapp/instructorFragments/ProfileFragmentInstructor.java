package com.example.onlinequizapp.instructorFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinequizapp.R;
import com.example.onlinequizapp.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragmentInstructor extends Fragment {

    FirebaseUser user;
    DatabaseReference reference;
    String userID;
    TextView fullnameTV, emailTV, greetingsTV;
    ImageView userImage;
    FirebaseAuth mAuth;
    RelativeLayout logOutLayout, logOutLayoutAgain;
    Button logOutButtonAgain, logOutButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_instructor, container, false);
        mAuth=FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Instructors");
        userID = user.getUid();
        fullnameTV = rootView.findViewById(R.id.fullnameUser);
        emailTV = rootView.findViewById(R.id.emailUser);
        greetingsTV = rootView.findViewById(R.id.greetingsTitle);
        logOutButton = rootView.findViewById(R.id.logOutButton);
        logOutButtonAgain = rootView.findViewById(R.id.logOutButtonAgain);
        logOutLayout = rootView.findViewById(R.id.logOutLayout);
        logOutLayoutAgain = rootView.findViewById(R.id.logOutLayoutAgain);


        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot Snapshot) {
                User userProfile = Snapshot.getValue(User.class);
                if(userProfile != null){
                    String fullname = userProfile.fullName;
                    String email = userProfile.email;



                    greetingsTV.setText("Здравствуйте, " + fullname + "!");
                    fullnameTV.setText(fullname);
                    emailTV.setText(email);
                    //Picasso.get().load(getImageLink).into(userImage);
                    //userImage.setClipToOutline(true);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Failed to get data from firebase", Toast.LENGTH_SHORT).show();

            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutLayout.setBackgroundResource(R.drawable.round_back_selected_option_red);
                logOutLayoutAgain.setVisibility(View.VISIBLE);
            }
        });
        logOutButtonAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutLayoutAgain.setBackgroundResource(R.drawable.round_back_selected_option_red);
                mAuth.signOut();
                Intent intent = new Intent(getActivity(), com.example.onlinequizapp.LoginActivity.class);
                startActivity(intent);


            }
        });



        return rootView;
    }
}