package com.example.projectcs426;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.Rating;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class CurrentHelperHired extends AppCompatActivity implements View.OnClickListener {

    Intent intentIn;
    Button vote, addtoFav;
    ImageView avt;
    TextView name, phone;
    HelperInfor mhelper = null;
    DataBaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_current_helper_hired);

        db = new DataBaseHelper(this);

        getCurrentHiredHelper(); // from HireHelper table

        initComponents();

        //if(mhelper != null){
        vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CurrentHelperHired.this);
                LinearLayout linearLayout = new LinearLayout(CurrentHelperHired.this);
                RatingBar ratingBar = new RatingBar(CurrentHelperHired.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                ratingBar.setLayoutParams(lp);
                ratingBar.setNumStars(5);
                ratingBar.setStepSize((float)0.5);

                linearLayout.addView(ratingBar);

                builder.setTitle("Your vote to this helper: ");
                builder.setView(linearLayout);

                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                        float score = ratingBar.getRating();
                        float result = (score + mhelper.Rating) /2;
                        if(mhelper!=null) {
                            //mhelper.setRating(result);
                            //mhelper.setAvailable(true);

                            boolean done = modifyInHelperInfor(mhelper, view, result);

                            if (done) {
                                boolean check1 = db.removeFromHireHelper(mhelper, FirebaseAuth.getInstance().getCurrentUser().getUid());
                                if (check1) {
                                    Snackbar.make(view, "Thanks a lot...", Snackbar.LENGTH_LONG)
                                            .setAction("DISMISS", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                }
                                            }).show();
                                }

                            }
                            else {
                                Toast.makeText(CurrentHelperHired.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });

                builder.setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                startActivity(new Intent(CurrentHelperHired.this, Maps.class));
                            }

                        })

                        // Button Cancel
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });


                builder.create();
                builder.show();
            }
        });

        addtoFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checkAdd, checkExist = false;
                // check if that helper already exist or not
                checkExist = db.IsExistInFav(FirebaseAuth.getInstance().getCurrentUser().getUid(), mhelper.getPhone());
                if(!checkExist){
                    // if not you can add
                    checkAdd = db.addFavHelper(FirebaseAuth.getInstance().getCurrentUser().getUid(), mhelper.getPhone());
                    if (checkAdd) {
                        Snackbar.make(view, "You choose this as your favorite helper", Snackbar.LENGTH_LONG)
                                .setAction("DISMISS", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    }
                                }).show();

                        Intent intentToCurrent = new Intent(CurrentHelperHired.this, FavoriteHelper.class);
                        startActivity(intentToCurrent);
                        // move to Favorite class
                        
                    } else {
                        Snackbar.make(view, "Something went wrong", Snackbar.LENGTH_LONG)
                                .setAction("DISMISS", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    }
                                }).show();
                    }
                }
                else {// he already exist
                    Snackbar.make(view, "This helper had been added to favorite", Snackbar.LENGTH_LONG)
                            .setAction("DISMISS", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }).show();

                }

            }
        });
    }

    private boolean modifyInHelperInfor(HelperInfor mhelper, View view, Float result) {
        boolean check =false;
        check = db.updateHelperInforFree(mhelper.getPhone(), result);
        if(check){
            Snackbar.make(view, "Thanks for your support", Snackbar.LENGTH_LONG)
                    .setAction("DISMISS", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();

            return true;
        }

        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        return false;

    }


    private void getCurrentHiredHelper() {

        Cursor res = db.getAllDataHireHelper();
        if(res.moveToFirst()){
            mhelper = new HelperInfor();
            do{
                mhelper.setHName(res.getString(1));
                mhelper.setPhone(res.getString(2));
                mhelper.setAvatar(Integer.valueOf(res.getString(3)));
            }while(res.moveToNext());


        }

        else {
            Toast.makeText(this, "Nothing here", Toast.LENGTH_SHORT).show();
        }
        /*
        FileInputStream fis = null;
        String file_name = FirebaseAuth.getInstance().getCurrentUser().getUid();

        try {
            fis = openFileInput(file_name + ".txt");
            InputStreamReader reader = new InputStreamReader(fis, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            String text = null;
            StringBuilder sb = new StringBuilder();
            int k = 7;
            if((text = bufferedReader.readLine()) != null){
                mhelper = new HelperInfor();
                mhelper.setHName(text);
            }
            while ((text = bufferedReader.readLine()) != null) {
                sb.append(text).append('\n');
                if (k == 7) {
                    mhelper.setPhone(text);
                    k--;
                } else if (k == 6) {
                    mhelper.setGender(text);
                    k--;
                } else if (k == 5) {
                    mhelper.setDOB(text);
                    k--;
                } else if (k == 4) {
                    mhelper.setAddress(text);
                    k--;
                } else if (k == 3) {
                    mhelper.setNotes(text);
                    k--;
                } else if (k == 2) {
                    mhelper.setRating(Float.parseFloat(text));
                    k--;
                } else if (k == 1) {
                    mhelper.setAvatar(Integer.parseInt(text));
                    k--;
                } else if (k == 0) {
                    mhelper.setAvailable(Boolean.parseBoolean(text));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/
    }

    private void initComponents() {
        vote=findViewById(R.id.vote);
        avt=findViewById(R.id.Cavt);
        name = findViewById(R.id.CName);
        phone = findViewById(R.id.Cphone);
        addtoFav = findViewById(R.id.addToFav);

        if(mhelper != null){
            avt.setImageDrawable(getResources().getDrawable(mhelper.avatar));
            name.setText(mhelper.HName);
            phone.setText(mhelper.phone);
        }
    }

    private void clearToUsersFile() {
            String file_name = FirebaseAuth.getInstance().getCurrentUser().getUid();
            try {
                FileOutputStream fos = openFileOutput(file_name+".txt", 0);
                OutputStreamWriter osw = new OutputStreamWriter(fos);
                BufferedWriter bw = new BufferedWriter(osw);
                bw.write("");

                bw.close();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private void writeToHelperFile() {
        String file_name = String.valueOf(mhelper.avatar);
        try {
            FileOutputStream fos = openFileOutput(file_name+".txt", 0);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(mhelper.getHName() + '\n');
            bw.write(mhelper.getPhone()+'\n');
            bw.write(mhelper.getGender() +'\n');
            bw.write(mhelper.getDOB() +'\n');
            bw.write(mhelper.getAddress() + '\n');
            bw.write(mhelper.getNotes() +'\n');
            bw.write(mhelper.getRating().toString()+'\n');
            bw.write(String.valueOf(Integer.valueOf(mhelper.avatar)) +'\n');
            bw.write(String.valueOf(mhelper.available) +'\n');

            bw.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View view) {

    }
}