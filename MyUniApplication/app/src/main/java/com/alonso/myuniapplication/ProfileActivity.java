package com.alonso.myuniapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alonso.myuniapplication.business.Career;
import com.alonso.myuniapplication.business.Subject;
import com.alonso.myuniapplication.business.University;
import com.alonso.myuniapplication.business.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private TextView userNameET;
    private TextView emailET;
    private TextView universityTV;
    private TextView careerTV;

    private EditText careerET;
    private EditText universityET;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReferenceUsers;

    List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReferenceUsers = FirebaseDatabase.getInstance().getReference("user");


        userNameET = findViewById(R.id.userNameTV);
        emailET = findViewById(R.id.emailTV);
        universityTV = findViewById(R.id.univercityTV);
        careerTV = findViewById(R.id.careerTV);

        careerET = findViewById(R.id.confirmationPasswordET);
        universityET = findViewById(R.id.universityET);

        University university = getMockedUniversitiesAndSave();
        User user = getUser();
//        University university = searchUnivercityByCareer(user.getCareer());

        userNameET.setText(user.getUserName());
        emailET.setText(user.getEmail());
        universityTV.setText(university.getName());
        universityET.setText(university.getDescription());
        careerTV.setText(user.getCareer().getName());
        careerET.setText(user.getCareer().getDescription());

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

    private User getUser(){

        FirebaseUser user = getCurrentUser();
        users = new ArrayList<>();
        databaseReferenceUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user;
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    user = ds.getValue(User.class);
                    users.add(ds.getValue(User.class));
                    System.out.println(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        return users.get(0);
    }

    private FirebaseUser getCurrentUser() {
        return  firebaseAuth.getCurrentUser();
    }
}
