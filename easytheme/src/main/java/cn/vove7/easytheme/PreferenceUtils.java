package cn.vove7.easytheme;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Set;

public class PreferenceUtils {
   private static SharedPreferences preferences;
   @SuppressLint("StaticFieldLeak")
   private static Context context;

   public static void initPreference(Context context) {
      PreferenceUtils.context = context;
      if (preferences != null)
         return;
      preferences = PreferenceManager.getDefaultSharedPreferences(context);
   }

   private static String $(int resId) {
      return context.getString(resId);
   }

   public static int getThemeId() {
      return preferences.getInt($(R.string.key_easy_theme_id), R.style.Light_Primary);
   }

   public static ThemeSet.Theme getTheme() {
      String theme = preferences.getString($(R.string.key_easy_theme), null);
      return theme == null ? ThemeSet.Theme.Primary : ThemeSet.Theme.valueOf(theme);
   }

   public static ThemeSet.ThemeMode getThemeMode() {
      String theme = preferences.getString($(R.string.key_easy_theme_mode), null);
      return theme == null ? ThemeSet.ThemeMode.Light : ThemeSet.ThemeMode.valueOf(theme);
   }

   public static void setValue(String key, Object val) {
      SharedPreferences.Editor editor = preferences.edit();
      if (val instanceof Boolean) {
         editor.putBoolean(key, (boolean) val);
      } else if (val instanceof Integer) {
         editor.putInt(key, (Integer) val);
      } else if (val instanceof Long) {
         editor.putLong(key, (Long) val);
      } else if (val instanceof Float) {
         editor.putFloat(key, (Float) val);
      } else if (val instanceof String) {
         editor.putString(key, (String) val);
      } else if (val instanceof Set) {
         editor.putStringSet(key, (Set<String>) val);
      }
      editor.apply();
   }
}
