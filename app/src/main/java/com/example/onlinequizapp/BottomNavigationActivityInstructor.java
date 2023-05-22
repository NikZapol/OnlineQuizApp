package com.example.onlinequizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.onlinequizapp.instructorFragments.ChatFragmentInstructor;
import com.example.onlinequizapp.instructorFragments.HomeFragmentInstructor;
import com.example.onlinequizapp.instructorFragments.ProfileFragmentInstructor;
import com.example.onlinequizapp.instructorFragments.RecordsFragmentInstructor;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class BottomNavigationActivityInstructor extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;
    HomeFragmentInstructor homeFragmentInstructor = new HomeFragmentInstructor();
    ProfileFragmentInstructor profileFragmentInstructor = new ProfileFragmentInstructor();
    ChatFragmentInstructor chatFragmentInstructor = new ChatFragmentInstructor();
    RecordsFragmentInstructor recordsFragmentInstructor = new RecordsFragmentInstructor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation_instructor);
        bottomNavigationView = findViewById(R.id.bottom_navigation_instructor);
        bottomNavigationView.setSelectedItemId(R.id.homeButton);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragmentInstructor).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){

                    case R.id.homeButton:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragmentInstructor).commit();
                        return true;
                    case R.id.profileButton:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,profileFragmentInstructor).commit();
                        return true;
                    case R.id.recordsButton:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,recordsFragmentInstructor).commit();
                        return true;
                    case R.id.chatButton:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,chatFragmentInstructor).commit();
                        return true;
                }

                return false;
            }
        });
    }

}