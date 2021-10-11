package com.gineuscrypticalsoft.busticketsystem.view.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gineuscrypticalsoft.busticketsystem.R;
import com.gineuscrypticalsoft.busticketsystem.view.MainActivity;
import com.gineuscrypticalsoft.busticketsystem.view.registration.SignUp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

public class Profile extends AppCompatActivity {

    FirebaseAuth mAuth;
    KProgressHUD kProgressHUD;
    private FirebaseUser mUser;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseUser currentUser;
    String uId;
    String TAG= "profile";

    Button btnProfileEdit;
    ImageView imageView;
    TextView txtName, txtEmail, txtPhone, txtAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Profile");

        kProgressHUD = KProgressHUD.create(Profile.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setWindowColor(getResources().getColor(R.color.black))
                .setCancellable(true)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f);

        mAuth = FirebaseAuth.getInstance();
        mUser= mAuth.getCurrentUser();
        FindView();
        dataLoadFromFirebase();
        btnProfileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, Profile_update.class));
            }
        });
    }

    void FindView(){
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        uId = currentUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("UserProfileData").child(uId);
        storageReference= FirebaseStorage.getInstance().getReference("profileImage");

        btnProfileEdit= findViewById(R.id.btn_profile_edit);
        imageView= findViewById(R.id.profile_image);
        txtName= findViewById(R.id.txt_name);
        txtEmail= findViewById(R.id.txt_email);
        txtPhone= findViewById(R.id.txt_phone);
        txtAddress= findViewById(R.id.txt_address);
    }

    void dataLoadFromFirebase(){
        txtEmail.setText(mUser.getEmail());
        kProgressHUD.show();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kProgressHUD.dismiss();
                if(snapshot.exists()){
                    txtName.setText(Objects.requireNonNull(snapshot.child("name").getValue()).toString());
                    txtPhone.setText(snapshot.child("phone").getValue().toString());
                    txtAddress.setText(snapshot.child("address").getValue().toString());
                    String image= snapshot.child("image").getValue().toString();
                    Log.d(TAG,"image_uri: "+image);
                    Picasso.get().load(image).into(imageView);
                } else {
                    Toast.makeText(Profile.this, "Please update your profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile.this, "data loading error: "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}