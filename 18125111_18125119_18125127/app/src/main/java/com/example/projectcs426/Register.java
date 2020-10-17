package com.example.projectcs426;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class Register extends AppCompatActivity {
    DataBaseHelper dataBaseHelper;
    //FirebaseStorage storage;
    ImageView avaReg;
    Button btnChoose,btnSendReg;
    //StorageReference storageReference;
    TextInputEditText FullName=null,DOB=null,Phone=null,Address=null,Gender=null,Note= null;
    ProgressBar mprogressBar;

    /*private Uri filePath,mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;*/

    private final int PICK_IMAGE_REQUEST = 1000;
    private static  final int PERMISSION_CODE = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_form);
        avaReg = findViewById(R.id.avaReg);
        btnChoose= findViewById(R.id.btnChoose);
        btnSendReg= findViewById(R.id.btn_sendReg);
        dataBaseHelper = new DataBaseHelper(this);


        initRegInfo();

        btnChoose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED){
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions,PERMISSION_CODE);
                    }
                    else {
                        chooseImage();
                    }
                }
                else{
                    chooseImage();
                }
            }
        });

        btnSendReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(FullName.getText().toString().matches("")|DOB.getText().toString().matches("")|
                        Phone.getText().toString().matches("")|Address.getText().toString().matches("")|
                        Gender.getText().toString().matches("")|Note.getText().toString().matches("")){
                    Snackbar.make(view, "Please fill all the information", Snackbar.LENGTH_LONG)
                            .setAction("DISMISS", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }).show();
                }

                else {
                    Intent intent= new Intent(Register.this,Maps.class);
                    //startActivity(intent);
                }
            }
        });
    }


    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Drawable myDraw;
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            try {
                InputStream is= getContentResolver().openInputStream(data.getData());
                myDraw = Drawable.createFromStream(is, data.getData().toString());
            } catch (FileNotFoundException e) {
                myDraw = getResources().getDrawable(R.drawable.xuan_vinh); // neu kh lay dc anh thif set anh vinh
                e.printStackTrace();
            }
            avaReg.setImageURI(data.getData());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if (grantResults.length>0 && grantResults[0]==
                        PackageManager.PERMISSION_GRANTED){
                    chooseImage();
                }
                else {
                    Toast.makeText(this,"Permission denied...!",Toast.LENGTH_SHORT);
                }
            }
        }
    }


    public void initRegInfo() {
        FullName= findViewById(R.id.nameReg);
        Phone = findViewById(R.id.phoneReg);
        Address=findViewById(R.id.addReg);
        DOB = findViewById(R.id.birthReg);
        Gender = findViewById(R.id.genderReg);
        Note = findViewById(R.id.noteReg);

        FullName.setText(null);
        Phone.setText(null);
        Address.setText(null);
        DOB.setText(null);
        Gender.setText(null);
        Note.setText(null);
    }



}
