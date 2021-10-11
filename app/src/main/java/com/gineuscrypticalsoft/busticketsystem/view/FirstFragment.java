package com.gineuscrypticalsoft.busticketsystem.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.gineuscrypticalsoft.busticketsystem.R;
import com.gineuscrypticalsoft.busticketsystem.utils.Constant;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class FirstFragment extends Fragment {

    TextView textView;
    Button btnSearch;
    DatabaseReference databaseReference, databaseReferenceCount;
    AutoCompleteTextView editTextFromCity, editTextToCity;
    SharedPreferences sharedPref;
    KProgressHUD kProgressHUD;
    String TAG= "first_fragment";
    ArrayList<String> matchingID;
    ArrayAdapter<String> adapter;
    Calendar myCalendar = Calendar.getInstance();
    long carListCount;

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
        childrenCount();
        matchingID= new ArrayList<>();
        sharedPref = getContext().getSharedPreferences(getString(R.string.SAVE_ID), Context.MODE_PRIVATE);
        //int getId = sharedPref.getInt("id", 0);

        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, Constant.CITY_LIST);
        editTextFromCity.setAdapter(adapter);
        editTextToCity.setAdapter(adapter);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextFromCity.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "City", Toast.LENGTH_SHORT).show();
                } else if (editTextToCity.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "City", Toast.LENGTH_SHORT).show();
                } else if (textView.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Date", Toast.LENGTH_SHORT).show();
                } else {
                    kProgressHUD.show();
                    matchingID.clear();
                    String fromCity = editTextFromCity.getText().toString().trim();
                    String toCity = editTextToCity.getText().toString().trim();
                    searchingCityFromDatabase(fromCity, toCity, carListCount);
                }
            }
        });


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        textView.setText(sdf.format(myCalendar.getTime()));
    }

    void findView(View view){
        btnSearch= view.findViewById(R.id.button_search);
        textView= view.findViewById(R.id.edit_text_date_picker);
        editTextFromCity= view.findViewById(R.id.edit_text_from_city);
        editTextToCity= view.findViewById(R.id.edit_text_to_city);
    }

    void searchingCityFromDatabase(String city1, String city2, long getId) {
        for(int i= 1; i<= getId; i++) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("CarList").child(String.valueOf(i));
            int finalI = i;
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String getFromCity= snapshot.child("fromCity").getValue().toString();
                    String getToCity= snapshot.child("toCity").getValue().toString();
                    Log.d(TAG, "city: "+getFromCity);
                    if(city1.equals(getFromCity) && city2.equals(getToCity)){
                        matchingID.add(String.valueOf(finalI));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    kProgressHUD.dismiss();
                    Toast.makeText(getContext(), "error :"+error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                kProgressHUD.dismiss();
                Log.d(TAG, "check___2: "+matchingID);
                if (matchingID.isEmpty()){
                    Toast.makeText(getContext(), "Not Found", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Founded", Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(getContext(), Search.class);
                    intent.putExtra("matchingID", matchingID);
                    startActivity(intent);
                }
            }
        },5000);

    }

    void childrenCount(){
        kProgressHUD.show();
        databaseReferenceCount = FirebaseDatabase.getInstance().getReference().child("CarList");
        databaseReferenceCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kProgressHUD.dismiss();
                carListCount= snapshot.getChildrenCount();
                Log.d(TAG,"car_list: "+carListCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}