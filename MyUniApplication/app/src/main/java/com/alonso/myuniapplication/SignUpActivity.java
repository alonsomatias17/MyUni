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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private EditText userNameET;
    private EditText passwordET;
    private EditText confirmationPasswordET;
    private EditText emailET;

    private FirebaseAuth firebaseAuth;

    public static final int USERNAME_MIN_LENGTH = 1;
    public static final int PASSWORD_MIN_LENGTH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();

        setUpViews();
    }

    private void setUpViews() {
        userNameET = (EditText)findViewById(R.id.userNameET2);
        passwordET = (EditText)findViewById(R.id.passwordET2);
        confirmationPasswordET = (EditText)findViewById(R.id.confirmationPasswordET);
        emailET = (EditText)findViewById(R.id.emailET);
    }

    public void signUp(View view){
        String userName = userNameET.getText().toString();
        String password = passwordET.getText().toString();
        String confirmationPassword = confirmationPasswordET.getText().toString();
        String email = emailET.getText().toString();

        if(this.validateCredentialsFmt(userName, password, confirmationPassword, email)){
            saveUser(password, email);
        } else {
            Toast.makeText(this,"Credenciales Inválidas, vuelva a intentarlo", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUser(String password, String email) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(SignUpActivity.this,"Registro de usuario exitoso!!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
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
        //TODO check in DB for not duplicated user name
        return true;
    }

    private boolean validCredentialsLength(String userName, String password) {
        return userName.length()>=USERNAME_MIN_LENGTH && password.length()>=PASSWORD_MIN_LENGTH;
    }
}
