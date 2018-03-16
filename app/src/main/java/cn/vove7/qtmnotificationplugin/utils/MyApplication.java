package cn.vove7.qtmnotificationplugin.utils;

import android.app.Application;

import cn.vove7.easytheme.EasyTheme;
import cn.vove7.easytheme.ThemeSet;
import cn.vove7.qtmnotificationplugin.MainActivity;
import cn.vove7.qtmnotificationplugin.QTMNotificationListener;

public class MyApplication extends Application {
   static MyApplication myApplication;
   private QTMNotificationListener QTMNotificationListener;

   private MainActivity mainActivity;

   @Override
   public void onCreate() {
      super.onCreate();
      myApplication = this;
      EasyTheme.init(this, ThemeSet.ThemeMode.Light, ThemeSet.Theme.Primary);
   }

   public static MyApplication getInstance() {
      if (myApplication == null) {
         myApplication = new MyApplication();
      }
      return myApplication;
   }


   public QTMNotificationListener getQTMNotificationListener() {
      return QTMNotificationListener;
   }

   public void setQTMNotificationListener(QTMNotificationListener QTMNotificationListener) {
      this.QTMNotificationListener = QTMNotificationListener;
   }

   public MainActivity getMainActivity() {
      return mainActivity;
   }

   public void setMainActivity(MainActivity mainActivity) {
      this.mainActivity = mainActivity;
   }

}
