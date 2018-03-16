package cn.vove7.qtmnotificationplugin.utils;

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

   public ArrayList<String> queryNickname(String s, String type, ArrayList<String> notIn) {
      SQLiteDatabase database = new SQLiteHelper(context).getReadableDatabase();

      String sql = "select nickname from " + SQLiteHelper.TABLE_NICKNAME +
              " where type=? and nickname like ?";
      String notInSql = buildNotIn(notIn);
      if (notInSql != null) {
         sql += " and nickname not in " + notInSql;
      }
      Cursor cursor = database.rawQuery(sql,
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

   private String buildNotIn(ArrayList<String> notIn) {
      if (notIn == null || notIn.size() == 0)
         return null;

      StringBuilder notInBuilder = new StringBuilder("(");
      notInBuilder.append("'").append(notIn.get(0)).append("'");
      for (int i = 1; i < notIn.size(); i++) {
         notInBuilder.append(",'").append(notIn.get(i)).append("'");
      }
      notInBuilder.append(")");
      return notInBuilder.toString();
   }

   public ArrayList<String> getAllNickname(String type, ArrayList<String> notIn) {

      SQLiteDatabase database = new SQLiteHelper(context).getReadableDatabase();
      StringBuilder sqlBuilder = new StringBuilder("select nickname from " + SQLiteHelper.TABLE_NICKNAME +
              " where type=?");
      String notInSql = buildNotIn(notIn);
      if (notInSql != null) {
         sqlBuilder.append(" and nickname not in ").append(notInSql);
      }

      Cursor cursor = database.rawQuery(sqlBuilder.toString(),
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
