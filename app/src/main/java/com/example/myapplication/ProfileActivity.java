package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    static int pReqCode = 1;

    Uri pickedImgUri;

    TextView emailTv;
    ImageView profileImg;
    FirebaseAuth myAuth;
    FirebaseUser currentUser;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        emailTv = findViewById(R.id.profile_email);
        profileImg = findViewById(R.id.profile_image);
        progressBar = findViewById(R.id.profile_progressBar);

        myAuth = FirebaseAuth.getInstance();
        currentUser = myAuth.getCurrentUser();
        emailTv.setText(myAuth.getCurrentUser().getEmail());

        if(currentUser.getPhotoUrl() != null) {
            Picasso.get().load(currentUser.getPhotoUrl()).into(profileImg);
        }

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= 22) {
                    checkAndRequestForPermission();
                } else {
                    openGallery();
                }
            }
        });
    }

    private void checkAndRequestForPermission() {

        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if(ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(ProfileActivity.this, "Please provide the required permission", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(ProfileActivity.this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, pReqCode);
            }

        } else {
            openGallery();
        }

    }

    private void openGallery() {
        CropImage.activity(pickedImgUri)
                .setAspectRatio(1, 1)
                .start(ProfileActivity.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if((requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) && (resultCode == RESULT_OK) && (data != null)) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            pickedImgUri = result.getUri();

            progressBar.setVisibility(View.VISIBLE);

            profileImg.setImageURI(pickedImgUri);

            StorageReference myStorage = FirebaseStorage.getInstance().getReference().child("userPhotos");
            final StorageReference imageFilePath = myStorage.child(pickedImgUri.getLastPathSegment());

            imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(uri)
                                    .build();

                            currentUser.updateProfile(profileUpdate)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                showMessage("Profile image updated!");
                                                progressBar.setVisibility(View.INVISIBLE);
                                            }
                                        }
                                    });
                        }
                    });
                }
            });
        } else {
            Toast.makeText(ProfileActivity.this, "Error! Try again...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
        }
    }

    private void showMessage(String message) {
        Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
