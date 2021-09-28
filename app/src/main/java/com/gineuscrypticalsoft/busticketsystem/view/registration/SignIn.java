package com.gineuscrypticalsoft.busticketsystem.view.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gineuscrypticalsoft.busticketsystem.R;
import com.gineuscrypticalsoft.busticketsystem.view.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

public class SignIn extends AppCompatActivity {

    Button btnLogIn;
    EditText editTextEmail, editTextPassword;
    TextView txtSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        FindView();
        setTitle("Sign In");

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextEmail.getText().toString().isEmpty()){
                    Toast.makeText(SignIn.this, "Email", Toast.LENGTH_SHORT).show();
                    editTextEmail.requestFocus();
                } else if (editTextPassword.getText().toString().isEmpty()){
                    Toast.makeText(SignIn.this, "Password", Toast.LENGTH_SHORT).show();
                    editTextPassword.requestFocus();
                } else if (editTextPassword.getText().toString().length() < 6){
                    Toast.makeText(SignIn.this, "Password At least 6 Char Long", Toast.LENGTH_SHORT).show();
                } else {

                }
            }
        });

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this, SignUp.class));
            }
        });
    }

    void FindView(){
        btnLogIn= findViewById(R.id.btn_log_in);
        editTextEmail= findViewById(R.id.edit_text_log_email);
        editTextPassword= findViewById(R.id.edit_text_log_password);
        txtSignUp= findViewById(R.id.txt_sign_up);
    }
}