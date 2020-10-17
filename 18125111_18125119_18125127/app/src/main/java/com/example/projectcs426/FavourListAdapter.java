package com.example.projectcs426;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class FavourListAdapter extends ArrayAdapter<HelperInfor> {

    public FavourListAdapter(@NonNull Context context, @NonNull ArrayList<HelperInfor> favourList) {
        super(context, 0, favourList);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = createRow(position, (ListView)parent);
        }
        return convertView;
    }

    private View createRow(int position, ListView parent) {
        View itemView = createARow(position);
        HelperInfor helperInfor = getItem(position);
        DisplayInfo(itemView, helperInfor);
        return itemView;

    }

    private View createARow(int position) {
        return LayoutInflater.from(this.getContext()).inflate(R.layout.fav_item, null);
    }

    private void DisplayInfo(View convertView, HelperInfor helperInfor){

        if(helperInfor != null){
            ImageView imageView = (ImageView) convertView.findViewById(R.id.fav_avt);
            imageView.setImageResource(helperInfor.getAvatar());


            TextView textView = convertView.findViewById(R.id.fav_FName);
            textView.setText(helperInfor.getHName());

            textView = convertView.findViewById(R.id.fav_Phone);
            textView.setText(helperInfor.getPhone());
        }

    }

}

