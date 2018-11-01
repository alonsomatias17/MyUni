package com.alonso.myuniapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.alonso.myuniapplication.business.Career;
import com.alonso.myuniapplication.business.Subject;
import com.alonso.myuniapplication.business.University;
import com.alonso.myuniapplication.business.User;
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

    private EditText careerDscET;
    private EditText universityET;

    private User user;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReferenceUsers;
    private FirebaseFirestore firebaseFirestore;


    List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile3);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReferenceUsers = FirebaseDatabase.getInstance().getReference("user");
        firebaseFirestore = FirebaseFirestore.getInstance();



//        userNameTV = (TextView)findViewById(R.id.userNameTV);
//        emailTV = findViewById(R.id.emailTV);
//        universityTV = findViewById(R.id.univercityTV);
//        careerTV = findViewById(R.id.careerTV);
//
//        careerET = findViewById(R.id.confirmationPasswordET);
//        universityET = findViewById(R.id.universityET);

        University university = getMockedUniversitiesAndSave();
        findUserByEmailFS();
//        User user = getUser();
//        University university = searchUnivercityByCareer(user.getCareer());

//        userNameTV.setText("hola");
      /*  emailTV.setText(user.getEmail());
        universityTV.setText(university.getName());
        universityET.setText(university.getDescription());
        careerTV.setText(user.getCareer().getName());
        careerET.setText(user.getCareer().getDescription());*/

    }

    private void findUserByEmail(){
        final FirebaseUser firebaseUser = getCurrentUser();
        Query query = databaseReferenceUsers.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                /*while (iterator.hasNext()) {
                    DataSnapshot next = (DataSnapshot) iterator.next();
                    String match = (String) next.child("description").getValue();
                    String key = next.getKey();
                    listKeys.add(key);
                    adapter.add(match);
                }*/
                if(iterator.hasNext()){
                    DataSnapshot next = (DataSnapshot) iterator.next();
                    user = next.getValue(User.class);

//                    userNameTV = findViewById(R.id.userNameTV);
//                    emailTV = findViewById(R.id.emailTV);
//                    universityTV = findViewById(R.id.univercityTV);
//                    careerTV = findViewById(R.id.careerTV);
//                    careerDscTV = findViewById(R.id.careerDscTV);
//                    universityET = findViewById(R.id.universityET);

//                    userNameTV.setText(user.getUserName());
//                    emailTV.setText(user.getEmail());
//                    universityTV.setText(university.getName());
//                    universityET.setText(user.getUserName());
//                    careerTV.setText(user.getCareer().getName());
//                    careerDscTV.setText("Hola");

                } else {
                    System.out.print("No hay data");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void findUserByEmailFS(){
        firebaseFirestore.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User user = document.toObject(User.class);
                                if(!user.getEmail().equals("")){
                                    userNameTV = findViewById(R.id.userNameTVProfile);
                                    emailTV = findViewById(R.id.emailTVProfile);
                                    careerTV = findViewById(R.id.careerTVProfile);
                                    careerDscET = findViewById(R.id.careerDscETProfile);

                                    userNameTV.setText(user.getUserName());
                                    emailTV.setText(user.getEmail());
                                    careerTV.setText(user.getCareer().getName());
                                    careerDscET.setText(user.getCareer().getDescription());
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

    private User getUser(){

        final FirebaseUser user = getCurrentUser();
        users = new ArrayList<>();
        databaseReferenceUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User userUni;
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    userUni = ds.getValue(User.class);
                    users.add(userUni);
                    System.out.println(userUni);
                    userNameTV = findViewById(R.id.userNameTVProfile);

                    userNameTV.setText(userUni.getUserName());
/*                    emailTV.setText(userUni.getEmail());
                    careerTV.setText(userUni.getCareer().getName());
                    careerET.setText(userUni.getCareer().getDescription());*/
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        System.out.print(users.toString());
        return new User();
    }

    private FirebaseUser getCurrentUser() {
        return  firebaseAuth.getCurrentUser();
    }
}
