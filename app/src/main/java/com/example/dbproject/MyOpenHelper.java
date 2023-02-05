package com.example.dbproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class MyOpenHelper extends SQLiteOpenHelper {

    public static String DATABASENAME = "prueba";
    public static String COMMENTSTABLE = "comments";
    //public static String colCommentID = "id";
    //public static String colCommentTitle = "title";
    //public static String colCommentText = "text";
    private ArrayList<Comentari> comentariArrayList = new ArrayList<>();
    Context c;

    public MyOpenHelper(Context context){
        super(context,DATABASENAME,null,33);
        c = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE if not exists comments(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT ,text TEXT )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+COMMENTSTABLE);
        onCreate(db);
    }

    public void addComment(Comentari comentari){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title",comentari.getTitle());
        contentValues.put("text",comentari.getText());
        db.insert(COMMENTSTABLE,null,contentValues);
        db.close();
    }
    public ArrayList<Comentari> getComments(){
        comentariArrayList.clear();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+COMMENTSTABLE,null);
        if (cursor.getCount() != 0){
            if (cursor.moveToFirst()){
                do{
                    Comentari comentari = new Comentari();
                    comentari.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("id"))));
                    comentari.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                    comentari.setText(cursor.getString(cursor.getColumnIndexOrThrow("text")));
                    comentariArrayList.add(comentari);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return comentariArrayList;
    }
    public void removeComment(String commentTitle){
        try{
            String[] args = {commentTitle};
            getWritableDatabase().delete(COMMENTSTABLE,  "title=?",args);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
