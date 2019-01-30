package com.alonso.myuniapplication;

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

import com.alonso.myuniapplication.adapters.OnGoingSubjectsAdapter;
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

import java.util.ArrayList;
import java.util.List;

public class LiveSubjectsActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;

    private User user = new User();
    private OnGoingSubjectsAdapter onGoingSubjectsAdapter;
    private University university = new University();
    private Career career;
    private List<Subject> completedSubjects;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private int progressBarStatus = 0;
    private static final int PB_STATUS_LOAD_FINISHED = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_subjects);

        firebaseFirestore = FirebaseFirestore.getInstance();
        getUniversityFS();

        Intent mIntent = getIntent();
        user = mIntent.getParcelableExtra("UserToOSubject");
        progressBar = findViewById(R.id.live_subject_progress_bar);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (user.getUserName().equals("")) {
                }
                progressBarStatus++;

                while (progressBarStatus < PB_STATUS_LOAD_FINISHED) {
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

            recyclerView = findViewById(R.id.live_subject_recycler_view);
            onGoingSubjectsAdapter = new OnGoingSubjectsAdapter(getApplicationContext(), completedSubjects);

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.addItemDecoration(new DividerItemDecoration(LiveSubjectsActivity.this, LinearLayoutManager.VERTICAL));
                    recyclerView.setAdapter(onGoingSubjectsAdapter);

                    onGoingSubjectsAdapter.notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            Log.e("ApprovedSubjectsAct", "Error setting Data", e);
        }
    }

    @Override
    public void onBackPressed() {
        updateUserApprovedSubjects();
        finish();
    }

    private void updateUserApprovedSubjects() {
        updateUserLiveSubjectList();
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
                                Log.i("OnGoingSubjectsAct", "User updated correctly");

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("OnGoingSubjectsAct", "Error updating user", e);
                            }
                        });
            }
        }).start();
    }

    private void updateUserLiveSubjectList() {
        user.getOnGoingSubjects().clear();
        for(Subject subject : completedSubjects){
             if(subject.isOnGoing())
                user.getOnGoingSubjects().add(subject);
        }
    }

    private void setSubjectsState() {
        completedSubjects = career.getSubjects();
        List<Subject> subjectsToRemove = new ArrayList<>();
        for(Subject subject : completedSubjects){
            for (Subject userSubject: user.getApprovedSubjects()){
                if(subject.getCode() == userSubject.getCode())
                    subjectsToRemove.add(subject);
            }
            for (Subject userSubject: user.getOnGoingSubjects()){
                if(subject.getCode() == userSubject.getCode())
                    subject.setState(userSubject.getState());
            }
        }
        completedSubjects.removeAll(subjectsToRemove);
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
