package com.example.vastu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class WorkingMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    TextView tv1,tv2;
    NavigationView navigationView;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_working_main);
        toolbar=findViewById(R.id.toolbar);
        //toolbar.setBackground(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));
        toolbar.setBackgroundColor(Color.parseColor("#000000"));
        toolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Vastu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore= FirebaseFirestore.getInstance();

        drawerLayout=findViewById(R.id.drawer);
        navigationView=findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(Color.parseColor("#FFFFFF"));

        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_frag,new fragment_main());
        fragmentTransaction.commit();
        updateNavigationHeader();
    }
    public void closeDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if(menuItem.getItemId() == R.id.home){
            closeDrawer();
            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_frag,new fragment_main());
            fragmentTransaction.commit();


        }
        if(menuItem.getItemId()==R.id.personfuture){
            closeDrawer();
            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_frag,new fragment_main());
            fragmentTransaction.commit();

        }
        if(menuItem.getItemId()==R.id.housevastu){
            closeDrawer();
            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_frag,new fragment_video());
            fragmentTransaction.commit();

        }
        if(menuItem.getItemId()==R.id.logout){

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Do you want to logout?");
            alertDialogBuilder.setPositiveButton("yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    FirebaseAuth fire;
                                    FirebaseAuth.AuthStateListener mAuth;
                                    FirebaseAuth.getInstance().signOut();
                                    Intent i=new Intent(WorkingMain.this,MainActivity.class);
                                    startActivity(i);
                                }
                            });

            alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    closeDrawer();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        return true;
    }
    public void  updateNavigationHeader(){
        navigationView=findViewById(R.id.navigation_view);
        View headerview=navigationView.getHeaderView(0);
        tv1= headerview.findViewById(R.id.user_name_fetch);
        tv2=headerview.findViewById(R.id.user_email_fetch);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore= FirebaseFirestore.getInstance();
        String userId=firebaseAuth.getCurrentUser().getUid();
        DocumentReference documentReference=firebaseFirestore.collection("User").document(userId);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    if(documentSnapshot.exists()){
                        tv1.setText(documentSnapshot.getString("Name"));
                        tv2.setText(documentSnapshot.getString("Email"));
                    }
                }

            }
        });

    }
    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Please confirm");
        builder.setMessage("Are you want to exit the app?");
        builder.setCancelable(true);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //WorkingMain.super.onBackPressed();
                //finish();
                //moveTaskToBack(true);
                finishAffinity();
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //write something when you say no.
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}