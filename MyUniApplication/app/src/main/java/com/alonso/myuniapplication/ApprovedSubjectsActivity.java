package com.alonso.myuniapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.alonso.myuniapplication.adapters.UserAdapter;
import com.alonso.myuniapplication.business.Career;
import com.alonso.myuniapplication.business.Subject;
import com.alonso.myuniapplication.business.University;
import com.alonso.myuniapplication.business.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApprovedSubjectsActivity extends AppCompatActivity {

    private User user = new User();
    private UserAdapter mAdapter;
    private University university = new University();
    private Career career;
    List<Subject> completedSubjects;
    private boolean hasBackPressed = false;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private int progressBarStatus = 0;
    private static final int PBSTATUS_LOAD_FINISHED = 2;


    private FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved_subjects);
        firebaseFirestore = FirebaseFirestore.getInstance();
        getUniversityFS();

        Intent mIntent = getIntent();
        user = mIntent.getParcelableExtra("UserToApSubject");

        progressBar = findViewById(R.id.progressBarAppSubject);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (user.getUserName().equals("")) {
                }
                progressBarStatus++;

                while (progressBarStatus < PBSTATUS_LOAD_FINISHED) {
                }
                setData();
                progressBar.setVisibility(View.INVISIBLE);
            }
        }).start();
    }

    private void setData() {

        try {
            career = university.findCareer(user.getCareer().getCode());
            setSubjectsState();

            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            mAdapter = new UserAdapter(getApplicationContext(), completedSubjects);

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.addItemDecoration(new DividerItemDecoration(ApprovedSubjectsActivity.this, LinearLayoutManager.VERTICAL));
                    recyclerView.setAdapter(mAdapter);

                    mAdapter.notifyDataSetChanged();
                }
            });

        } catch (Exception e) {
            Log.e("ApprovedSubjectsAct", "Error setting Data", e);
        }

    }

    private void setSubjectsState() {
        completedSubjects = career.getSubjects();
        for(Subject subject : completedSubjects){
            for (Subject userSubject: user.getApprovedSubjects()){
                if(subject.getCode() == userSubject.getCode())
                    subject.setState(userSubject.getState());
            }
        }
    }

    @Override
    public void onBackPressed() {
        updateUserApprovedSubjects();
        finish();
    }

    private void updateUserApprovedSubjects() {
        updateUserApprovedSubjectList();
        updateUserFS();
    }

    private void updateUserFS() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                firebaseFirestore.collection("users")
                        .document("3SdWS6L3Tc9E1HPrp3PH")
                        .set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.i("ApprovedSubjectsAct", "User updated correctly");

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("ApprovedSubjectsAct", "Error updating user", e);
                            }
                        });
            }
        }).start();
    }

    private void updateUserApprovedSubjectList() {
        user.getApprovedSubjects().clear();
        for(Subject subject : completedSubjects){
            if(subject.isApproved())
                user.getApprovedSubjects().add(subject);
        }
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
                                progressBarStatus++;
                                Log.d("findUserByEmailFS", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w("findUserByEmailFS", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

}
