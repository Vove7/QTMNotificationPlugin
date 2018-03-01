package cn.vove7.qtmnotificationplugin.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class SQLOperator {
   private Context context;
   private SQLiteHelper sqLiteHelper;

   public SQLOperator(Context context) {
      this.context = context;
      sqLiteHelper = new SQLiteHelper(context);
   }

   public void insertNickname(String nickname, String type) {
      if (nickname != null) {
         if (hasNickname(nickname, type)) {
            return;
         }
         SQLiteDatabase database = sqLiteHelper.getWritableDatabase();
         ContentValues values = new ContentValues();
         values.put("nickname", nickname);
         values.put("type", type);
         database.insert(SQLiteHelper.TABLE_NICKNAME, null, values);
         database.close();
      }
   }

   public ArrayList<String> searchNickname(String s, String type) {
      SQLiteDatabase database = new SQLiteHelper(context).getReadableDatabase();

      Cursor cursor = database.rawQuery("select nickname from " + SQLiteHelper.TABLE_NICKNAME +
                      " where type=? and nickname like ?",
              new String[]{type, '%' + s + '%'});
      ArrayList<String> list = new ArrayList<>();
      while (cursor.moveToNext()) {
         list.add(cursor.getString(0));
      }
      cursor.close();
      database.close();
      return list;
   }

   private boolean hasNickname(String name, String type) {
      SQLiteDatabase database = new SQLiteHelper(context).getReadableDatabase();
      Cursor cursor = database.rawQuery("select * from " + SQLiteHelper.TABLE_NICKNAME +
                      " where nickname=? and type=?",
              new String[]{name, type});
      boolean b = cursor.moveToNext();
      cursor.close();
      database.close();
      return b;
   }

   public ArrayList<String> getNickname(String type) {
      SQLiteDatabase database = new SQLiteHelper(context).getReadableDatabase();
      Cursor cursor = database.rawQuery("select nickname from " + SQLiteHelper.TABLE_NICKNAME +
                      " where type=?",
              new String[]{type});
      ArrayList<String> list = new ArrayList<>();
      while (cursor.moveToNext()) {
         list.add(cursor.getString(0));
      }
      cursor.close();
      database.close();
      return list;
   }


}
