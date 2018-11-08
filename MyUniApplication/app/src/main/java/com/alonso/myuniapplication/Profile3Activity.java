package com.alonso.myuniapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alonso.myuniapplication.business.Career;
import com.alonso.myuniapplication.business.Subject;
import com.alonso.myuniapplication.business.University;
import com.alonso.myuniapplication.business.User;
import com.alonso.myuniapplication.business.UserDTO;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Profile3Activity extends AppCompatActivity {

    private TextView userNameTV;
    private TextView emailTV;
    private TextView universityTV;
    private TextView careerTV;
    private TextView universityDscTV;
    private TextView careerDscTV;

    private University university;
    private User user;
    private ProgressBar progressBar;
    private int progressBarStatus = 0;
    private UserDTO userDTO;

    FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private static final int PBSTATUS_LOAD_FINISHED = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile3);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = getCurrentUser();
        progressBar = findViewById(R.id.progressBarProfile);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressBarStatus < PBSTATUS_LOAD_FINISHED) {
                }
                progressBar.setVisibility(View.INVISIBLE);
            }
        }).start();

        Intent mIntent = getIntent();
//
//        Bundle bundle = getIntent().getExtras();
//        userDTO = bundle.getParcelable("User");
//
        user = mIntent.getParcelableExtra("User");
        System.out.println("Profile3Activity. User info: " + user.getUserName() + " " + user.getEmail());

        findUserByEmailFS();
        getUniversityFS();
    }


    private void findUserByEmailFS(){
        firebaseFirestore.collection("users")
                .whereEqualTo("email", firebaseUser.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                user = document.toObject(User.class);
                                if(!user.getEmail().equals("")){
                                    userNameTV = findViewById(R.id.userNameTVProfile);
                                    emailTV = findViewById(R.id.emailTVProfile);
                                    careerTV = findViewById(R.id.careerTVProfile);
//                                    careerDscET = findViewById(R.id.careerDscETProfile);
                                    careerDscTV = findViewById(R.id.careerDscProfileTV);


                                    userNameTV.setText(user.getUserName());
                                    emailTV.setText(user.getEmail());
                                    careerTV.setText(user.getCareer().getName());
//                                    careerDscET.setText(user.getCareer().getDescription());
                                    careerDscTV.setText(user.getCareer().getDescription());
                                    progressBarStatus++;
                                }

                                Log.d("findUserByEmailFS", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w("findUserByEmailFS", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void getUniversityFS(){
        firebaseFirestore.collection("universities")
                .whereEqualTo("name", "Universidad de Morón")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                university = document.toObject(University.class);
                                if(!university.getName().equals("")){
                                    universityTV = findViewById(R.id.universityTVProfile);
                                    universityDscTV = findViewById(R.id.universityDscProfileTV);

                                    universityTV.setText(university.getName());
                                    universityDscTV.setText(university.getDescription());
                                    progressBarStatus++;
                                }

                                Log.d("findUserByEmailFS", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w("findUserByEmailFS", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    @NonNull
    private University getMockedUniversitiesAndSave() {
        University university = new University(1,"UM", "Morón");

        university.getCareers().add(new Career(10, "Ing. Informática", "Morón"));
        university.getCareers().add(new Career(20, "Contador Público", "San Justo"));
        university.getCareers().add(new Career(30, "Profesorado de Educación Física", "CABA"));

        university.getCareers().get(0).getSubjects().add(new Subject(100, "Análisis I", "Análisis I", 1));
        university.getCareers().get(0).getSubjects().add(new Subject(200, "Circuitos y Sistemas Digitales", "Circuitos y Sistemas Digitales", 1));
        university.getCareers().get(0).getSubjects().add(new Subject(300, "Probabilidad y Estadística", "Probabilidad y Estadística", 2));
        university.getCareers().get(0).getSubjects().add(new Subject(400, "Análisis II", "Análisis II", 2));
        university.getCareers().get(0).getSubjects().add(new Subject(500, "Aquitecura del Computador", "Aquitecura del Computador", 2));
        university.getCareers().get(0).getSubjects().add(new Subject(600, "Física I", "Física", 1));
        university.getCareers().get(0).getSubjects().add(new Subject(700, "Trabajo de Campo", "Trabajo de Campo", 3));
        university.getCareers().get(0).getSubjects().add(new Subject(800, "Análisis de Sistemas", "Análisis de Sistemas", 2));
        university.getCareers().get(0).getSubjects().add(new Subject(900, "Diseño de aplicaciones", "Diseño de aplicaciones", 3));


        //saveUniversity(university);
        return university;
    }

    private FirebaseUser getCurrentUser() {
        return  firebaseAuth.getCurrentUser();
    }
}
