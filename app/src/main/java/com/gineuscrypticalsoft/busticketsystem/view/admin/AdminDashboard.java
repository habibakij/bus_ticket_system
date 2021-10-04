package com.gineuscrypticalsoft.busticketsystem.view.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.gineuscrypticalsoft.busticketsystem.R;
import com.gineuscrypticalsoft.busticketsystem.view.MainActivity;
import com.gineuscrypticalsoft.busticketsystem.view.profile.Profile;
import com.gineuscrypticalsoft.busticketsystem.view.profile.Profile_update;
import com.gineuscrypticalsoft.busticketsystem.view.registration.SignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class AdminDashboard extends AppCompatActivity {

    static final int GALLERY_REQUEST_CODE= 100;
    EditText editTextCarName, editTextAcOrNon, editTextId, editTextSeatRent;
    Button btnSubmit, btnPickDateTime;
    TextView txtStartTime, txtEndTime, txtLastSerialId;
    Spinner spinnerFromCity, spinnerToCity;
    String fromCity= "", toCity= "";
    String[] cityList= {"Dhaka", "Faridpur", "Gazipur", "Gopalganj", "Kishoreganj", "Madaripur", "Manikganj", "Munshiganj", "Narayanganj", "Narsingdi", "Rajbari",
            "Shariatpur", "Tangail", "Bagerhat", "Chuadanga", "Jessore", "Jhenaidah", "Khulna", "Kushtia", "Magura", "Meherpur", "Narail", "Satkhira", "Jamalpur",
            "Mymensingh", "Netrokona", "Sherpur", "Bogra", "Joypurhat", "Naogaon", "Natore", "Chapainawabganj", "Pabna", "Rajshahi", "Sirajganj", "Dinajpur",
            "Gaibandha", "Kurigram", "Lalmonirhat", "Nilphamari", "Panchagarh", "Rangpur", "Thakurgaon", "Habiganj", "Moulvibazar", "Sunamganj", "Sylhet",
            "Barguna", "Barisal", "Bhola", "Jhalokati", "Patuakhali", "Pirojpur", "Bandarban", "Brahmanbaria", "Chandpur", "Chittagong", "Comilla", "Cox's Bazar",
            "Feni", "Khagrachhari", "Lakshmipur", "Noakhali", "Rangamati"};
    TimePicker timePicker;
    ImageView imageViewCar;
    int cameraCount= 0;
    Uri imageUri;
    String TAG= "admin_dashboard";
    DatabaseReference databaseReference;
    StorageReference storageReference;
    KProgressHUD kProgressHUD;
    String imageDownloadUri;
    HashMap<String, String> hashMap;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    int imageValidation= 0;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        setTitle("Upload New Car");
        kProgressHUD = KProgressHUD.create(AdminDashboard.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setWindowColor(getResources().getColor(R.color.black))
                .setCancellable(true)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f);
        FindView();
        mAuth = FirebaseAuth.getInstance();
        int getId = sharedPref.getInt("id", 0);
        String string= "last upload serial: "+getId;
        txtLastSerialId.setText(string);

        Arrays.sort(cityList);
        spinnerFromCity.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, cityList));
        spinnerToCity.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, cityList));

        imageViewCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        spinnerFromCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fromCity= parent.getItemAtPosition(position).toString();
                Toast.makeText(AdminDashboard.this, "select "+parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerToCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                toCity= parent.getItemAtPosition(position).toString();
                Toast.makeText(AdminDashboard.this, "select "+parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        txtStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder= new AlertDialog.Builder(AdminDashboard.this, R.style.Style_Dialog_Rounded_Corner);
                AlertDialog dialog= builder.create();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
                dialog.setContentView(R.layout.calender_view);
                timePicker= dialog.findViewById(R.id.time_picker);
                btnPickDateTime= dialog.findViewById(R.id.btn_pick_date_time);

                btnPickDateTime.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {
                        int hour= timePicker.getHour();
                        int m= timePicker.getMinute();
                        String format= "";
                        if (hour == 0) {
                            hour += 12;
                            format = "AM";
                        } else if (hour == 12) {
                            format = "PM";
                        } else if (hour > 12) {
                            hour -= 12;
                            format = "PM";
                        } else {
                            format = "AM";
                        }
                        String time= hour+":"+m+" "+format;
                        txtStartTime.setText(time);
                        dialog.dismiss();
                        Toast.makeText(AdminDashboard.this, "select: "+time, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        txtEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder= new AlertDialog.Builder(AdminDashboard.this, R.style.Style_Dialog_Rounded_Corner);
                AlertDialog dialog= builder.create();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
                dialog.setContentView(R.layout.calender_view);
                timePicker= dialog.findViewById(R.id.time_picker);
                btnPickDateTime= dialog.findViewById(R.id.btn_pick_date_time);

                btnPickDateTime.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {
                        int hour= timePicker.getHour();
                        int m= timePicker.getMinute();
                        String format= "";
                        if (hour == 0) {
                            hour += 12;
                            format = "AM";
                        } else if (hour == 12) {
                            format = "PM";
                        } else if (hour > 12) {
                            hour -= 12;
                            format = "PM";
                        } else {
                            format = "AM";
                        }
                        String time= hour+":"+m+" "+format;
                        txtEndTime.setText(time);
                        dialog.dismiss();
                        Toast.makeText(AdminDashboard.this, "select: "+time, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextId.getText().toString().isEmpty()){
                    Toast.makeText(AdminDashboard.this, "Please Id", Toast.LENGTH_SHORT).show();
                } else if(editTextSeatRent.getText().toString().isEmpty()){
                    Toast.makeText(AdminDashboard.this, "Please Seat Rent", Toast.LENGTH_SHORT).show();
                } else if(editTextCarName.getText().toString().isEmpty()){
                    Toast.makeText(AdminDashboard.this, "Please Name", Toast.LENGTH_SHORT).show();
                } else if(editTextAcOrNon.getText().toString().isEmpty()){
                    Toast.makeText(AdminDashboard.this, "Please ac type", Toast.LENGTH_SHORT).show();
                } else if(fromCity.equals("")){
                    Toast.makeText(AdminDashboard.this, "Please Select From", Toast.LENGTH_SHORT).show();
                } else if(toCity.equals("")){
                    Toast.makeText(AdminDashboard.this, "Please Select To", Toast.LENGTH_SHORT).show();
                } else if(txtStartTime.getText().toString().isEmpty()){
                    Toast.makeText(AdminDashboard.this, "Please Start Time", Toast.LENGTH_SHORT).show();
                } else if(txtEndTime.getText().toString().isEmpty()){
                    Toast.makeText(AdminDashboard.this, "Please End Time", Toast.LENGTH_SHORT).show();
                } else if(imageValidation == 0){
                    Toast.makeText(AdminDashboard.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                } else {
                    int id = Integer.parseInt(editTextId.getText().toString());
                    Log.d(TAG, "check_id: " + getId);
                    if (id > getId) {
                        databaseReference = FirebaseDatabase.getInstance().getReference().child("CarList").child(editTextId.getText().toString());
                        storageReference = FirebaseStorage.getInstance().getReference("carImage");
                        uploadData();
                    } else {
                        Toast.makeText(AdminDashboard.this, "invalid serial id", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_administration) {
            Toast.makeText(this, "You Aren't Authorized", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.action_profile) {
            Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminDashboard.this, Profile.class));
        } else if (id == R.id.action_logout) {
            kProgressHUD.show();
            signOut();
        }
        return super.onOptionsItemSelected(item);
    }

    void signOut(){
        mAuth.signOut();
        kProgressHUD.dismiss();
        startActivity(new Intent(AdminDashboard.this, SignUp.class));
        finish();
    }


    void FindView(){
        editTextCarName= findViewById(R.id.edit_text_car_name);
        editTextAcOrNon= findViewById(R.id.edit_text_ac_or_non);
        txtStartTime= findViewById(R.id.txt_start_time);
        txtEndTime= findViewById(R.id.txt_end_time);
        btnSubmit= findViewById(R.id.btn_submit);
        spinnerFromCity= findViewById(R.id.spinner_from_city);
        spinnerToCity= findViewById(R.id.spinner_to_city);
        imageViewCar= findViewById(R.id.image_car);
        editTextId= findViewById(R.id.edit_text_id);
        editTextSeatRent= findViewById(R.id.edit_text_seat_rent);
        txtLastSerialId= findViewById(R.id.txt_last_id);
        sharedPref = this.getSharedPreferences(getString(R.string.SAVE_ID), Context.MODE_PRIVATE);
    }

    private void selectImage() {
        final CharSequence[] charSequences = new CharSequence[]{
                Html.fromHtml("<b><font color='black'>Choose Photo</font></b>"), "Take from Camera", "Select Photo Gallery"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminDashboard.this,R.style.Style_Dialog_Rounded_Corner);
        builder.setItems(charSequences, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 1) {
                    cameraCount++;
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 0);
                } else if (i == 2) {
                    Intent gallery = new Intent();
                    gallery.setType("image/*");
                    gallery.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(gallery, "Select Image"), GALLERY_REQUEST_CODE);
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM;
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (cameraCount > 0) {
            try {
                imageValidation++;
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imageViewCar.setImageBitmap(bitmap);
            } catch (Exception e) {
                Log.d(TAG, "camera_error: "+e.getMessage());
            }

        } else {
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                imageUri = data.getData();
                try {
                    imageValidation++;
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    imageViewCar.setImageBitmap(bitmap);
                } catch (Exception e) {
                    Log.d(TAG, "gallery_error: "+e.getMessage());
                }
            }
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    void uploadData(){
        kProgressHUD.show();
        StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
        reference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> taskSnapshot) {
                reference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            imageDownloadUri = task.getResult().toString();
                            Log.d(TAG, "image_download_uri: "+imageDownloadUri);
                            dataSave();
                            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        sharedPreferencesSaveID(Integer.parseInt(editTextId.getText().toString()));
                                        kProgressHUD.dismiss();
                                        Toast.makeText(AdminDashboard.this, "Upload successfully", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(AdminDashboard.this, CarList.class));
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    kProgressHUD.dismiss();
                                    Toast.makeText(AdminDashboard.this, "not updated", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            kProgressHUD.dismiss();
                            Toast.makeText(AdminDashboard.this, "image not updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                kProgressHUD.dismiss();
                Toast.makeText(AdminDashboard.this, "image not updated", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void dataSave(){
        hashMap= new HashMap<>();
        hashMap.put("carID", editTextId.getText().toString());
        hashMap.put("seatRent", editTextSeatRent.getText().toString());
        hashMap.put("carName", editTextCarName.getText().toString());
        hashMap.put("acType", editTextAcOrNon.getText().toString());
        hashMap.put("fromCity", fromCity);
        hashMap.put("toCity", toCity);
        hashMap.put("startTime", txtStartTime.getText().toString());
        hashMap.put("endTime", txtEndTime.getText().toString());
        hashMap.put("image", imageDownloadUri);
    }

    void sharedPreferencesSaveID(int id){
        editor = sharedPref.edit();
        editor.putInt("id",id);
        editor.apply();
        editor.apply();
    }

}