package com.example.firelog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    EditText nemail,npassword;
    Button nlogin;
    TextView ncreate;
    ProgressBar progressBar;
    FirebaseAuth AAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nemail=findViewById(R.id.emaill);
        npassword=findViewById(R.id.passwordd);
        nlogin=findViewById(R.id.login);
        AAuth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressBar);
        ncreate=findViewById(R.id.create);

        nlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email=nemail.getText().toString().trim();
                String password = npassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    nemail.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    npassword.setError("password is required");
                    return;
                }

                if (password.length()<6){
                    npassword.setError("password must be atleast 7 character");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //authenticate the user

                AAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(login.this, "logged in successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent (getApplicationContext(),MainActivity.class));
                        }
                        else{

                            Toast.makeText(login.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });

        ncreate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),register.class));
            }
        });

    }
}
