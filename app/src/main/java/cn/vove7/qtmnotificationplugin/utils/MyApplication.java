package cn.vove7.qtmnotificationplugin.utils;

import android.app.Application;

import cn.vove7.easytheme.EasyTheme;
import cn.vove7.easytheme.ThemeSet;
import cn.vove7.qtmnotificationplugin.MainActivity;
import cn.vove7.qtmnotificationplugin.QTWNotificationListener;

public class MyApplication extends Application {
   static MyApplication myApplication;
   private QTWNotificationListener QTWNotificationListener;

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
