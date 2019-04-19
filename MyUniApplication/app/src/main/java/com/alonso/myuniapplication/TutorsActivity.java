package com.alonso.myuniapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.alonso.myuniapplication.adapters.TutorsAdapter;
import com.alonso.myuniapplication.business.Subject;
import com.alonso.myuniapplication.business.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Iterator;

public class TutorsActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private DatabaseReference tutorsRef;


    private User user = new User();
    private RecyclerView recyclerView;
    private TutorsAdapter tutorsAdapter;
    private HashMap<Integer, Boolean> tutorSubjects = new HashMap<>();
    private HashMap<Integer, Boolean> previousTutorSubjects = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutors);

        Intent mIntent = getIntent();
        user = mIntent.getParcelableExtra("UserToTutor");

        firebaseFirestore = FirebaseFirestore.getInstance();
        tutorsRef = FirebaseDatabase.getInstance().getReference().child("Tutors");


        while (user.getUserName().equals("")) {
        }
        setData();
    }

    private void setData() {
        try {
            setTutoringSubjects();

            recyclerView = findViewById(R.id.tutors_recycler_view);
            tutorsAdapter = new TutorsAdapter(getApplicationContext(), user.getApprovedSubjects(), tutorSubjects);

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.addItemDecoration(new DividerItemDecoration(TutorsActivity.this, LinearLayoutManager.VERTICAL));
                    recyclerView.setAdapter(tutorsAdapter);

                    tutorsAdapter.notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            Log.e("TutorsActivity", "Error setting Data", e);
        }
    }

    private void setTutoringSubjects() {
//        mockTutorSubjects();
        for(Integer subjectCode : user.getTutorSubjectsKeys()){
            tutorSubjects.put(subjectCode, true);
            previousTutorSubjects.put(subjectCode, true);
        }
    }

    private void mockTutorSubjects() {
        tutorSubjects.put(user.getApprovedSubjects().get(0).getCode(),true);
        tutorSubjects.put(user.getApprovedSubjects().get(1).getCode(),true);
    }

    @Override
    public void onBackPressed() {
        updateUserTutoringSubjects();
        removeOldTutoringSubjects();
        finish();
    }

    private void removeOldTutoringSubjects() {
        for(Subject subject : user.getApprovedSubjects()){
            if( tutorSubjects.get(subject.getCode()) != null ){
                if(previousTutorSubjects.get(subject.getCode()) == null){
                    addNewTutoringSubject(subject.getCode());
                }
            } else {
                if(previousTutorSubjects.get(subject.getCode()) != null){
                    removeTutoringSubject(subject.getCode());
                }
            }
        }
    }
    private void removeTutoringSubject(int subjectCode) {
        tutorsRef.child(Integer.toString(subjectCode)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String email= "";
                    email = (String)snapshot.getValue();
                    if(email.equals(user.getEmail()))
                        snapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

//    private void removeTutoringSubject(int subjectCode) {
//        Query tutorsQuery = tutorsRef.child("Tutors").child(Integer.toString(subjectCode))
//                .orderByChild("email")
//                .equalTo(user.getEmail());
//
//        tutorsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
//                    appleSnapshot.getRef().removeValue();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.e("TutorsActivity", "Error deleting tutor", databaseError.toException());
//            }
//        });
//
//    }

    private void addNewTutoringSubject(int subjectCode) {
        final String key = tutorsRef.child("Tutors").push().getKey();
        tutorsRef.child(Integer.toString(subjectCode)).child(key).setValue(user.getEmail())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.i("TutorsActivity", "New tutor saved correctly");
                } else {
                    Log.e("TutorsActivity", "Error updating new tutor");

                }
            }
        });
    }

    private void updateUserTutoringSubjects() {
        //Clean old list
        user.getTutorSubjectsKeys().clear();
        for(Subject subject : user.getApprovedSubjects()){
            if( tutorSubjects.get(subject.getCode()) != null && tutorSubjects.get(subject.getCode()) ){
                user.getTutorSubjectsKeys().add(subject.getCode());
            }
        }
        updateUserFS();
        //fill new list
    }

    private void updateUserFS() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                firebaseFirestore.collection("users")
                        .document(user.getEmail())
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
}
