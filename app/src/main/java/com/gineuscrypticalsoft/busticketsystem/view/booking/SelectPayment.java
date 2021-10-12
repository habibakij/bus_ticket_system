package com.gineuscrypticalsoft.busticketsystem.view.booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gineuscrypticalsoft.busticketsystem.R;
import com.gineuscrypticalsoft.busticketsystem.view.profile.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class SelectPayment extends AppCompatActivity {
    TextView tvCarName, tvDateTime, tvCarRent, tvCarCoachType, tvCarFrom, tvCarTo;
    TextView tvHolderName, tvHolderEmail, tvHolderPhone, tvHolderBooking, tvHolderFrom, tvHolderTo;
    ImageView imageBkash, imageRocket, imageTcash, imageUcash, imageSureCash, imageEasyCash;
    String carName, carDateTime, carRent, carCoachType, carFrom, carTo, holderBooking;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    FirebaseUser currentUser;
    FirebaseUser mUser;
    String uId;
    KProgressHUD kProgressHUD;
    String TAG= "select_payment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_payment);

        FindView();
        mAuth = FirebaseAuth.getInstance();
        mUser= mAuth.getCurrentUser();
        kProgressHUD = KProgressHUD.create(SelectPayment.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setWindowColor(getResources().getColor(R.color.black))
                .setCancellable(true)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f);

        carName= getIntent().getExtras().getString("selectName");
        carDateTime= getIntent().getExtras().getString("selectDateTime");
        carRent= getIntent().getExtras().getString("selectRent");
        carCoachType= getIntent().getExtras().getString("selectAcType");
        carFrom= getIntent().getExtras().getString("selectFrom");
        carTo= getIntent().getExtras().getString("selectTo");
        holderBooking= getIntent().getExtras().getString("selectSeat");

        setText();
        dataLoadFromFirebase();
        selectPayment();
    }

    void FindView(){
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        uId = currentUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("UserProfileData").child(uId);

        tvCarName= findViewById(R.id.txt_payment_car_name);
        tvDateTime= findViewById(R.id.txt_payment_car_date_time);
        tvCarRent= findViewById(R.id.txt_payment_car_rent);
        tvCarCoachType= findViewById(R.id.txt_payment_car_ac_type);
        tvCarFrom= findViewById(R.id.txt_payment_car_from);
        tvCarTo= findViewById(R.id.txt_payment_car_to);

        tvHolderName= findViewById(R.id.txt_payment_holders_name);
        tvHolderEmail= findViewById(R.id.txt_payment_holders_email);
        tvHolderPhone= findViewById(R.id.txt_payment_holders_phone);
        tvHolderBooking= findViewById(R.id.txt_payment_holders_total_seat);
        tvHolderFrom= findViewById(R.id.txt_payment_holders_form);
        tvHolderTo= findViewById(R.id.txt_payment_holders_to);

        imageBkash= findViewById(R.id.image_bkash);
        imageRocket= findViewById(R.id.image_rocket);
        imageTcash= findViewById(R.id.image_tcash);
        imageUcash= findViewById(R.id.image_ucash);
        imageSureCash= findViewById(R.id.image_surecash);
        imageEasyCash= findViewById(R.id.image_easycash);
    }

    void setText(){
        tvCarName.setText(carName);
        tvDateTime.setText(carDateTime);
        tvCarRent.setText(carRent);
        tvCarCoachType.setText(carCoachType);
        tvCarFrom.setText(carFrom);
        tvCarTo.setText(carTo);
        tvHolderBooking.setText(holderBooking);
        tvHolderFrom.setText(carFrom);
        tvHolderTo.setText(carTo);
    }

    void dataLoadFromFirebase(){
        tvHolderEmail.setText(mUser.getEmail());
        kProgressHUD.show();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kProgressHUD.dismiss();
                if(snapshot.exists()){
                    tvHolderName.setText(Objects.requireNonNull(snapshot.child("name").getValue()).toString());
                    tvHolderPhone.setText(snapshot.child("phone").getValue().toString());
                } else {
                    Toast.makeText(SelectPayment.this, "Please update your profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SelectPayment.this, "data loading error: "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void selectPayment(){

        imageBkash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageBkash.setBackgroundColor(getResources().getColor(R.color.purple_500));

                imageRocket.setBackgroundColor(getResources().getColor(R.color.white));
                imageTcash.setBackgroundColor(getResources().getColor(R.color.white));
                imageUcash.setBackgroundColor(getResources().getColor(R.color.white));
                imageSureCash.setBackgroundColor(getResources().getColor(R.color.white));
                imageEasyCash.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        imageRocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageRocket.setBackgroundColor(getResources().getColor(R.color.purple_500));

                imageBkash.setBackgroundColor(getResources().getColor(R.color.white));
                imageTcash.setBackgroundColor(getResources().getColor(R.color.white));
                imageUcash.setBackgroundColor(getResources().getColor(R.color.white));
                imageSureCash.setBackgroundColor(getResources().getColor(R.color.white));
                imageEasyCash.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        imageTcash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageTcash.setBackgroundColor(getResources().getColor(R.color.purple_500));

                imageRocket.setBackgroundColor(getResources().getColor(R.color.white));
                imageBkash.setBackgroundColor(getResources().getColor(R.color.white));
                imageUcash.setBackgroundColor(getResources().getColor(R.color.white));
                imageSureCash.setBackgroundColor(getResources().getColor(R.color.white));
                imageEasyCash.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        imageUcash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUcash.setBackgroundColor(getResources().getColor(R.color.purple_500));

                imageRocket.setBackgroundColor(getResources().getColor(R.color.white));
                imageTcash.setBackgroundColor(getResources().getColor(R.color.white));
                imageBkash.setBackgroundColor(getResources().getColor(R.color.white));
                imageSureCash.setBackgroundColor(getResources().getColor(R.color.white));
                imageEasyCash.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        imageSureCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSureCash.setBackgroundColor(getResources().getColor(R.color.purple_500));

                imageRocket.setBackgroundColor(getResources().getColor(R.color.white));
                imageTcash.setBackgroundColor(getResources().getColor(R.color.white));
                imageUcash.setBackgroundColor(getResources().getColor(R.color.white));
                imageBkash.setBackgroundColor(getResources().getColor(R.color.white));
                imageEasyCash.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        imageEasyCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageEasyCash.setBackgroundColor(getResources().getColor(R.color.purple_500));

                imageRocket.setBackgroundColor(getResources().getColor(R.color.white));
                imageTcash.setBackgroundColor(getResources().getColor(R.color.white));
                imageUcash.setBackgroundColor(getResources().getColor(R.color.white));
                imageSureCash.setBackgroundColor(getResources().getColor(R.color.white));
                imageBkash.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });
    }


}