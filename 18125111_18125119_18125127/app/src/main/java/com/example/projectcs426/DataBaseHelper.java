package com.example.projectcs426;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String HelperInfor_TABLE_NAME = "HelperInfor";

    public static final String HelperInfor_ID = "ID"; // primary key
    public static final String HelperInfor_COLUMN_Name = "FullName";
    public static final String HelperInfor_COLUMN_Phone = "Phone";
    public static final String HelperInfor_COLUMN_SEX = "Gender";
    public static final String HelperInfor_COLUMN_DOB = "DOB";
    public static final String HelperInfor_COLUMN_Add = "Address";
    public static final String HelperInfor_COLUMN_Note = "Note";
    public static final String HelperInfor_COLUMN_RATING = "Rating";
    public static final String HelperInfor_COLUMN_AVATAR = "Avatar";
    public static final String HelperInfor_COLUMN_AVAILABLE = "Available";

    public DataBaseHelper(Context context){
        super(context,DATABASE_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS HelperInfor" +
                "(ID integer primary key autoincrement, FullName text, Phone text, Gender text, DOB text, Address text, Note text, Rating float, Avatar text, Available text)");

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS HireHelper" +
                "(UserID text primary key, FullName text, Phone text, Avatar text)");

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS FavHelper" +
                "(UserID text, HelperPhone text)");

        //create table user hire helper
        // create table user's favorite helper
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS HelperInfor");
        //onCreate(sqLiteDatabase);
    }

    public boolean insertHelper(String name, String phone, String gen, String dob, String add, String note, Float rate, String avatar, String avai){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("FullName",name);
        contentValues.put("Phone",phone);
        contentValues.put("Gender",gen);
        contentValues.put("DOB",dob);
        contentValues.put("Address",add);
        contentValues.put("Note",note);
        contentValues.put("Rating",rate);
        contentValues.put("Avatar",avatar);
        contentValues.put("Available",avai);
        db.insert("HelperInfor", null, contentValues);
        return true;
    }

    public boolean insertHireHelper(String userID, HelperInfor helperInfor){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("UserID", userID);
        contentValues.put("FullName", helperInfor.getHName());
        contentValues.put("Phone", helperInfor.getPhone());
        contentValues.put("Avatar", helperInfor.getAvatar());
        db.insert("HireHelper", null, contentValues);
        return true;
    }

    public Cursor getAllDataInHelperInfor(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM HelperInfor", null);
        return res;
    }

    public boolean updateHelperInfor(String phone){ // hired
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Available", "false");
        int i =0;
        i = db.update("HelperInfor" , contentValues, "Phone=?", new String[]{phone});
        if(i>0)
            return true;
        return false;
    }

    public boolean updateHelperInforFree(String phone, Float result){ // free
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Available", "true");
        contentValues.put("Rating", result);
        int i =0;
        i = db.update("HelperInfor" , contentValues, "Phone=?", new String[]{phone});
        if(i>0)
            return true;
        return false;
    }

    public Cursor getAllDataHireHelper(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM HireHelper", null);
        return res;
    }

    public boolean removeFromHireHelper(HelperInfor helperInfor, String userID){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM HireHelper WHERE UserID= " +"'"+ userID.toString()+"'" +" AND Phone= " + "'"+helperInfor.getPhone()+"'");
        //return db.delete("HireHelper", "UserID"+"="+userID + " AND Phone"+"="+helperInfor.getPhone(), null) >0;
        return true;
    }

    public Cursor getAllDataFavhelp(String userID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM FavHelper WHERE UserID=" + "'"+userID+"'", null);
        return res;
    }

    public Cursor getFavInfor(String Phone){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM HelperInfor WHERE Phone =" +"'"+ Phone +"'", null);
        return res;
    }

    public boolean removeFromFavHelper(HelperInfor helperInfor, String userID){
        SQLiteDatabase db = this.getWritableDatabase();
        //db.execSQL("DELETE FROM FavHelper WHERE UserID= " +"'"+ userID+"'" +" AND Phone= " + "'"+helperInfor.getPhone()+"'");
        if (db.delete("FavHelper", "UserID"+"="+"'"+userID+"'"+ " AND HelperPhone"+"="+"'"+helperInfor.getPhone()+"'", null) >0)
            return true;
        return false;
    }

    public boolean checkUserHired(String userID){
        SQLiteDatabase db =this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM HireHelper WHERE UserID=" + "'"+userID+"'", null);
        if(res.getCount() > 0)// tuc la co thue
            return false;
        return true;
    }

    public boolean addFavHelper(String userID, String Phone){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("UserID", userID);
        contentValues.put("HelperPhone", Phone);
        db.insert("FavHelper", null, contentValues);
        return true;
    }

    public boolean IsExistInFav(String userID, String helperphone){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res1 = db.rawQuery("SELECT * FROM FavHelper", null);
        if(res1.getCount() <= 0){ // chua co thich ai
            return false;
        }
        if(res1.moveToFirst()){
            do{
                if(res1.getString(0).equals(userID) && res1.getString(1).equals(helperphone)) //if exist
                    return true;
            }while(res1.moveToNext());
        }
        return false;
    }

}
