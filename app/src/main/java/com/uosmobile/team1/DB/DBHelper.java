package com.uosmobile.team1.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    //DB 정보
    static final String DB_NAME = "uosmobile.db";
    static final int DB_VERSION = 1;

    public Context context;

    //DB 테이블 이름
    private static final String TABLE_CONTENTS_INFORMATION = "CONTENTS_INFORMATIONS";
    
    //DB 테이블 생성문
    private static final String CREATE_TABLE_CONTENTS_INFORMATION =
            "create table if not exists "
            +TABLE_CONTENTS_INFORMATION+" ("
            +"contents_name text primary key , "
            +"contents_last_page integer, "
            +"contents_total_page integer);";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CONTENTS_INFORMATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+TABLE_CONTENTS_INFORMATION);
        onCreate(db);
    }

    public void onDelete(SQLiteDatabase db){
        db.execSQL("drop table if exists "+TABLE_CONTENTS_INFORMATION);
    }

    /*
    * CONTENTS_INFORMATIONS table methods
    * */
    public boolean insertContentsInformation(String contentsName, int contentsTotalPage){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("contents_name", contentsName);
        values.put("contents_last_page", 1);
        values.put("contents_total_page", contentsTotalPage);
        long result = db.insert(TABLE_CONTENTS_INFORMATION, null, values);

        if(result==-1)
            return false;
        else
            return true;
    }

    public boolean setContentsLastPage(String contentsName, int page){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("contents_name", contentsName);
        values.put("contents_last_page", page);
        db.update(TABLE_CONTENTS_INFORMATION,values, "contents_name = ?", new String[] {contentsName});
        return true;
    }

    public int getContentsLastPage(String contentsName){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select contents_last_page from "+ TABLE_CONTENTS_INFORMATION
                +" where contents_name = " + contentsName, null);

        int page = cursor.getInt(0);

        return page;
    }
}
