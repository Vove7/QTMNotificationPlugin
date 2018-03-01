package cn.vove7.qtmnotificationplugin;

import android.app.Notification;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.vove7.qtmnotificationplugin.util.MyApplication;
import cn.vove7.qtmnotificationplugin.util.SQLOperator;
import cn.vove7.qtmnotificationplugin.util.SettingsHelper;
import cn.vove7.qtmnotificationplugin.util.Utils;

import static android.os.Build.VERSION_CODES.M;

public class QTMNotificationListener extends NotificationListenerService {
   private final String TAG = getClass().getName();
   public static boolean isConnect = false;

   public static final String PACKAGE_QQ = "com.tencent.mobileqq";
   public static final String PACKAGE_TIM = "com.tencent.tim";
   public static final String PACKAGE_MM = "com.tencent.mm";

   @Override
   public void onListenerConnected() {
      super.onListenerConnected();
      isConnect = true;
      MyApplication.getInstance().setQTMNotificationListener(this);
      //初始化配置
      SettingsHelper.initPreference(this);

      Log.d(TAG, "QTM提醒已上线");
      if (Build.VERSION.SDK_INT < M)
         Looper.prepare();
      Toast.makeText(this, R.string.qtm_online, Toast.LENGTH_SHORT).show();
   }

   @Override
   public void onListenerDisconnected() {
      super.onListenerDisconnected();
      Log.d(TAG, "QTM提醒已下线");
      isConnect = false;
   }
   public static final String TYPE_QQ_TIM="QQ/TIM";
   public static final String TYPE_WECHAT="WECHAT";

   private static final int TYPE_NOT_NOTIFY = 152;
   private static final int TYPE_QQ_OK = 102;
   private static final int TYPE_QQ_ZONE = 76;
   private static final int TYPE_QQ_PC_LOGIN = 520;
   private static final int TYPE_QQ_RELEVANCE = 444;
   private static final int TYPE_QQ_HELLO = 678;
   private static final int TYPE_QQ_PUBLIC = 595;
   private static final int TYPE_QQ_VERIFY_LOGIN = 191;
   private static final int TYPE_QQ_EMAIL = 859;
   private static final int TYPE_QQ_FRIEND = 373;

   @Override
   public void onNotificationPosted(StatusBarNotification sbn) {
      Log.d(TAG, "\n获得通知 PackageName --- " + sbn.getPackageName());

      Notification notification = sbn.getNotification();
      Bundle extras = notification.extras;
      String notificationTitle = extras.getString(Notification.EXTRA_TITLE);
      String notificationText = extras.getString(Notification.EXTRA_TEXT);


      Log.d(TAG, "标题：" + notificationTitle +
              "\n内容：" + notificationText + "\n");

      parsePackage(sbn.getPackageName(), notificationTitle, notificationText);//
      //Log.d("isOnGoing", "" + sbn.isOngoing());
   }

   /**
    * 根据包名分开处理
    *
    * @param packageName 包名索引
    * @param title       通知标题
    * @param content     通知内容
    */
   private void parsePackage(String packageName, String title, String content) {
      switch (packageName) {
         case PACKAGE_QQ://QQ
            //notifyQQOrTim(true, title, content);
            //break;
         case PACKAGE_TIM: //TIM
            notifyQQOrTim(title, content);
            break;

         case PACKAGE_MM: //MM
            notifyWechat(title, content);
            break;
         default:
      }
   }

   private static final String MODE_DEFAULT = "default";
   private static final String MODE_ONLY_FA = "mode_only_fa";

   private void notifyQQOrTim(String title, String content) {
      boolean totalSwitch = SettingsHelper.isTotalSwitchQQ();
      int notificationType = parseQQNotificationType(title, content);//放入昵称
      if (!totalSwitch) {
         return;
      }
      //获得好友昵称
      String nickname = getQQNickname(title, content);
      Log.d(TAG, "parseQQNotificationType: 昵称： " + nickname);

      //消息类型
      String mode = SettingsHelper.getModeQQ();
      //仅熄屏
      if ((SettingsHelper.isOnlyScreenOffQQ())
              && Utils.isScreenOn(this)) {
         Log.d(TAG, "notifyQQOrTim: screen on");
         return;
      }
      //blacklist
      Set<String> blackList = SettingsHelper.getBlackListQQ();
      if (blackList != null && blackList.contains(nickname)) {
         Log.d(TAG, "notifyQQOrTim: in black list: " + nickname);
         return;
      }
      //免打扰
      boolean isNoDistrubingQuantum = SettingsHelper.isNoDistrubingOnQQ();
      String begin = SettingsHelper.getNoDistrubingBeginTimeQQ();
      String end = SettingsHelper.getNoDistrubingEndTimeQQ();

      if (isNoDistrubingQuantum && Utils.inTimeQuantum(begin, end, null)) {
         Log.d(TAG, "notifyQQOrTim: no distrubing time quantum");
         boolean isFaOnND = SettingsHelper.isOpenFaOnNDQQ();
         if (!(isFaOnND && isFaQQ(nickname))) {//没打开 或者打开不在特别关心中
            Log.d(TAG, "notifyQQOrTim: 没打开 或者不在特别关心中");
            return;
         }
      }
      switch (mode) {
         case MODE_ONLY_FA: {
            if (!isFaQQ(nickname)) {//不是特别关心
               Log.d(TAG, "notifyQQOrTim: not fa ");
               return;
            }
         }
         case MODE_DEFAULT: {//默认
            switch (notificationType) {
               case TYPE_NOT_NOTIFY:
                  return;
               case TYPE_QQ_PC_LOGIN:
                  return;
               case TYPE_QQ_OK:
                  new SQLOperator(this).insertNickname(nickname, TYPE_QQ_TIM);
               case TYPE_QQ_RELEVANCE:
               case TYPE_QQ_ZONE:
               case TYPE_QQ_EMAIL:
                  boolean isVibrator = SettingsHelper.isVibratorQQ();
                  int vibratorStrength = SettingsHelper.getVibratorStrengthQQ();
                  int repeatNum = SettingsHelper.getRepeatNumQQ();
                  boolean isAlarm = SettingsHelper.isAlarmQQ();
                  String ringtone = SettingsHelper.getRingtoneQQ();
                  if (isVibrator)
                     Utils.notificationVibrator(this,
                             vibratorStrength,
                             repeatNum);
                  if (isAlarm)
                     Utils.startAlarm(this, ringtone);
                  break;
               case TYPE_QQ_PUBLIC:
               case TYPE_QQ_VERIFY_LOGIN:
               case TYPE_QQ_HELLO: {

               }
               break;
            }
         }
         break;
      }
   }

   private boolean isFaQQ(String nickname) {
      Set<String> faSet = SettingsHelper.getFaSetQQ();
      return faSet != null && faSet.contains(nickname);
   }

   private void notifyWechat(String title, String content) {

      boolean isVibrator = SettingsHelper.isVibratorWechat();
      int vibratorStrength = SettingsHelper.getVibratorStrengthWechat();
      int repeatNum = SettingsHelper.getRepeatNumWechat();
      boolean isAlarm = SettingsHelper.isAlarmWechat();
      String ringtone = SettingsHelper.getRingtoneWechat();
      if (isVibrator)
         Utils.notificationVibrator(this,
                 vibratorStrength,
                 repeatNum);
      if (isAlarm)
         Utils.startAlarm(this, ringtone);
   }

   /**
    * 解析QQ通知
    *
    * @param title   标题
    * @param content 内容
    * @return 返回类型
    */
   private int parseQQNotificationType(String title, String content) {
      //QQ后台语音通话 or 后台提示
      if (title == null || ("QQ".equals(title) && "QQ正在后台运行".equals(content))) {
         Log.d(TAG, "parseQQNotificationType: title:null");
         return TYPE_NOT_NOTIFY;
      }
      Matcher matcher = Pattern.compile("QQ空间动态(\\(共(\\d+)条未读\\))?$").matcher(title);
      if (matcher.find()) {
         Log.d(TAG, "parseQQNotificationType: QQ空间");
         return TYPE_QQ_ZONE;
      }
      switch (title) {
         case "你的帐号在电脑登录":
            return TYPE_QQ_PC_LOGIN;
         case "一键验证":
            return TYPE_QQ_VERIFY_LOGIN;
         case "关联QQ号":
            return TYPE_QQ_RELEVANCE;
         case "一声问候":
            return TYPE_QQ_HELLO;
         case "邮件提醒":
            return TYPE_QQ_EMAIL;
         case "公众号":
            return TYPE_QQ_PUBLIC;
         case "朋友通知":
            return TYPE_QQ_FRIEND;
      }
      //好友-群-组..
      Log.d(TAG, "parseQQNotificationType: 普通消息");
      return TYPE_QQ_OK;
   }

   /**
    * 获取昵称
    *
    * @param title   标题
    * @param content 内容
    * @return 返回昵称
    */
   private String getQQNickname(String title, String content) {
      String regexMessageNum = "\\((\\d+)条(以上)?新消息\\)$";
      Matcher matcherNum = Pattern.compile(regexMessageNum).matcher(title);

      String regexMsg = "有\\s(\\d+)\\s个联系人给你发过来(\\d+)条新消息";
      Matcher matcherQQMultiPeo = Pattern.compile(regexMsg).matcher(content);

      if (matcherNum.find())
         return title.substring(0, matcherNum.start() - 1);
      else if ("QQ".equals(title) || "TIM".equals(title) && matcherQQMultiPeo.find()) {
         return null;
      }
      return title;
   }
}