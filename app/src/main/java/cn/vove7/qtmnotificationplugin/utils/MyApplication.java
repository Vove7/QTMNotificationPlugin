package cn.vove7.qtmnotificationplugin.utils;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.util.Log;
import android.util.SparseArray;

import cn.vove7.easytheme.EasyTheme;
import cn.vove7.easytheme.ThemeSet;
import cn.vove7.qtmnotificationplugin.R;
import cn.vove7.qtmnotificationplugin.activity.MainActivity;
import cn.vove7.qtmnotificationplugin.service.QTWNotificationListener;

public class MyApplication extends Application {
   static MyApplication myApplication;
   private QTWNotificationListener QTWNotificationListener;

   private MainActivity mainActivity;

   @Override
   public void onCreate() {
      super.onCreate();
      myApplication = this;
      EasyTheme.init(this, ThemeSet.ThemeMode.Light, ThemeSet.Theme.Primary);
      initTextResources();
      initNewVersion();
   }

   void initNewVersion() {
      PackageInfo info;
      try {
         info = getPackageManager().getPackageInfo(this.getPackageName(), 0);

         int currentVersion = info.versionCode;
         SettingsHelper.initPreference(this);
         int lastVersion = SettingsHelper.getInt(R.string.key_version, 0);
         Log.d("MyApplication :", "initNewVersion  ----> " + lastVersion + " --> " + currentVersion);
         switch (currentVersion) {
            case 4: {
               int repeatNumQQ = Integer.parseInt(SettingsHelper.getString(R.string.key_repeat_num_qq, "2"));
               int repeatNumW = Integer.parseInt(SettingsHelper.getString(R.string.key_repeat_num_wechat, "2"));
               SettingsHelper.removeKey(R.string.key_repeat_num_qq);
               SettingsHelper.removeKey(R.string.key_repeat_num_wechat);
               SettingsHelper.setValue(R.string.key_repeat_num_qq, repeatNumQQ);
               SettingsHelper.setValue(R.string.key_repeat_num_wechat, repeatNumW);
            }
            break;
         }
         if (currentVersion > lastVersion) {
            SettingsHelper.setValue(R.string.key_version, currentVersion);
         }
      } catch (PackageManager.NameNotFoundException e) {
         Log.d("SplashActivity :", "initNewVersion  ----> 新版本初始化出错");
         e.printStackTrace();
      }
   }

   public static MyApplication getInstance() {
      if (myApplication == null) {
         myApplication = new MyApplication();
      }
      return myApplication;
   }


   public static SparseArray<String> TextVolumeWith  = new SparseArray<>();

   private void initTextResources() {
      initTextVolumeWith();
   }

   private void initTextVolumeWith() {
      TextVolumeWith.put(AudioAttributes.USAGE_MEDIA, getString(R.string.text_media_volume));
      TextVolumeWith.put(AudioAttributes.USAGE_NOTIFICATION, getString(R.string.text_notification_volume));
      TextVolumeWith.put(AudioAttributes.USAGE_NOTIFICATION_RINGTONE, getString(R.string.text_ringtone_volume));
   }


   public QTWNotificationListener getQTWNotificationListener() {
      return QTWNotificationListener;
   }

   public void setQTWNotificationListener(QTWNotificationListener QTWNotificationListener) {
      this.QTWNotificationListener = QTWNotificationListener;
   }

   public MainActivity getMainActivity() {
      return mainActivity;
   }

   public void setMainActivity(MainActivity mainActivity) {
      this.mainActivity = mainActivity;
   }

}
