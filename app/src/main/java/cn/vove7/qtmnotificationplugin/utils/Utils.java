package cn.vove7.qtmnotificationplugin.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.os.Vibrator;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.vove7.qtmnotificationplugin.R;

public class Utils {
   public static boolean isScreenOn(Context context) {
      PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

      return powerManager != null && powerManager.isInteractive();
   }

   public static String arr2Str(String[] arr) {
      StringBuilder builder = new StringBuilder("[");
      for (String s : arr) {
         builder.append(s).append(",");
      }
      builder.append("]");
      return builder.toString();
   }

   /**
    * 截取数组
    * from should &gt(<) to
    *
    * @param arr  Object数组
    * @param from int
    * @param to   int
    * @return 子数组
    */
   public static Integer[] subArr(Integer[] arr, int from, int to) {
      if (from > to) {
         try {
            throw new Exception("from >= to");
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
      from = from < 0 ? 0 : from;
      to = to >= arr.length ? arr.length : to;
      int index = 0;
      Integer[] subArr = new Integer[to - from];
      for (int i = from; i < to; i++) {
         subArr[index++] = arr[i];
      }
      return subArr;
   }

   /**
    * @param begin 区间开始时间
    * @param end   区间结束时间
    * @param bet   中间点 null默认当前时间
    * @return bet是否处于指定时间段内
    */
   public static boolean inTimeQuantum(String begin, String end, String bet) {
      if (bet == null) {
         @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
         bet = dateFormat.format(new Date());
      }

      String[] now = bet.split(":");
      String[] beg = begin.split(":");
      String[] en = end.split(":");
      if (gt(en, beg)) {
         return gt(en, now) && gt(now, beg);
      }
      return !(gt(beg, now) && gt(now, en));
   }

   //???
   public static boolean gt(String[] v1, String[] v2) {
      if (i(v1[0]) > i(v2[0]))
         return true;
      else if (i(v1[0]) == i(v2[0]))
         return i(v1[1]) >= i(v2[1]);
      return false;
   }

   public static int i(String v) {
      return Integer.parseInt(v);
   }

   public static void notificationVibrator(Context context, int time, int repeatNum) {
      Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

      long[] pattern = new long[repeatNum * 2];
      //生成震动[]
      for (int i = 0; i < repeatNum; i++) {
         pattern[2 * i] = 200;
         pattern[2 * i + 1] = time;
      }
      if (vibrator != null) {
         vibrator.vibrate(pattern, -1); //震动一次，-1
      }
   }

   private static Ringtone ringtone;

   public static void startAlarm(Context context, String ring) {
      AudioAttributes audioAttributes = new AudioAttributes.Builder()
              .setUsage(SettingsHelper.getInt(R.string.key_volume_with, AudioAttributes.USAGE_NOTIFICATION))
              .build();
      Uri uri;
      if (ring == null || ring.equals("")) {
         uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
         if (uri == null) return;
      } else {
         uri = Uri.parse(ring);
      }
      if (ringtone != null && ringtone.isPlaying())
         ringtone.stop();
      ringtone = RingtoneManager.getRingtone(context, uri);
      ringtone.setAudioAttributes(audioAttributes);
      ringtone.play();
   }

   public static String getRingtoneTitleFromUri(Context context, String uri) {
      if (TextUtils.isEmpty(uri)) {
         return "";
      }
      Ringtone ringtone = RingtoneManager.getRingtone(context, Uri.parse(uri));
      return ringtone.getTitle(context);
   }
}
