package com.gineuscrypticalsoft.busticketsystem.view.booking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.gineuscrypticalsoft.busticketsystem.R;

public class CarSelect extends AppCompatActivity {

    TextView seatA1, seatA2, seatA3, seatA4, seatB1, seatB2, seatB3, seatB4, seatC1, seatC2, seatC3, seatC4, seatD1, seatD2, seatD3, seatD4, seatE1, seatE2, seatE3, seatE4,
            seatF1, seatF2, seatF3, seatF4, seatG1, seatG2, seatG3, seatG4, seatH1, seatH2, seatH3, seatH4, seatI1, seatI2, seatI3, seatI4, seatJ1, seatJ2, seatJ3, seatJ4;
    String name, dateTime, rent, acType, form, to;
    TextView textViewSelectName, textViewSelectDateTime, textViewSelectRent, textViewSelectAcType, textViewSelectFrom, textViewSelectTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_select);

        FindView();
        name= getIntent().getExtras().getString("selectName");
        dateTime= getIntent().getExtras().getString("selectDateTime");
        rent= getIntent().getExtras().getString("selectRent");
        acType= getIntent().getExtras().getString("selectAcType");
        form= getIntent().getExtras().getString("selectFrom");
        to= getIntent().getExtras().getString("selectTo");

        setVehiclesInfo();

    }

    void FindView(){
        seatA1= findViewById(R.id.seat_a1);
        seatA2= findViewById(R.id.seat_a2);
        seatA3= findViewById(R.id.seat_a3);
        seatA4= findViewById(R.id.seat_a4);
        seatB1= findViewById(R.id.seat_b1);
        seatB2= findViewById(R.id.seat_b2);
        seatB3= findViewById(R.id.seat_b3);
        seatB4= findViewById(R.id.seat_b4);
        seatC1= findViewById(R.id.seat_c1);
        seatC2= findViewById(R.id.seat_c2);
        seatC3= findViewById(R.id.seat_c3);
        seatC4= findViewById(R.id.seat_c4);
        seatD1= findViewById(R.id.seat_d1);
        seatD2= findViewById(R.id.seat_d2);
        seatD3= findViewById(R.id.seat_d3);
        seatD4= findViewById(R.id.seat_d4);
        seatE1= findViewById(R.id.seat_e1);
        seatE2= findViewById(R.id.seat_e2);
        seatE3= findViewById(R.id.seat_e3);
        seatE4= findViewById(R.id.seat_e4);
        seatF1= findViewById(R.id.seat_f1);
        seatF2= findViewById(R.id.seat_f2);
        seatF3= findViewById(R.id.seat_f3);
        seatF4= findViewById(R.id.seat_f4);
        seatG1= findViewById(R.id.seat_g1);
        seatG2= findViewById(R.id.seat_g2);
        seatG3= findViewById(R.id.seat_g3);
        seatG4= findViewById(R.id.seat_g4);
        seatH1= findViewById(R.id.seat_h1);
        seatH2= findViewById(R.id.seat_h2);
        seatH3= findViewById(R.id.seat_h3);
        seatH4= findViewById(R.id.seat_h4);
        seatI1= findViewById(R.id.seat_i1);
        seatI2= findViewById(R.id.seat_i2);
        seatI3= findViewById(R.id.seat_i3);
        seatI4= findViewById(R.id.seat_i4);
        seatJ1= findViewById(R.id.seat_j1);
        seatJ2= findViewById(R.id.seat_j2);
        seatJ3= findViewById(R.id.seat_j3);
        seatJ4= findViewById(R.id.seat_j4);
        textViewSelectName= findViewById(R.id.txt_select_name);
        textViewSelectDateTime= findViewById(R.id.txt_select_date_time);
        textViewSelectRent= findViewById(R.id.txt_select_rent);
        textViewSelectAcType= findViewById(R.id.txt_select_ac_type);
        textViewSelectFrom= findViewById(R.id.txt_select_from);
        textViewSelectTo= findViewById(R.id.txt_select_to);

    }

    void setVehiclesInfo(){
        textViewSelectName.setText(name);
        textViewSelectDateTime.setText(dateTime);
        textViewSelectRent.setText(rent+" TK");
        textViewSelectAcType.setText(acType+" Coach");
        textViewSelectFrom.setText(form);
        textViewSelectTo.setText(to);
    }

}