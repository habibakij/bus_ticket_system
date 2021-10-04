package com.gineuscrypticalsoft.busticketsystem.view.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.gineuscrypticalsoft.busticketsystem.R;
import com.gineuscrypticalsoft.busticketsystem.adapter.CarListAdapter;
import com.gineuscrypticalsoft.busticketsystem.model.CarListModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CarList extends AppCompatActivity {

    KProgressHUD kProgressHUD;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    SharedPreferences sharedPref;
    List<CarListModel> carListModelList;
    CarListModel carListModel;
    CarListAdapter carListAdapter;
    int getId;
    String TAG= "car_list";

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);

        recyclerView= findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        sharedPref = this.getSharedPreferences(getString(R.string.SAVE_ID), Context.MODE_PRIVATE);
        getId = sharedPref.getInt("id", 0);
        Log.d(TAG, "get_id: "+getId);

        kProgressHUD = KProgressHUD.create(CarList.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setWindowColor(getResources().getColor(R.color.black))
                .setCancellable(true)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f);

        carListModelList= new ArrayList<>();
        for(int i= 1; i<=getId; i++){
            databaseReference = FirebaseDatabase.getInstance().getReference().child("CarList").child(String.valueOf(i));
            loadDataFromDatabase();
        }

    }

    void loadDataFromDatabase(){
        Log.d(TAG, "check_execute");
        kProgressHUD.show();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kProgressHUD.dismiss();
                if(snapshot.exists()){
                    carListModel= new CarListModel();
                    carListModel.setCarName(snapshot.child("carName").getValue().toString());
                    carListModel.setSeatRent((snapshot.child("seatRent").getValue()).toString());
                    carListModel.setAcType((snapshot.child("acType").getValue()).toString());
                    carListModel.setStartTime((snapshot.child("startTime").getValue()).toString());
                    carListModel.setEndTime((snapshot.child("endTime").getValue()).toString());
                    carListModel.setFromCity((snapshot.child("fromCity").getValue()).toString());
                    carListModel.setToCity((snapshot.child("toCity").getValue()).toString());
                    carListModel.setImage((snapshot.child("image").getValue()).toString());
                    Log.d(TAG, "__test: "+carListModel.getCarName());
                    carListModelList.add(carListModel);
                    carListAdapter= new CarListAdapter(CarList.this, carListModelList);
                    recyclerView.setAdapter(carListAdapter);
                } else {
                    Toast.makeText(CarList.this, "Data Not Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                kProgressHUD.dismiss();
                Toast.makeText(CarList.this, "internal error, please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }
}