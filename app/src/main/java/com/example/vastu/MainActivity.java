package com.example.vastu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity  {

    private Button submit;
    private TextView register;
    private TextView enter_email,enter_password;
    FirebaseUser firebaseUser;
    FirebaseAuth fire;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        register=findViewById(R.id.register_btn);
        submit=findViewById(R.id.submit_btn);
        enter_email=findViewById(R.id.enter_email);
        enter_password=findViewById(R.id.enter_password);
        fire=FirebaseAuth.getInstance();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent re=new Intent(MainActivity.this,Resgiter.class);
                startActivity(re);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String e=enter_email.getText().toString();
                String p=enter_password.getText().toString();
                if(e!=null && e.isEmpty()){
                    enter_email.setError("please enter Email");
                    enter_email.requestFocus();
                }
                else if(p!=null && p.isEmpty()) {
                    enter_password.setError("please enter email");
                    enter_password.requestFocus();
                }
                else if(p.isEmpty() && e.isEmpty()){
                    Toast.makeText(MainActivity.this,"Both are empty",Toast.LENGTH_SHORT).show();
                }
                else if(!(p.isEmpty() && e.isEmpty())){
                        fire.signInWithEmailAndPassword(e,p).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(!task.isSuccessful()){
                                    Toast.makeText(MainActivity.this,"Email/Password not found!",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Intent i=new Intent(MainActivity.this,WorkingMain.class);
                                    startActivity(i);
                                }
                            }
                        });
                }
                else{
                    Toast.makeText(MainActivity.this,"Did you Register?",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null){
            Intent intent=new Intent(MainActivity.this,WorkingMain.class);
            startActivity(intent);
            finish();
        }
    }
}