package com.gineuscrypticalsoft.busticketsystem.view.booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.gineuscrypticalsoft.busticketsystem.R;
import com.gineuscrypticalsoft.busticketsystem.adapter.UserCarListAdapter;
import com.gineuscrypticalsoft.busticketsystem.model.CarListModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {

    String TAG= "search_activity";
    ArrayList<String> matchingID;
    KProgressHUD kProgressHUD;
    DatabaseReference databaseReference;
    List<CarListModel> carListModelList;
    CarListModel carListModel;
    UserCarListAdapter userCarListAdapter;
    RecyclerView recyclerViewSearch;
    Animation top;
    UserCarListAdapter.OnItemListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerViewSearch= findViewById(R.id.recycler_view_search);
        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSearch.setHasFixedSize(true);
        top= AnimationUtils.loadAnimation(this, R.anim.top);
        recyclerViewSearch.setAnimation(top);

        carListModelList= new ArrayList<>();
        matchingID= new ArrayList<String>();

        kProgressHUD = KProgressHUD.create(Search.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setWindowColor(getResources().getColor(R.color.black))
                .setCancellable(true)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f);

        matchingID= (ArrayList<String>) getIntent().getSerializableExtra("matchingID");
        for (int i= 0; i<matchingID.size(); i++){
            Log.d(TAG, "check_id: "+matchingID.get(i));
            databaseReference = FirebaseDatabase.getInstance().getReference().child("CarList").child(String.valueOf(matchingID.get(i)));
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
                    userCarListAdapter = new UserCarListAdapter(Search.this, carListModelList, mListener);
                    recyclerViewSearch.setAdapter(userCarListAdapter);
                    setClickListener();
                } else {
                    Toast.makeText(Search.this, "Data Not Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                kProgressHUD.dismiss();
                Toast.makeText(Search.this, "internal error, please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setClickListener() {
        mListener= new UserCarListAdapter.OnItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(Search.this, "clicked: "+carListModelList.get(position).getCarName(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Search.this, CarSelect.class));
            }
        };
    }
}