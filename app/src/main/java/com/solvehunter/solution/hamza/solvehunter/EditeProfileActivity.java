package com.solvehunter.solution.hamza.solvehunter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class EditeProfileActivity extends AppCompatActivity {
    Button update;
    private FirebaseAuth mAuth;
    ProgressBar loader;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myRef =database.getReference();
    TextView name;
    TextView phone;
    ImageView Image;
    ImageView choseImage;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Uri selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edite_profile);
        update = findViewById(R.id.update);
        loader=findViewById(R.id.loader);
        name=findViewById(R.id.name);
        phone=findViewById(R.id.phone);
        mAuth = FirebaseAuth.getInstance();
        Image=findViewById(R.id.user_image);
        choseImage=findViewById(R.id.image_chooser);
        pref = getApplicationContext().getSharedPreferences("myPref", MODE_PRIVATE);
        editor = pref.edit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();

        name.setText(pref.getString("name",""));
        phone.setText(pref.getString("phone",""));
        if(!pref.getString("image","").equals("")){
            Glide.with(EditeProfileActivity.this).load(pref.getString("image","")).into(Image);
        }
    }
    private void userSelectGallery() {

        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);
    }

    private void userSelectCamera() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 0);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    selectedImage = data.getData();
                    Image.setImageURI(selectedImage);

                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    selectedImage = data.getData();
                    Image.setImageURI(selectedImage);
                }
                break;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loader.setVisibility(View.VISIBLE);
                if(selectedImage!=null)
                    uploadImageToFirebaseStorge();
                myRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("name").setValue(name.getText().toString());
                myRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("phone").setValue(phone.getText().toString());
                Toast.makeText(EditeProfileActivity.this,"update done",Toast.LENGTH_LONG).show();
                loader.setVisibility(View.GONE);
            }
        });

        choseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final CharSequence[] items = {"Take Photo", "Choose from gallery", "Cancel"};
                final CharSequence[] items = { "Choose from gallery", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(EditeProfileActivity.this);
                builder.setTitle("choose image");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int item) {
                        boolean result = Utility.checkPermission(EditeProfileActivity.this);
                        if (items[item].equals("Take Photo")) {
                            if (result) userSelectCamera();
                        } else if (items[item].equals("Choose from gallery")) {
                            if (result) ;
                            userSelectGallery();
                        } else if (items[item].equals("cancel")) {
                            dialogInterface.dismiss();
                        }

                    }
                });

                builder.show();
            }
        });
    }

    private void uploadImageToFirebaseStorge() {
        StorageReference bookImageRef = FirebaseStorage.getInstance().getReference("profilePics/" + System.currentTimeMillis() + ".jpg");
        {
            bookImageRef.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    myRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("image")
                            .setValue(taskSnapshot.getDownloadUrl().toString());
                    editor.putString("image",taskSnapshot.getDownloadUrl().toString());
                    editor.commit();
                }
            });
        }
    }
}
