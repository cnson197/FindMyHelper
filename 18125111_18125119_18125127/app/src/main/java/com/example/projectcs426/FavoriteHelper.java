package com.example.projectcs426;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class FavoriteHelper extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<HelperInfor> favourList;
    DataBaseHelper dataBaseHelper;
    Intent intentOut;
    Button removeFromFav;
    ListView favoriteListview;
    ActionBar actionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.favorite_list);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Favorite Helper(s)");

        favoriteListview = findViewById(R.id.favList);
        dataBaseHelper = new DataBaseHelper(this);

        //removeFromFav = findViewById(R.id.removeFFav);

        favourList = getFavourList();//get infor in favourList of this user
        setListView();


        

    }

    private void setListView() {
        //ListView favoriteListview = findViewById(R.id.favList);

        final HelperInfor tmp = new HelperInfor();
        if(favourList != null){
            final FavourListAdapter favourListAdapter = new FavourListAdapter(this, favourList);
            favoriteListview.setAdapter(favourListAdapter);
            favoriteListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                    //get all information from this item
                    // then move to profile activity
                    //tmp = favourList.get(i);
                    AlertDialog.Builder builder = new AlertDialog.Builder(FavoriteHelper.this);
                    builder.setTitle("You Want TO: ");
                    builder.setPositiveButton("Go and Hire",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //dialog.dismiss();
                                    intentOut = new Intent(FavoriteHelper.this, HelperProfile.class);
                                    if (favourList.get(i) != null) {
                                        Bundle arg = new Bundle();

                                        arg.putParcelable("helperInf", (Parcelable) favourList.get(i));

                                        intentOut.putExtra("bundle_arg", arg);

                                        startActivity(intentOut);
                                    } else {
                                        Toast.makeText(FavoriteHelper.this, "Something wrong!", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            })

                            .setNeutralButton("Remove", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int a) {
                                    if(dataBaseHelper.removeFromFavHelper(favourList.get(i), FirebaseAuth.getInstance().getUid())){
                                        favourList.remove(i);
                                        //favourList = new ArrayList<HelperInfor>();
                                        //favourList = getFavourList();
                                        //favourListAdapter.notifyDataSetChanged();
                                        favoriteListview.setAdapter(favourListAdapter);
                                        Toast.makeText(FavoriteHelper.this, "Removed", Toast.LENGTH_SHORT).show();
                                    }
                                    else Toast.makeText(FavoriteHelper.this, "Something Wrong!!", Toast.LENGTH_SHORT);
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

       }
        else {
            Snackbar.make(findViewById(R.id.favList), "Don't have any favorite to show", Snackbar.LENGTH_LONG)
                    .setAction("DISMISS", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();
        }


    }

    private HelperInfor findHelperFavourInfo(int avatarID) {
        HelperInfor helperInfor = null;
        for (int i = 0 ; i < favourList.size();i++)
        {
            helperInfor = favourList.get(i);
            if (helperInfor.getAvatar() == avatarID){
                return helperInfor;
            }
        }
        return null;
    }

    private ArrayList<HelperInfor> getFavourList() {
        HelperInfor helperInfor = null;
        ArrayList<HelperInfor> favourList =null;
        Cursor fav = dataBaseHelper.getAllDataFavhelp(FirebaseAuth.getInstance().getUid());
        DataBaseHelper dataBaseHelper1 = new DataBaseHelper(this);
        if(fav.moveToFirst()){
            favourList = new ArrayList<HelperInfor>();
            do{
                helperInfor = new HelperInfor();
                helperInfor.setPhone(fav.getString(1));
                Cursor fav1 = dataBaseHelper1.getFavInfor(fav.getString(1));
                if(fav1.moveToFirst()){
                    do{
                        helperInfor.setHName(fav1.getString(1));
                        helperInfor.setGender(fav1.getString(3));
                        helperInfor.setDOB(fav1.getString(4));
                        helperInfor.setAddress(fav1.getString(5));
                        helperInfor.setNotes(fav1.getString(6));
                        helperInfor.setRating(Float.valueOf(fav1.getString(7)));
                        helperInfor.setAvatar(Integer.valueOf(fav1.getString(8)));
                        helperInfor.setAvailable(Boolean.valueOf(fav1.getString(9)));
                    }while(fav1.moveToNext());
                }
                favourList.add(helperInfor);
            }while(fav.moveToNext());
        }
        return favourList;
    }

    @Override
    public void onClick(View view) {

    }
}
