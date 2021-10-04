package com.gineuscrypticalsoft.busticketsystem.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.gineuscrypticalsoft.busticketsystem.R;
import com.gineuscrypticalsoft.busticketsystem.view.admin.AdminDashboard;
import com.gineuscrypticalsoft.busticketsystem.view.booking.Search;
import com.gineuscrypticalsoft.busticketsystem.view.registration.SignIn;
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

public class FirstFragment extends Fragment {

    TextView textView;
    Button btnSearch;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    EditText editTextFromCity, editTextToCity;
    SharedPreferences sharedPref;
    KProgressHUD kProgressHUD;
    String TAG= "first_fragment";
    List<String> matchingID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView(view);
        kProgressHUD = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setWindowColor(getResources().getColor(R.color.black))
                .setCancellable(true)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f);

        matchingID= new ArrayList<>();
        sharedPref = getContext().getSharedPreferences(getString(R.string.SAVE_ID), Context.MODE_PRIVATE);
        int getId = sharedPref.getInt("id", 0);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kProgressHUD.show();
                Toast.makeText(getContext(), "button clicked", Toast.LENGTH_SHORT).show();
                matchingID.clear();
                String fromCity= editTextFromCity.getText().toString();
                String toCity= editTextToCity.getText().toString();
                for(int i= 1; i<= getId; i++) {
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("CarList").child(String.valueOf(i));
                    int finalI = i;
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            kProgressHUD.dismiss();
                            String getFromCity= snapshot.child("fromCity").getValue().toString();
                            String getToCity= snapshot.child("toCity").getValue().toString();
                            Log.d(TAG, "city: "+getFromCity);
                            if(fromCity.equals(getFromCity) && toCity.equals(getToCity)){
                                Toast.makeText(getContext(), "matching", Toast.LENGTH_SHORT).show();
                                matchingID.add(String.valueOf(finalI));
                                Intent intent= new Intent(getContext(), Search.class);
                                intent.putExtra("matchingID", (Parcelable) matchingID);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            kProgressHUD.dismiss();
                        }
                    });
                }

            }
        });


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "ok", Toast.LENGTH_SHORT).show();
                textView.setText("today date");
            }
        });
    }

    void findView(View view){
        btnSearch= view.findViewById(R.id.button_search);
        textView= view.findViewById(R.id.edit_text_date_picker);
        editTextFromCity= view.findViewById(R.id.edit_text_from_city);
        editTextToCity= view.findViewById(R.id.edit_text_to_city);
    }
}