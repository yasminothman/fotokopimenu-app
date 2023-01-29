package com.example.fotokopimenuapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginPage extends AppCompatActivity implements View.OnClickListener{

    private Button registerUser, LogIn, forgotPassword;
    private EditText email,password;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        LogIn = (Button) findViewById(R.id.buttonLogIn);
        LogIn.setOnClickListener(this);

        email = (EditText) findViewById(R.id.editTextEA);
        password = (EditText) findViewById(R.id.EditTextPassword);

        mAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        forgotPassword = (Button) findViewById(R.id.buttonForgotPassword);
        forgotPassword.setOnClickListener(this);

    }

    //start sini untuk register button
    @Override
    public void onClick(View v){
        switch(v.getId()){
            //sampai sini
            case R.id.buttonLogIn:
                userLogIn();
                break;
            case R.id.buttonForgotPassword:
                startActivity(new Intent(this, ForgotPassword.class));
                break;
        }
    }

    private void userLogIn() {
        String editTextEmail = email.getText().toString().trim();
        String editTextPassword = password.getText().toString().trim();

        if(editTextEmail.isEmpty()){
            email.setError("Email is required!");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(editTextEmail).matches()){
            email.setError("Please enter a valid email");
            email.requestFocus();
            return;
        }

        if(editTextPassword.isEmpty()){
            password.setError("Password is required!");
            password.requestFocus();
            return;
        }
        if(password.length()<6){
            password.setError("Min password  length is 6 characters!");
            password.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(editTextEmail,editTextPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()){
                        //redirect to admin page
                        startActivity(new Intent(loginPage.this, adminPage.class));
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(loginPage.this, "Check your email to verify your account!", Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(loginPage.this,"Failed to login! Please check your credentials", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}