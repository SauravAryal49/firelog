package com.example.firelog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.proto.TargetGlobal;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText mfullname,memail,mpassword,mphone;
    Button mRegister;
    TextView mlogging;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    ProgressBar progressBar;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mfullname=findViewById(R.id.fullname);
        memail=findViewById(R.id.email);
        mpassword=findViewById(R.id.password);
        mphone=findViewById(R.id.phone);
        mRegister=findViewById(R.id.Register);
        mlogging=findViewById(R.id.logging);

        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        progressBar=findViewById(R.id.progressBar);

        if(fAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        mRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final String email=memail.getText().toString().trim();
                String password = mpassword.getText().toString().trim();
                final String fullName=mfullname.getText().toString().trim();
                final String Phone=mphone.getText().toString();


                if (TextUtils.isEmpty(email)){
                    memail.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    mpassword.setError("password is required");
                    return;
                }

                if (password.length()<6){
                    mpassword.setError("password must be atleast 7 character");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //register the user in the firebase

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()){
                           Toast.makeText(register.this,"user created",Toast.LENGTH_SHORT).show();
                           userID=fAuth.getCurrentUser().getUid();
                           DocumentReference documentReference=fStore.collection("users").document(userID);
                           Map<String,Object> user=new HashMap<>();
                           user.put("fullName",fullName);
                           user.put("email",email);
                           user.put("phone",Phone);
                           documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {
                                   Log.d(TAG,"onsuccess: user profile is created for" + userID);
                               }
                           });
                           startActivity(new Intent(getApplicationContext(),MainActivity.class));
                       }
                       else{
                           Toast.makeText(register.this, "error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                           progressBar.setVisibility(View.GONE);
                       }
                    }
                });
            }
        });

        mlogging.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),login.class));
            }
        });
    }
}