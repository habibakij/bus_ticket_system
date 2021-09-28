package com.gineuscrypticalsoft.busticketsystem.view.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gineuscrypticalsoft.busticketsystem.R;
import com.gineuscrypticalsoft.busticketsystem.utils.Constant;
import com.gineuscrypticalsoft.busticketsystem.view.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kaopiz.kprogresshud.KProgressHUD;

public class SignUp extends AppCompatActivity {

    Button btnSignUp;
    EditText editTextSignUpEmail, editTextSignUpPhone, editTextSignUpPassword, editTextSignUpConPassword;
    private FirebaseAuth mAuth;
    KProgressHUD kProgressHUD;
    private FirebaseUser mUser;

    @Override
    protected void onStart() {
        super.onStart();
        if(mUser != null){
            startActivity(new Intent(SignUp.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        FindView();
        setTitle("Sign Up");
        mAuth = FirebaseAuth.getInstance();
        mUser= mAuth.getCurrentUser();


        kProgressHUD = KProgressHUD.create(SignUp.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setWindowColor(getResources().getColor(R.color.black))
                .setCancellable(true)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextSignUpEmail.getText().toString().isEmpty()){
                    Toast.makeText(SignUp.this, "Please Email", Toast.LENGTH_SHORT).show();
                } else if(editTextSignUpPhone.getText().toString().isEmpty()){
                    Toast.makeText(SignUp.this, "Please Phone", Toast.LENGTH_SHORT).show();
                } else if(editTextSignUpPassword.getText().toString().isEmpty()){
                    Toast.makeText(SignUp.this, "Please Password", Toast.LENGTH_SHORT).show();
                } else if(editTextSignUpPassword.getText().toString().length() < 6){
                    Toast.makeText(SignUp.this, "Password at least 6 char", Toast.LENGTH_SHORT).show();
                } else if(editTextSignUpConPassword.getText().toString().isEmpty()){
                    Toast.makeText(SignUp.this, "Please Confirm Password", Toast.LENGTH_SHORT).show();
                } else if(!editTextSignUpPassword.getText().toString().equals(editTextSignUpConPassword.getText().toString())){
                    Toast.makeText(SignUp.this, "Password Not Matched", Toast.LENGTH_SHORT).show();
                } else {
                    kProgressHUD.show();
                    mAuth.createUserWithEmailAndPassword(editTextSignUpEmail.getText().toString(), editTextSignUpPassword.getText().toString()).
                            addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                kProgressHUD.dismiss();
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(SignUp.this, "Successfully log in: "+user.toString(), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUp.this, MainActivity.class));
                                finish();
                            } else {
                                kProgressHUD.dismiss();
                                Log.d("failed: ","error: "+task.toString());
                                Toast.makeText(SignUp.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });

    }


    void FindView(){
        btnSignUp= findViewById(R.id.btn_sign_up);
        editTextSignUpEmail= findViewById(R.id.edit_text_sign_up_email);
        editTextSignUpPhone= findViewById(R.id.edit_text_sign_up_phone);
        editTextSignUpPassword= findViewById(R.id.edit_text_sign_up_password);
        editTextSignUpConPassword= findViewById(R.id.edit_text_sign_up_con_password);
    }
}