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
    private User user = new User();
    private ProgressBar progressBar;
    private int progressBarStatus = 0;

    private FirebaseFirestore firebaseFirestore;

    private static final int PBSTATUS_LOAD_FINISHED = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile3);

        firebaseFirestore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBarProfile);

        Intent mIntent = getIntent();
        user = mIntent.getParcelableExtra("User");

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressBarStatus < PBSTATUS_LOAD_FINISHED && user.getUserName().equals("")) {
                }
                setUserProfile();
                progressBar.setVisibility(View.INVISIBLE);
            }
        }).start();

        getUniversityFS();
    }

    private void setUserProfile() {
        userNameTV = findViewById(R.id.userNameTVProfile);
        emailTV = findViewById(R.id.emailTVProfile);
        careerTV = findViewById(R.id.careerTVProfile);
        careerDscTV = findViewById(R.id.careerDscProfileTV);

        userNameTV.setText(user.getUserName());
        emailTV.setText(user.getEmail());
        careerTV.setText(user.getCareer().getName());
        careerDscTV.setText(user.getCareer().getDescription());
        progressBarStatus++;
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
}
