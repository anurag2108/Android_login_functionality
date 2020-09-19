package com.example.vastu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.media.audiofx.DynamicsProcessing;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Resgiter extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    public static final String TAG = "TAG";
    private TextView name,email,pass1,pass2;
    private EditText btn1;
    private Button btn2;
    String dateofbirth;
    FirebaseAuth objFire;
    FirebaseFirestore firebaseFirestore;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resgiter);
        objFire=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        name=findViewById((R.id.person_name));
        email=findViewById(R.id.person_email);
        pass1=findViewById(R.id.password_1);
        pass2=findViewById(R.id.password_2);
        btn1=findViewById(R.id.dob_button);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatepickerDialog();
            }
        });
        btn2=findViewById(R.id.register_submit);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String e=email.getText().toString();
                final String n=name.getText().toString();
                String p1=pass1.getText().toString();
                String p2=pass2.getText().toString();
                final String dob=btn1.getText().toString();
                if(n!=null && n.isEmpty()){
                    name.setError("please enter name");
                    name.requestFocus();
                }
                else if(e!=null &&e.isEmpty()) {
                    email.setError("please enter email");
                    email.requestFocus();
                }
                else if((p1!=null && p2!=null) &&(p1.isEmpty() || p2.isEmpty())){
                    pass1.setError("please enter password");
                    pass2.setError("please enter password");
                }
                else if(p2!=null &&!p2.equals(p1)){
                    pass2.setError("please enter the same password");
                    pass2.requestFocus();
                }
                else if(!p1.isEmpty() && !p2.isEmpty() && !e.isEmpty() && !n.isEmpty()){

                    objFire.createUserWithEmailAndPassword(e,p2).addOnCompleteListener(Resgiter.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(Resgiter.this,"Please try again",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                userID=objFire.getCurrentUser().getUid();
                                DocumentReference documentReference=firebaseFirestore.collection("User").document(userID);
                                Map<String,Object> user=new HashMap<>();
                                user.put("Name",n);
                                user.put("DateofBirth",dob);
                                user.put("Email",e);
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG,"OnSuccess:User Created"+userID);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG,"OnFailure:User not Created"+userID);
                                    }
                                });
                                Intent how = new Intent(Resgiter.this, MainActivity.class);
                                startActivity(how);
                            }
                        }
                    });

                }
                else{
                    Toast.makeText(Resgiter.this,"Error Occurred",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void showDatepickerDialog(){
        DatePickerDialog datepicker=new DatePickerDialog(this,this, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datepicker.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month++;
        dateofbirth = dayOfMonth+"/"+month+"/"+year;
        btn1.setText(dateofbirth);
    }
}