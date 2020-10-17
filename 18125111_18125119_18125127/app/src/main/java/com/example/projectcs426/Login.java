package com.example.projectcs426;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Login extends AppCompatActivity {
    EditText mEmail,mPassword;
    Button mLoginBtn;
    TextView mCreateBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    boolean check = true;
    HelperInfor helperInfor;
    DataBaseHelper db;

    private ArrayList<HelperInfor> _helpers = new ArrayList<>();
    long maxid = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.passw);
        mLoginBtn = findViewById(R.id.LoginButton);
        mCreateBtn = findViewById(R.id.TextCreateAcc);

        db = new DataBaseHelper(this);
        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

//        databaseReference=FirebaseDatabase.getInstance().getReference().child("HelperInfor");
//        DatabaseReference helpRef = databaseReference.child("HelperInfor");
//
//        ValueEventListener valueEventListener = new ValueEventListener() {
//
//            //@Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot ds: dataSnapshot.getChildren()){
//                    HelperInfor helperInfor = new HelperInfor();
//                    helperInfor.setHName(ds.child("hname").getValue(String.class));
//                    helperInfor.setPhone(ds.child("phone").getValue(String.class));
//                    helperInfor.setDOB(ds.child("dob").getValue(String.class));
//                    helperInfor.setGender(ds.child("gender").getValue(String.class));
//                    helperInfor.setNotes(ds.child("notes").getValue(String.class));
//                    helperInfor.setAddress(ds.child("address").getValue(String.class));
//                    helperInfor.setRating(ds.child("rating").getValue(Float.class));
//                    helperInfor.setAvatar(ds.child("avatar").getValue(Integer.class));
//                    helperInfor.setID(ds.child("id").getValue(Integer.class));
//                    helperInfor.setAvailable(ds.child("available").getValue(Boolean.class));
//
//                    Log.d("TAG", helperInfor.getHName());
//
//                    _helpers.add(helperInfor);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        };
//        helpRef.addListenerForSingleValueEvent(valueEventListener);

        if(check) {

            /*db.insertHelper("Bùi Xuân Vĩnh","0989778966","Male","14/09/2000","231/83/8A Dương Bá Trạc, Phường 1, Quận 8","Work early in the noon",
                   (float)2, String.valueOf(Integer.valueOf(R.drawable.xuan_vinh)), "true");
            db.insertHelper("Cao Ngọc Sơn","0938895657","Male","19/07/2000","528/1A Minh Phụng, Phường 9, Quận 11","Work after 6pm",
                   (float)3, String.valueOf(Integer.valueOf(R.drawable.ngoc_son)), "true");
            db.insertHelper("Đỗ Lê Duẫn","0938895327","Male","14/03/2000","244 Bùi Hữu Nghĩa, Phường 2, Quận Bình Thạnh",
                "Work early in the mar",(float)4,String.valueOf(Integer.valueOf(R.drawable.le_duan)), "true");*/

            check = false;
        }

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is required");
                    return;
                }
                if(password.length() < 6){
                    mPassword.setError("Password must be six or more character!");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this,"Log in successfully" + fAuth.getCurrentUser().getUid()
                                    ,Toast.LENGTH_SHORT).show();
                            //getInforHelper();
                            startActivity(new Intent(Login.this, Maps.class));
                        }else{
                            Toast.makeText(Login.this,"Error !" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,SignIn.class));
            }
        });
    }


    private void writeToFile() {
        String file_name = String.valueOf(R.drawable.xuan_vinh);
        try {
            FileOutputStream fos = openFileOutput(file_name+".txt", 0);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write("Bùi Xuân Vĩnh" + '\n');
            bw.write("0989778966"+'\n');//primary
            bw.write("Male" +'\n');
            bw.write("14/09/2000" +'\n');
            bw.write("231/83/8A Dương Bá Trạc, Phường 1, Quận 8" + '\n');
            bw.write("Work early in the noon" +'\n');
            bw.write("2"+'\n');
            bw.write(String.valueOf(Integer.valueOf(R.drawable.xuan_vinh)) +'\n');//primary
            bw.write("true" +'\n');

            bw.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToFile1() {
        String file_name = String.valueOf(R.drawable.phuong_anh);
        try {
            FileOutputStream fos = openFileOutput(file_name+".txt", 0);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write("Cao Ngọc Sơn" + '\n');
            bw.write("0938895657"+'\n');
            bw.write("Male" +'\n');
            bw.write("19/07/2000" +'\n');
            bw.write("528/1A Minh Phụng, Phường 9, Quận 11" + '\n');
            bw.write("Work early in the sun" +'\n');
            bw.write("3"+'\n');
            bw.write(String.valueOf(Integer.valueOf(R.drawable.phuong_anh)) +'\n');
            bw.write("true" +'\n');

            bw.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToFile2() {
        String file_name = String.valueOf(R.drawable.le_duan);
        try {
            FileOutputStream fos = openFileOutput(file_name+".txt", 0);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write("Đỗ Lê Duẫn" + '\n');
            bw.write("0938895327"+'\n');
            bw.write("Male" +'\n');
            bw.write("14/03/2000" +'\n');
            bw.write("244 Bùi Hữu Nghĩa, Phường 2, Quận Bình Thạnh" + '\n');
            bw.write("Work early in the mar" +'\n');
            bw.write("4"+'\n');
            bw.write(String.valueOf(Integer.valueOf(R.drawable.le_duan)) +'\n');
            bw.write("true" +'\n');

            bw.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
