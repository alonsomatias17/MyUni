package com.alonso.myuniapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggin);
        FirebaseApp.initializeApp(LogInActivity.this);
        firebaseAuth = FirebaseAuth.getInstance();

        validateCurrentUser();

        email = (EditText)findViewById(R.id.emailET);
        password = (EditText)findViewById(R.id.passwordET);
    }

    private void validateCurrentUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null){
            Toast.makeText(this,"Bienvenido nuevamente " + user.getEmail(), Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    public void logIn(View view){
        this.validateCredentials(email.getText().toString(), password.getText().toString());
    }

    public void signUp(View view){
        startActivity(new Intent(this, SignUpActivity.class));
    }

    private void validateCredentials(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LogInActivity.this,"Credenciales correctas!!!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LogInActivity.this, MenuActivity.class));

                } else{
                    Toast.makeText(LogInActivity.this,"Credenciales INCORRECTAS", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
