package com.gineuscrypticalsoft.busticketsystem.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.gineuscrypticalsoft.busticketsystem.R;
import com.gineuscrypticalsoft.busticketsystem.view.registration.SignIn;

public class FirstFragment extends Fragment {

    TextView textView;
    Button btnSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView(view);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_SecondFragment);
                Toast.makeText(getContext(), "button clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), SignIn.class));
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
    }
}