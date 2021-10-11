package com.gineuscrypticalsoft.busticketsystem.view;

import android.content.Intent;
import android.os.Bundle;

import com.gineuscrypticalsoft.busticketsystem.R;
import com.gineuscrypticalsoft.busticketsystem.view.admin.AdminDashboard;
import com.gineuscrypticalsoft.busticketsystem.view.profile.Profile;
import com.gineuscrypticalsoft.busticketsystem.view.registration.SignUp;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kaopiz.kprogresshud.KProgressHUD;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Objects;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    KProgressHUD kProgressHUD;
    private FirebaseUser mUser;
    String adminEmail= "akijmia.cse@gmail.com";

    @Override
    protected void onStart() {
        super.onStart();
        if(mUser == null){
            Log.d(TAG, "check_user");
            startActivity(new Intent(MainActivity.this, SignUp.class));
            finish();
        } else {
            if(Objects.equals(mUser.getEmail(), adminEmail)){
                startActivity(new Intent(MainActivity.this, AdminDashboard.class));
                finish();
            }
            Log.d(TAG,"current_user: "+mUser.getEmail());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        mAuth = FirebaseAuth.getInstance();
        mUser= mAuth.getCurrentUser();

        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);

        kProgressHUD = KProgressHUD.create(MainActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setWindowColor(getResources().getColor(R.color.black))
                .setCancellable(true)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_administration) {
            Toast.makeText(this, "You Aren't Authorized", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.action_profile) {
            startActivity(new Intent(MainActivity.this, Profile.class));
        } else if (id == R.id.action_logout) {
            kProgressHUD.show();
            signOut();
        }
        return super.onOptionsItemSelected(item);
    }

    void signOut(){
        mAuth.signOut();
        kProgressHUD.dismiss();
        startActivity(new Intent(MainActivity.this, SignUp.class));
        finish();
    }

}