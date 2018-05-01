package cn.vove7.qtmnotificationplugin.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.annotation.StringRes;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

import cn.vove7.qtmnotificationplugin.R;


public class SettingsHelper {

   private static SharedPreferences preferences;
   @SuppressLint("StaticFieldLeak")
   private static Context context;

   private static String $(int resId) {
      return context.getString(resId);
   }

   public static void initPreference(Context context) {
      SettingsHelper.context = context;
      if (preferences != null)
         return;
      preferences = PreferenceManager.getDefaultSharedPreferences(context);
   }

   /**
    * @param f       PreferenceFragment
    * @param keys    操作元素key
    * @param enabled 设置是否可用
    */
   public static void setOptionEnabled(PreferenceFragment f, Integer[] keys, boolean enabled) {
      for (int key : keys) {
         f.findPreference(f.getString(key)).setEnabled(enabled);
      }
   }

   public static void setOptionEnabled(PreferenceFragment f, Integer[][] keys, boolean enabled) {
      for (Integer[] key : keys) {
         int is = key[0];//是否受子开关控制
         f.findPreference(f.getString(key[1])).setEnabled(enabled);
         if (!enabled) {//不启用
            setOptionEnabled(f, Utils.subArr(key, 2, key.length), false);
         } else {
            boolean b = true;
            if (is == 1) {//受父开关控制
               b = ((SwitchPreference) f.findPreference(f.getString(key[1]))).isChecked();//子开关状态
            }
            setOptionEnabled(f, Utils.subArr(key, 2, key.length), b);//跟随父开关
         }
      }
   }

   public static void setValue(@StringRes int keyId, Object val) {
      setValue($(keyId), val);
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
      boolean b = editor.commit();

      Log.d(SettingsHelper.class.getName(), "setValue: success:" + b);
   }


   public static boolean isNoDistrubingOnQQ() {
      return getBoolean(R.string.key_is_no_distrubing_qq, false);
   }


   public static boolean isNoDistrubingOnWechat() {
      return getBoolean(R.string.key_is_no_distrubing_wechat, false);
   }

   public static String getNoDistrubingTimeQuantumQQ() {
      return getString(R.string.key_no_disturbing_time_quantum_qq, "23:00-06:00");
   }
   //public static String getNoDistrubingBeginTimeQQ() {
   //   return getString(R.string.key_no_disturbing_begin_qq, "23:00");
   //}
   //
   //public static String getNoDistrubingEndTimeQQ() {
   //   return getString(R.string.key_no_disturbing_end_qq, "06:00");
   //}

   public static String getNoDistrubingTimeQuantumWechat() {
      return getString(R.string.key_no_disturbing_time_quantum_wechat, "23:00-06:00");
   }
   //public static String getNoDistrubingBeginTimeWechat() {
   //   return getString(R.string.key_no_disturbing_begin_wechat, "23:00");
   //}
   //
   //public static String getNoDistrubingEndTimeWechat() {
   //   return getString(R.string.key_no_disturbing_end_wechat, "06:00");
   //}

   public static boolean isOpenFaOnNDQQ() {
      return getBoolean(R.string.key_is_fa_no_distrubing_qq, false);
   }

   public static boolean notifyAllMsgQQ() {
      return getBoolean(R.string.key_notify_all_member_qq, false);
   }

   public static boolean isOpenFaOnNDWechat() {
      return getBoolean(R.string.key_is_fa_no_distrubing_wechat, false);
   }

   public static String getString(@StringRes int keyId, String d) {
      return preferences.getString($(keyId), d);
   }

   public static String getString(String key, String d) {
      return preferences.getString(key, d);
   }

   public static int getInt(@StringRes int keyId, int d) {
      return preferences.getInt($(keyId), d);
   }

   public static int getInt(String key, int d) {
      return preferences.getInt(key, d);
   }

   public static boolean getBoolean(@StringRes int keyId, boolean d) {
      return preferences.getBoolean($(keyId), d);
   }

   public static Set<String> getStringSet(@StringRes int keyId, Set<String> d) {
      return preferences.getStringSet($(keyId), d);
   }

   public static boolean removeKey(@StringRes int key) {
      return preferences.edit().remove($(key)).commit();
   }

   public static Set<String> getFaSetQQ() {
      return new HashSet<>(getStringSet((R.string.key_fas_qq), new HashSet<>()));
   }

   public static Set<String> getFaSetWechat() {
      return new HashSet<>(getStringSet((R.string.key_fas_wechat), new HashSet<>()));
   }

   public static Set<String> getBlackListQQ() {
      return new HashSet<>(getStringSet((R.string.key_black_list_qq), new HashSet<>()));
   }


   public static Set<String> getBlackListWechat() {
      return new HashSet<>(getStringSet(
              (R.string.key_black_list_wechat), new HashSet<>()));
   }

   public static boolean getTotalSwitch() {
      return getBoolean((R.string.key_total_switch), true);
   }

   public static boolean getTotalSwitchQQ() {
      return getBoolean((R.string.key_total_switch_qq), true);
   }

   public static boolean getTotalSwitchWechat() {
      return getBoolean((R.string.key_total_switch_wechat), true);
   }

   public static String getRingtoneQQ() {
      return getString((R.string.key_ringtone_qq), null);
   }

   public static String getRingtoneWechat() {
      return getString((R.string.key_ringtone_wechat), null);
   }

   public static String getFaRingtoneQQ() {
      return getString((R.string.key_fa_ringtone_qq), null);
   }

   public static String getFaRingtoneWechat() {
      return getString((R.string.key_fa_ringtone_wechat), null);
   }

   public static boolean isVibratorQQ() {
      return getBoolean((R.string.key_is_vibrator_qq), true);
   }

   public static boolean isVibratorWechat() {
      return getBoolean((R.string.key_is_vibrator_wechat), true);
   }

   public static String getModeQQ() {
      return getString((R.string.key_mode_qq), context.getString(R.string.mode_default));
   }

   public static String getModeWechat() {
      return getString((R.string.key_mode_wechat), context.getString(R.string.mode_default));
   }

   private static int i(int resId) {
      return context.getResources().getInteger(resId);
   }

   public static int getVibratorStrengthQQ() {
      return getInt((R.string.key_vibrator_strength_qq),
              i(R.integer.default_vibrator_strength));
   }

   public static int getVibratorStrengthWechat() {
      return getInt((R.string.key_vibrator_strength_wechat),
              i(R.integer.default_vibrator_strength));
   }

   public static int getRepeatNumQQ() {
      return getInt((R.string.key_repeat_num_qq), 2);
   }


   public static int getRepeatNumWechat() {
      return getInt((R.string.key_repeat_num_wechat), 2);
   }

   public static boolean isAlarmQQ() {
      return getBoolean((R.string.key_is_alarm_qq), false);
   }

   public static boolean isAlarmWechat() {
      return getBoolean((R.string.key_is_alarm_wechat), false);
   }

   public static boolean isOnlyScreenOffQQ() {
      return getBoolean((R.string.key_is_only_screen_off_qq), false);
   }

   public static boolean isOnlyScreenOffWechat() {
      return getBoolean((R.string.key_is_only_screen_off_wechat), false);
   }
}
