package com.example.vastu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class fragment_main extends Fragment implements DatePickerDialog.OnDateSetListener {
    public static final String TAG = "TAG";
    Button persondetails;
    EditText newdob,newpersonname,newem,newpersonrel,newnakshatra, newperson,rashi;
    FirebaseAuth objFire;
    FirebaseFirestore firebaseFirestore;
    String userID;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_fragment_main,container,false);
        objFire=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        persondetails=(Button) view.findViewById(R.id.persondetails);
        newpersonname=(EditText) view.findViewById(R.id.newpersonname);
        newem =(EditText)view.findViewById(R.id.newemail);
        rashi =(EditText)view.findViewById(R.id.newrashi);
        newpersonrel =(EditText)view.findViewById(R.id.newpersonrel);
        newnakshatra =(EditText)view.findViewById(R.id.newnakshat);
        newdob =(EditText)view.findViewById(R.id.newdob);
        newperson =(EditText)view.findViewById(R.id.newperson);
        persondetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newname=newpersonname.getText().toString();
                String newemail=newem.getText().toString();
                String ras=rashi.getText().toString();
                String rel=newpersonrel.getText().toString();
                String nakshatra=newnakshatra.getText().toString();
                String dob2=newdob.getText().toString();
                String details=newperson.getText().toString();
                if(newname!=null && newname.isEmpty()){
                    newpersonname.setError("please enter name");
                    newpersonname.requestFocus();
                }
                else if(newemail!=null && newemail.isEmpty()) {
                    newem.setError("please enter email");
                    newem.requestFocus();
                }
                else if(ras!=null && ras.isEmpty()){
                    rashi.setError("Please Enter Rashi");
                    rashi.requestFocus();
                }
                else if(rel!=null && rel.isEmpty()){
                    newpersonrel.setError("Please enter Relation");
                    newpersonrel.requestFocus();
                }
                else if(nakshatra!=null && nakshatra.isEmpty()){
                    newnakshatra.setError("Please Enter Nakshatra");
                    newnakshatra.requestFocus();
                }
                else if(dob2!=null && dob2.isEmpty()){
                    newdob.setError("Please enter Date of Birth");
                    newdob.requestFocus();
                }
                else if(details!=null && details.isEmpty()){
                    newperson.setError("Please Enter his/her details");
                    newperson.requestFocus();
                }
                userID=objFire.getCurrentUser().getUid();
                DocumentReference documentReference=firebaseFirestore.collection("User").document(userID);
                Map<String,Object> user=new HashMap<>();
                user.put("Relatives Name",newname);
                user.put("Relatives Email",newemail);
                user.put("Relatives Rashi",ras);
                user.put("Relatives Relation",rel);
                user.put("Relatives nakshatra",nakshatra);
                user.put("Relatives DateOfBirth",dob2);
                user.put("Relatives personality",details);
                documentReference.set(user, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG,"OnSuccess:Relative Entered"+userID);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"OnFailure:Relative not Entered"+userID);
                    }
                });
            }
        });
        newdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatepickerDialog();
            }
        });
        return view;

    }

    private void showDatepickerDialog() {
        DatePickerDialog datepicker=new DatePickerDialog(requireActivity(),this, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datepicker.show();
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month++;
        String dateofbirth;
        dateofbirth = dayOfMonth+"/"+month+"/"+year;
        newdob.setText(dateofbirth);
    }

}