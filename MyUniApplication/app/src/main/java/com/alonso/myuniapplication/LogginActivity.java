package com.alonso.myuniapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LogginActivity extends AppCompatActivity {

    private EditText userName;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggin);

        userName = (EditText)findViewById(R.id.userNameET);
        password = (EditText)findViewById(R.id.passwordET);
    }

    public void logIn(View view){
        this.validateCredentials(userName.getText().toString(), password.getText().toString());
    }

    public void signUp(View view){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private void validateCredentials(String userName, String password){
        if(isValidCredentials(userName, password)){
            Toast.makeText(this,"Credenciales correctas!!!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(this,"Credenciales INCORRECTAS", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidCredentials(String userName, String password) {
        //TODO check in DB
        return userName.equals("matias") && password.equals("alonso");
    }
}
