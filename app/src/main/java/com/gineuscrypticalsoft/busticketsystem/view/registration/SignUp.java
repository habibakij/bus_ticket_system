package com.gineuscrypticalsoft.busticketsystem.view.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.gineuscrypticalsoft.busticketsystem.R;
import com.gineuscrypticalsoft.busticketsystem.view.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kaopiz.kprogresshud.KProgressHUD;

public class SignUp extends AppCompatActivity {

    String TAG= "sign_up";
    Button btnSignUp;
    EditText editTextSignUpEmail, editTextSignUpPassword;
    private FirebaseAuth mAuth;
    KProgressHUD kProgressHUD;
    private FirebaseUser mUser;
    LinearLayout linearLayout;
    Animation top, bottom;

    @Override
    protected void onStart() {
        super.onStart();
        if(mUser != null){
            Log.d(TAG,"current_user: "+mUser.getEmail());
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

        linearLayout.setAnimation(top);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextSignUpEmail.getText().toString().isEmpty()){
                    Toast.makeText(SignUp.this, "Please Email", Toast.LENGTH_SHORT).show();
                } else if(editTextSignUpPassword.getText().toString().isEmpty()){
                    Toast.makeText(SignUp.this, "Please Password", Toast.LENGTH_SHORT).show();
                } else if(editTextSignUpPassword.getText().toString().length() < 6){
                    Toast.makeText(SignUp.this, "Password at least 6 char", Toast.LENGTH_SHORT).show();
                } else {
                    kProgressHUD.show();
                    mAuth.createUserWithEmailAndPassword(editTextSignUpEmail.getText().toString(), editTextSignUpPassword.getText().toString())
                            .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                kProgressHUD.dismiss();
                                Log.d(TAG, "signInWithEmail:success");
                                Toast.makeText(SignUp.this, "success", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                Log.d(TAG,"current_user: "+user.getEmail());
                                startActivity(new Intent(SignUp.this, MainActivity.class));
                                finish();
                            } else {
                                kProgressHUD.dismiss();
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(SignUp.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });

    }

    void FindView(){
        top= AnimationUtils.loadAnimation(this, R.anim.top);
        bottom= AnimationUtils.loadAnimation(this, R.anim.bottom);
        linearLayout= findViewById(R.id.login_layout);
        btnSignUp= findViewById(R.id.btn_sign_up);
        editTextSignUpEmail= findViewById(R.id.edit_text_sign_up_email);
        editTextSignUpPassword= findViewById(R.id.edit_text_sign_up_password);
    }

    /// not uses
    void signInUser(){
        mAuth.signInWithEmailAndPassword(editTextSignUpEmail.getText().toString(), editTextSignUpPassword.getText().toString())
                .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(SignUp.this, "success", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void slideUp(View view){
        TranslateAnimation animate = new TranslateAnimation(0,0, view.getHeight(), 0);
        animate.setDuration(5000);
        animate.setFillAfter(true);
        view.startAnimation(animate);

    }



}