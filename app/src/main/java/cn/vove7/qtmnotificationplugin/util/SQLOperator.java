package cn.vove7.qtmnotificationplugin.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import static cn.vove7.qtmnotificationplugin.ManageFaActivity.PKG_TYPE_QQ_TIM;
import static cn.vove7.qtmnotificationplugin.QTMNotificationListener.TYPE_QQ_TIM;
import static cn.vove7.qtmnotificationplugin.QTMNotificationListener.TYPE_WECHAT;

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

   public ArrayList<String> getNickname(int pkgType, ArrayList<String> notIn) {
      StringBuilder notInBuilder = null;
      if (notIn != null && notIn.size() > 0) {
         notInBuilder = new StringBuilder("'" + notIn.get(0) + "'");
         for (int i = 1; i < notIn.size(); i++) {
            notInBuilder.append(",'").append(notIn.get(i)).append("'");
         }
      }

      SQLiteDatabase database = new SQLiteHelper(context).getReadableDatabase();
      StringBuilder sqlBuilder = new StringBuilder("select nickname from " + SQLiteHelper.TABLE_NICKNAME +
              " where type=?");
      if (notInBuilder != null) {
         sqlBuilder.append(" and nickname not in (").append(notInBuilder.toString()).append(")");
      }

      Cursor cursor = database.rawQuery(sqlBuilder.toString(),
              new String[]{pkgType == PKG_TYPE_QQ_TIM ? TYPE_QQ_TIM : TYPE_WECHAT});
      ArrayList<String> list = new ArrayList<>();
      while (cursor.moveToNext()) {
         list.add(cursor.getString(0));
      }
      cursor.close();
      database.close();
      return list;
   }


}
