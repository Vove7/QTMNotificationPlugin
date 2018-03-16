package cn.vove7.qtmnotificationplugin.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
   private static final String DB_NAME = "sql.db";
   private static final int version = 2;
   public static final String TABLE_NICKNAME = "nickname";

   public SQLiteHelper(Context context) {
      super(context, DB_NAME, null, version);
   }

   private static final String SQL_CREATE = "create table " + TABLE_NICKNAME +
           "(id integer primary key autoincrement ,nickname text,type text)";
   private static final String SQL_DROP = "drop table if exists " + TABLE_NICKNAME;

   @Override
   public void onCreate(SQLiteDatabase sqLiteDatabase) {
      sqLiteDatabase.execSQL(SQL_CREATE);
   }

   @Override
   public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
      if (i1 > i) {
         //表更新
         sqLiteDatabase.execSQL(SQL_DROP);
         onCreate(sqLiteDatabase);
      }
   }
}
