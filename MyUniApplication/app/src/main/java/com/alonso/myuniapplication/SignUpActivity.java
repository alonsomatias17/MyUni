package com.alonso.myuniapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.alonso.myuniapplication.business.Career;
import com.alonso.myuniapplication.business.Subject;
import com.alonso.myuniapplication.business.University;
import com.alonso.myuniapplication.business.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    private EditText userNameET;
    private EditText passwordET;
    private EditText confirmationPasswordET;
    private EditText emailET;
    private Spinner universitySP;
    private Spinner careerSP;

    List<User> users;
    List<University> universities;
    University university;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReferenceUsers;
    private DatabaseReference databaseReferenceUniversity;

    public static final int USERNAME_MIN_LENGTH = 1;
    public static final int PASSWORD_MIN_LENGTH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReferenceUsers = FirebaseDatabase.getInstance().getReference("user");
        databaseReferenceUniversity = FirebaseDatabase.getInstance().getReference("university");

        setUpViews();
    }

    private void setUpViews() {
        /*users = new ArrayList<>();
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
        });*/

        userNameET = (EditText)findViewById(R.id.userNameET2);
        passwordET = (EditText)findViewById(R.id.passwordET2);
        confirmationPasswordET = (EditText)findViewById(R.id.confirmationPasswordET);
        emailET = (EditText)findViewById(R.id.emailET);

        universities = getMockedUniversitiesAndSave();
        databaseReferenceUniversity.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                universities = new ArrayList<>();
                university = dataSnapshot.getValue(University.class);
                /*for(DataSnapshot ds: dataSnapshot.getChildren()){
                    university = ds.getValue(University.class);
                    universities.add(university);
//                    System.out.println(universities.get(0).toString());
                }*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "loadPost:onCancelled", databaseError.toException());
//                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


        careerSP = (Spinner) findViewById(R.id.careerSP);
        ArrayAdapter<Career> dataAdapter1 = new ArrayAdapter<Career>(this, android.R.layout.simple_spinner_item, universities.get(0).getCareers());
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        careerSP.setAdapter(dataAdapter1);

        universitySP = (Spinner) findViewById(R.id.univercitySP);
        ArrayAdapter<University> dataAdapter = new ArrayAdapter<University>(this, android.R.layout.simple_spinner_item, universities);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        universitySP.setAdapter(dataAdapter);

    }

    @NonNull
    private List<University> getMockedUniversitiesAndSave() {
        List<University> universities = new ArrayList<University>();
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

        university.getCareers().get(1).getSubjects().add(new Subject(1000, "Contabilidad", "Contabilidad", 1));
        university.getCareers().get(1).getSubjects().add(new Subject(1100, "Economía internacional", "Economía internacional", 1));

        university.getCareers().get(2).getSubjects().add(new Subject(1200, "Atletismo", "Atletismo", 1));

        universities.add(university);

        //saveUniversity(university);
        return universities;
    }

    private void saveUniversity(University university) {
        String id = databaseReferenceUniversity.push().getKey();
        databaseReferenceUniversity.child(id).setValue(university);
    }

    public void signUp(View view){
        String userName = userNameET.getText().toString();
        String password = passwordET.getText().toString();
        String confirmationPassword = confirmationPasswordET.getText().toString();
        String email = emailET.getText().toString();
        //TODO: filter career in order to fit university
        University university = (University) universitySP.getSelectedItem();
        Career career = (Career) careerSP.getSelectedItem();

        if(this.validateCredentialsFmt(userName, password, confirmationPassword, email)){
            saveUser(userName, password, email, career);
        } else {
            Toast.makeText(this,"Credenciales Inválidas, vuelva a intentarlo", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUser(final String userName, String password, final String email, final Career career) {

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(SignUpActivity.this,"Registro de usuario exitoso!!", Toast.LENGTH_SHORT).show();
                    User user = new User(userName, email, career);
                    String id = databaseReferenceUsers.push().getKey();
                    databaseReferenceUsers.child(id).setValue(user);
                    startActivity(new Intent(SignUpActivity.this, MenuActivity.class));
                } else {
                    Toast.makeText(SignUpActivity.this,"Problema al registrar nuevo usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateCredentialsFmt(String userName, String password, String confirmationPassword, String email) {
        boolean validCredentials = false;
        if(!this.validCredentialsLength(userName, password)){
            Toast.makeText(this,"Credenciales Inválidas, logitud incorrercta", Toast.LENGTH_SHORT).show();
            return validCredentials;
        }
        if(!this.validPasswordsMatch(password, confirmationPassword)){
            Toast.makeText(this,"Las contraseñas deben coincidir", Toast.LENGTH_SHORT).show();
            return validCredentials;
        }
        if(!this.validUserName(userName)){
            Toast.makeText(this,"Nombre de usuario inválido", Toast.LENGTH_SHORT).show();
            return validCredentials;
        }
        if(!this.validEmail(email)){
            Toast.makeText(this,"Email inválido", Toast.LENGTH_SHORT).show();
            return validCredentials;

        }

        validCredentials=true;
        return validCredentials;
    }

    private boolean validEmail(String email) {
        return !email.equals("");
    }

    private boolean validPasswordsMatch(String password, String confirmationPassword) {
        return password.equals(confirmationPassword);
    }

    private boolean validUserName(String userName) {
        return !userName.equals("");
    }

    private boolean validCredentialsLength(String userName, String password) {
        return userName.length()>=USERNAME_MIN_LENGTH && password.length()>=PASSWORD_MIN_LENGTH;
    }
}
