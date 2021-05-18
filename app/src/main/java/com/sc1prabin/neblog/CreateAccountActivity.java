package com.sc1prabin.neblog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


public class CreateAccountActivity extends AppCompatActivity {
    private StorageReference storageReference;
    private TextView fName,lName,email,password;
    private Button createButton,alreadyAccount;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private ImageButton profilImage;
   private  Uri pImageUri = null;

    private final static int GALLERY_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Users");

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference  = FirebaseStorage.getInstance().getReference().child("PBlog_Profile_Pics");
        progressDialog = new ProgressDialog(this);

        alreadyAccount = findViewById(R.id.alreadyAccountId);
        fName = findViewById(R.id.fNameId);
        lName = findViewById(R.id.lNameId);
        email = findViewById(R.id.emailId);
        password = findViewById(R.id.passwordId);

        createButton = findViewById(R.id.create);

        profilImage = findViewById(R.id.profileImageBtnId);
        profilImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntentAgain = new Intent();
                galleryIntentAgain.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntentAgain.setType("image/*");
                startActivityForResult(galleryIntentAgain, GALLERY_CODE);
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
            }
        });
        alreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateAccountActivity.this,MainActivity.class));
                finish();
            }
        });

    }


    private void createNewAccount() {
        String firstName = fName.getText().toString();
        String lastName = lName.getText().toString();
        String emailVal = email.getText().toString();
        String passwordVal = password.getText().toString();

        if(!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(emailVal) && !TextUtils.isEmpty(passwordVal)){
            progressDialog.setMessage("Creating Account...");
            progressDialog.show();
            firebaseAuth.createUserWithEmailAndPassword(emailVal, passwordVal).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    if(authResult != null){
                        StorageReference imagePath = storageReference.child("PBlog_Profile_Pics").child(pImageUri.getLastPathSegment());

                        imagePath.putFile(pImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String usedId = firebaseAuth.getCurrentUser().getUid();
                        DatabaseReference currentUserDb = databaseReference.child(usedId);
                        currentUserDb.child("firstName").setValue(firstName);
                        currentUserDb.child("lastName").setValue(lastName);
                        currentUserDb.child("image").setValue(pImageUri.toString());


                        progressDialog.dismiss();

                        Intent intent = new Intent(CreateAccountActivity.this,PostListActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//
                                startActivity(intent);

                            }
                        });

                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_CODE && resultCode == RESULT_OK){
             pImageUri = data.getData();

             CropImage.activity(pImageUri).setAspectRatio(1,1)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(CreateAccountActivity.this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                profilImage.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

}