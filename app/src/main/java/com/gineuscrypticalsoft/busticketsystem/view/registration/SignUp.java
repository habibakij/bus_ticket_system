package com.gineuscrypticalsoft.busticketsystem.view.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.gineuscrypticalsoft.busticketsystem.R;

public class SignUp extends AppCompatActivity {

    Button btnSignUp;
    EditText editTextSignUpEmail, editTextSignUpPassword, editTextSignUpConPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        FindView();

    }


    void FindView(){
        btnSignUp= findViewById(R.id.btn_sign_up);
        editTextSignUpEmail= findViewById(R.id.edit_text_sign_up_email);
        editTextSignUpPassword= findViewById(R.id.edit_text_sign_up_password);
        editTextSignUpConPassword= findViewById(R.id.edit_text_sign_up_con_password);
    }
}