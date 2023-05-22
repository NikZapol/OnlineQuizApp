package com.example.onlinequizapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;



import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import okhttp3.internal.cache.DiskLruCache;


public class SettingsFragment extends Fragment {

    private BottomNavigationView bottomNav;

    public SettingsFragment() {
        // Required empty public constructor
    }
    String[] items = {"8:30-10:30", "11:45-13:45", "14:00-16:00", "16:15-18:15", "18:30-20:30"};
    String fullname, lessonid, userId, instructor, date, time;
    EditText dateET, fullnameET, instructorET, useridET, lessonidET, lessonUIslotET, keyET;
    AutoCompleteTextView timeET;
    FirebaseUser user;
    RelativeLayout recordbtnLayout, recordbtnLayoutAgain;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    DatabaseReference referenceUsers = FirebaseDatabase.getInstance().getReference("Users");
    DatabaseReference referenceLessons = FirebaseDatabase.getInstance().getReference("Lessons");
    DatabaseReference referenceLessonsSecondRecord = FirebaseDatabase.getInstance().getReference("Lessons2");
    Button recordbtn, recordbtnAgain;
    ProgressBar progressBar;
    int Count=0, maxrecords=0;
    String userID, lessonuislot = "0";
    String recordAlreadyHaveSlot1 = "0", recordAlreadyHaveSlot2 = "0", recordAlreadyHaveSlot3 = "0", recordSuccess = "0", instructorAlreadyHaveRecord = "0", emptyFields = "0", recordExist = "0";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        //инициализация базы данных
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        //задание полей с разметки
        bottomNav = getActivity().findViewById(R.id.bottom_navigation);
        dateET = rootView.findViewById(R.id.dateET);
        fullnameET = rootView.findViewById(R.id.fullname);
        instructorET = rootView.findViewById(R.id.instructor);
        useridET = rootView.findViewById(R.id.userid);
        lessonUIslotET = rootView.findViewById(R.id.lessonUIslotET);
        lessonidET = rootView.findViewById(R.id.lessonidET);
        progressBar = rootView.findViewById(R.id.progressbar);
        recordbtnAgain = rootView.findViewById(R.id.recordbtnAgain);
        recordbtn = rootView.findViewById(R.id.recordbtn);
        recordbtnLayout = rootView.findViewById(R.id.recordbtnLayout);
        recordbtnLayoutAgain = rootView.findViewById(R.id.recordbtnLayoutAgain);
        timeET = rootView.findViewById(R.id.timeET);


        //получение информации о пользователе из базы данных и заполнение разметки данными
        referenceUsers.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User userLesson = dataSnapshot.getValue(User.class);
                if (userLesson != null) {
                    fullname = userLesson.fullName;
                    lessonid = referenceLessons.push().getKey();
                    userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    instructor = dataSnapshot.child("instructor").getValue(String.class);
                    fullnameET.setText(fullname);
                    instructorET.setText(instructor);
                    useridET.setText(userId);
                    lessonidET.setText(lessonid);
                    lessonUIslotET.setText("0");
                    dateET.setText("");
                    timeET.setText("");
                    checkAlreadyHaveRecord2();
                    checkInstructorRecord2();
                    //выпадающий список
                    ArrayAdapter<String> adapterItems = new ArrayAdapter<String>(rootView.getContext(), R.layout.dropdown_item, items);
                    timeET.setAdapter(adapterItems);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Failed to get data from firebase", Toast.LENGTH_SHORT).show();
            }
        });


        //присвоение метода на кнопку выбора даты(открытие календаря)
        dateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int day) {
                        dateET.setText(day + "-" + (monthOfYear + 1) + "-" + year);
                    }
                },
                        year, month, day);
                datePickerDialog.show();

            }
        });


        //присвоение метода нажатия кнопки для повторного нажатия записаться
        recordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordbtnLayout.setBackgroundResource(R.drawable.round_back_selected_option_red);
                final long changeTime = 150L;
                recordbtnLayout.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        recordbtnLayout.setVisibility(View.GONE);
                        recordbtnLayoutAgain.setVisibility(View.VISIBLE);
                        //checkAlreadyHaveRecord2();

                    }
                }, changeTime);
            }
        });





        //присвоение метода повторного нажатия кнопки записаться
        recordbtnAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected date from the date EditText
                String selectedDate = dateET.getText().toString();

                // Get the current date
                Calendar currentDate = Calendar.getInstance();
                long currentTimeMillis = currentDate.getTimeInMillis();

                // Parse the selected date into Calendar object
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                try {
                    Date parsedDate = sdf.parse(selectedDate);
                    Calendar selectedDateCalendar = Calendar.getInstance();
                    selectedDateCalendar.setTime(parsedDate);

                    // Check if the selected date is in the past
                    if (selectedDateCalendar.before(currentDate)) {
                        // Selected date is in the past
                        Toast.makeText(getActivity(), "Выберите текущую или будущую дату", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Calculate the difference between selected date and current date in milliseconds
                    long selectedTimeMillis = selectedDateCalendar.getTimeInMillis();
                    long differenceMillis = selectedTimeMillis - currentTimeMillis;

                    // Calculate the difference in days
                    int differenceDays = (int) (differenceMillis / (24 * 60 * 60 * 1000));

                    // Check if the difference is more than 7 days
                    if (differenceDays > 7) {
                        // Selected date is more than 1 week away from the current date
                        Toast.makeText(getActivity(), "Выберите дату, не более чем на 1 неделю вперед", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    // Handle the parse exception, if needed
                }

                progressBar.setVisibility(View.VISIBLE);
                instructorAlreadyHaveRecord = "0";
                checkEmpty();
                Count = 0;

                if (emptyFields == "0") {
                    checkAlreadyHaveRecord2();

                    if (maxrecords==1) {
                        // user already has 3 records
                        Toast.makeText(getActivity(), "У вас максимум записей", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    } else {
                        // user has less than 3 records
                        checkInstructorRecord2();
                        if (instructorAlreadyHaveRecord == "0") {
                            lessonUIslotET.setText(Integer.toString(Count + 1));
                            writeNewLesson();
                        }
                    }
                }
            }
        });

        return rootView;
    }


    public void checkAlreadyHaveRecord2() {
        Query query = referenceLessons.orderByChild("userId").equalTo(userID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int count = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String lessonUIslot = snapshot.child("lessonUIslot").getValue(String.class);
                        if (lessonUIslot.equals("1") || lessonUIslot.equals("2") || lessonUIslot.equals("3")) {
                            count++;
                            Count = count;
                            Log.d("count", String.valueOf(Count));
                            if (count>=3) maxrecords=1;

                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {// Handle error
            }
        });
    }




    public void checkInstructorRecord2() {
        referenceLessons.orderByChild("date").equalTo(dateET.getText().toString())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                if (childSnapshot.child("time").getValue().toString().equals(timeET.getText().toString()) &&
                                        childSnapshot.child("instructor").getValue().toString().equals(instructorET.getText().toString())) {
                                    instructorAlreadyHaveRecord = "1";
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {// Handle error
                    }
                });
    }



    //функция для проверки пустоты
    public void checkEmpty() {
        if (dateET.getText().toString().isEmpty() && timeET.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Вы ничего не выбрали", Toast.LENGTH_SHORT).show();
            emptyFields = "1";
        } else if (timeET.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Вы не выбрали время", Toast.LENGTH_SHORT).show();
            emptyFields = "1";
        } else if (dateET.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Вы не выбрали дату", Toast.LENGTH_SHORT).show();
            emptyFields = "1";
        } else emptyFields = "0";
    }

    public void navigateToRecordsFragment() {
        bottomNav.setSelectedItemId(R.id.recordsButton);
    }


    //создание функции записи уроки
    public void writeNewLesson() {
        //инициализация класса, заполнение аттрибутов класса - данными из разметки
        Lesson lesson = new Lesson(
                timeET.getText().toString(),
                dateET.getText().toString(),
                fullnameET.getText().toString(),
                instructorET.getText().toString(),

                lessonidET.getText().toString(),//pushkey урока
                useridET.getText().toString(),//id пользователя
                lessonUIslotET.getText().toString());//counter для вычисления максимальных 3 записей

        //проверка на отправку данных
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (instructorAlreadyHaveRecord == "0") {
                    mDatabase.child("Lessons").child(lesson.getLessonId()).setValue(lesson);
                    recordSuccess = "1";
                    Toast.makeText(getActivity(), "Успешная запись", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    navigateToRecordsFragment();
                    dateET.setText("");
                    timeET.setText("");
                    //resetAlreadyHaveSlots();
                    //checkAlreadyHaveRecord();

                } else {
                    Toast.makeText(getActivity(), "У вашего инструктора уже есть запись на этот день на это время", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //произошла сетевая ошибка отправления данных
                Toast.makeText(getActivity(), "Произошла ошибка сети", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

}

/*  старый не рациональный  //метод для проверки имеющейся записи
    public void checkAlreadyHaveRecord() {
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
                                                if (snapshot.getValue().toString().equals("1")) {
                                                    recordAlreadyHaveSlot1 = "1";
                                                    //Toast.makeText(getActivity(), "recordAlreadyHaveSlot1=1", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    recordAlreadyHaveSlot1 = "0";
                                                    //проверка существования записи на втором слоте
                                                    referenceLessons.child(pushkey).addChildEventListener(new ChildEventListener() {
                                                        @Override
                                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                            if (snapshot.getKey().equals("lessonUIslot")) {
                                                                if (snapshot.getValue().toString().equals("2")) {
                                                                    recordAlreadyHaveSlot2 = "2";
                                                                    //Toast.makeText(getActivity(), "recordAlreadyHaveSlot2=2", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    recordAlreadyHaveSlot2 = "0";
                                                                    //проверка существования записи на третьем слоте
                                                                    referenceLessons.child(pushkey).addChildEventListener(new ChildEventListener() {
                                                                        @Override
                                                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                                            if (snapshot.getKey().equals("lessonUIslot")) {
                                                                                if (snapshot.getValue().toString().equals("3")) {
                                                                                    recordAlreadyHaveSlot3 = "3";
                                                                                    //Toast.makeText(getActivity(), "recordAlreadyHaveSlot3=3", Toast.LENGTH_SHORT).show();

                                                                                } else {
                                                                                    recordAlreadyHaveSlot3 = "0";
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
                                } else {
                                    recordAlreadyHaveSlot2 = "0";
                                    recordAlreadyHaveSlot1 = "0";
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
                } else {
                    recordAlreadyHaveSlot2 = "0";
                    recordAlreadyHaveSlot1 = "0";
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


/*старый не рациональный //метод для проверки записи на это же время и на этот же день у инструктора

    public void checkInstructorRecord() {
        if (recordSuccess == "0") {
            referenceLessons.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot.exists()) {
                        String pushkey = dataSnapshot.getKey();
                        referenceLessons.child(pushkey).addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                                if (dataSnapshot.exists()) {
                                    if (dataSnapshot.getKey().equals("date")) {
                                        if (dataSnapshot.getValue().toString().equals(dateET.getText().toString())) {
                                            //Toast.makeText(getActivity(), "Дата совпадает", Toast.LENGTH_SHORT).show();
                                            referenceLessons.child(pushkey).addChildEventListener(new ChildEventListener() {
                                                @Override
                                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                    if (snapshot.getKey().equals("time")) {
                                                        if (snapshot.getValue().toString().equals(timeET.getText().toString())) {
                                                            //Toast.makeText(getActivity(), "Время совпадает", Toast.LENGTH_SHORT).show();
                                                            referenceLessons.child(pushkey).addChildEventListener(new ChildEventListener() {
                                                                @Override
                                                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                                    if (snapshot.getKey().equals("instructor")) {
                                                                        if (snapshot.getValue().toString().equals(instructorET.getText().toString())) {
                                                                            instructorAlreadyHaveRecord = "1";
                                                                            //Toast.makeText(getActivity(), "У вашего инструктора уже есть запись на этот день на это время", Toast.LENGTH_LONG).show();
                                                                        }
                                                                    }
                                                                }

                                                                @Override
                                                                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                                }

                                                                @Override
                                                                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                                                                }

                                                                @Override
                                                                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {
                                                                }
                                                            });
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                }

                                                @Override
                                                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                                                }

                                                @Override
                                                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                }
                                            });
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot snapshot, @androidx.annotation.Nullable String previousChildName) {
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @androidx.annotation.Nullable String previousChildName) {
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }*/

/*старый не рациональный//присвоение метода нажатия кнопки записаться
        recordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                instructorAlreadyHaveRecord="0";
                checkEmpty();
                //checkInstructorRecord();
                if (emptyFields=="0") {
                    checkAlreadyHaveRecord();
                    referenceLessons.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot Snapshot) {
                            String pushkey = Snapshot.getKey();
                            referenceLessons.child(pushkey).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Lesson lesson = Snapshot.getValue(Lesson.class);
                                    if(lesson != null){
                                                if(lesson.lessonUIslot=="0"){
                                                    checkInstructorRecord();
                                                    if (instructorAlreadyHaveRecord=="0"){
                                                        lessonUIslotET.setText("1");
                                                        writeNewLesson();
                                                    }
                                                } else if (lesson.lessonUIslot=="1"){
                                                    checkInstructorRecord();
                                                    if (instructorAlreadyHaveRecord=="0"){
                                                        lessonUIslotET.setText("2");
                                                        writeNewLesson();
                                                    }
                                                } else if (lesson.lessonUIslot=="2"){
                                                    Toast.makeText(getActivity(), "У вас максимум записей", Toast.LENGTH_SHORT).show();
                                                }
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
        });

 */
