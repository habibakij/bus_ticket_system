package com.gineuscrypticalsoft.busticketsystem.view.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.gineuscrypticalsoft.busticketsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kaopiz.kprogresshud.KProgressHUD;
import java.util.HashMap;

public class Profile_update extends AppCompatActivity {

    String TAG= "profile_update";
    Button btnProfileUpdate;
    ImageView imageViewProfile;
    EditText editTextName, editTextPhone, editTextAddress;
    private static final int GALLERY_REQUEST_CODE = 100;
    int cameraCount= 0;
    Uri imageUri;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseUser currentUser;
    String uId;
    KProgressHUD kProgressHUD;
    HashMap<String, String> hashMap;
    String imageDownloadUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);
        setTitle("Profile Update");

        kProgressHUD = KProgressHUD.create(Profile_update.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setWindowColor(getResources().getColor(R.color.black))
                .setCancellable(true)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f);

        FindView();

        btnProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextName.getText().toString().isEmpty()){
                    Toast.makeText(Profile_update.this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                } else if (editTextPhone.getText().toString().isEmpty()){
                    Toast.makeText(Profile_update.this, "Please Enter Mobile", Toast.LENGTH_SHORT).show();
                } else if (editTextAddress.getText().toString().isEmpty()){
                    Toast.makeText(Profile_update.this, "Please Enter Address", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFirebaseStorage();
                }
            }
        });

        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    void FindView(){
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        uId = currentUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("UserProfileData").child(uId);
        storageReference= FirebaseStorage.getInstance().getReference("profileImage");

        btnProfileUpdate= findViewById(R.id.btn_profile_update);
        imageViewProfile= findViewById(R.id.profile_update_image);
        editTextName= findViewById(R.id.edit_text_name);
        editTextPhone= findViewById(R.id.edit_text_phone);
        editTextAddress= findViewById(R.id.edit_text_address);
    }

    private void selectImage() {
        final CharSequence[] charSequences = new CharSequence[]{
                Html.fromHtml("<b><font color='black'>Choose Photo</font></b>"), "Take from Camera", "Select Photo Gallery"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(Profile_update.this,R.style.Style_Dialog_Rounded_Corner);
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
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imageViewProfile.setImageBitmap(bitmap);
            } catch (Exception e) {
                Log.d(TAG, "camera_error: "+e.getMessage());
            }

        } else {
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                imageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    imageViewProfile.setImageBitmap(bitmap);
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

    void uploadFirebaseStorage(){
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
                                        kProgressHUD.dismiss();
                                        Toast.makeText(Profile_update.this, "Upload successfully", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(Profile_update.this, Profile.class));
                                        finish();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    kProgressHUD.dismiss();
                                    Toast.makeText(Profile_update.this, "not updated", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            kProgressHUD.dismiss();
                            Toast.makeText(Profile_update.this, "image not updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                kProgressHUD.dismiss();
                Toast.makeText(Profile_update.this, "image not updated", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void dataSave(){
        hashMap= new HashMap<>();
        hashMap.put("name", editTextName.getText().toString());
        hashMap.put("phone", editTextPhone.getText().toString());
        hashMap.put("address", editTextAddress.getText().toString());
        hashMap.put("image", imageDownloadUri);
    }

}