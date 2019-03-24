package com.alonso.myuniapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alonso.myuniapplication.business.University;
import com.alonso.myuniapplication.business.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile3Activity extends AppCompatActivity {

    private TextView userNameTV;
    private TextView emailTV;
    private TextView universityTV;
    private TextView careerTV;
    private TextView universityDscTV;
    private TextView careerDscTV;
    private CircleImageView userProfileCIV;

    private University university;
    private User user = new User();
    private ProgressBar progressBar;
    private int progressBarStatus = 0;

    private FirebaseFirestore firebaseFirestore;
    private  StorageReference userProfileImagesRef;

    private static final int PBSTATUS_LOAD_FINISHED = 1;
    private static final int GALLERY_PIC = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile3);

        firebaseFirestore = FirebaseFirestore.getInstance();
        userProfileImagesRef = FirebaseStorage.getInstance().getReference().child("Profile Images");
        progressBar = findViewById(R.id.progressBarProfile);

        Intent mIntent = getIntent();
        user = mIntent.getParcelableExtra("User");

        /*new Thread(new Runnable() {
            @Override
            public void run() {*/
                while (progressBarStatus < PBSTATUS_LOAD_FINISHED && user.getUserName().equals("")) {
                }
                setUserProfile();
                progressBar.setVisibility(View.INVISIBLE);
            /*}
        }).start();*/

        getUniversityFS();

        userProfileCIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_PIC);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_PIC && resultCode== RESULT_OK  && data!=null){
            Uri imageUri = data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                saveProfileImage(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(Profile3Activity.this, error.getMessage() , Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void saveProfileImage(Uri resultUri) {
        final StorageReference filePath = userProfileImagesRef.child(user.getEmail() + ".jpg");

        /*filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Profile3Activity.this,"Imagen guardada correctamente" , Toast.LENGTH_SHORT).show();
                    final String downloadUrl = filePath.getDownloadUrl().toString();
                    user.setProfileImageUri(downloadUrl);
                    updateUserFS();
                } else {
                    Toast.makeText(Profile3Activity.this,"Error al guardar la imagen" , Toast.LENGTH_SHORT).show();
                    Log.e("ProfileActivity", " - Error saving image");
                }
            }
        });*/
        UploadTask uploadTask = filePath.putFile(resultUri);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return filePath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    user.setProfileImageUri(downloadUri.toString());
                    updateUserFS();
                    Picasso.get().load(user.getProfileImageUri()).into(userProfileCIV);
                } else {
                    Toast.makeText(Profile3Activity.this,"Error al guardar la imagen" , Toast.LENGTH_SHORT).show();
                    Log.e("ProfileActivity", " - Error saving image");
                }
            }
        });
    }

    private void setUserProfile() {
        userNameTV = findViewById(R.id.userNameTVProfile);
        emailTV = findViewById(R.id.emailTVProfile);
        careerTV = findViewById(R.id.careerTVProfile);
        careerDscTV = findViewById(R.id.careerDscProfileTV);
        userProfileCIV = findViewById(R.id.profile_image);


        userNameTV.setText(user.getUserName());
        emailTV.setText(user.getEmail());
        careerTV.setText(user.getCareer().getName());
        careerDscTV.setText(user.getCareer().getDescription());
        if(!user.getProfileImageUri().equals(""))
            Picasso.get().load(user.getProfileImageUri()).into(userProfileCIV);

        progressBarStatus++;
    }

    private void getUniversityFS(){
        firebaseFirestore.collection("universities")
                //TODO: get univercity from user
                .whereEqualTo("name", "Universidad de Morón")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                university = document.toObject(University.class);
                                if(!university.getName().equals("")){
                                    universityTV = findViewById(R.id.universityTVProfile);
                                    universityDscTV = findViewById(R.id.universityDscProfileTV);

                                    universityTV.setText(university.getName());
                                    universityDscTV.setText(university.getDescription());
                                    progressBarStatus++;
                                }

                                Log.d("findUserByEmailFS", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w("findUserByEmailFS", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void updateUserFS() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                firebaseFirestore.collection("users")
                        .document(user.getEmail())
                        .set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.i("OnGoingSubjectsAct", "User updated correctly");

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("OnGoingSubjectsAct", "Error updating user", e);
                            }
                        });
            }
        }).start();
    }
}
