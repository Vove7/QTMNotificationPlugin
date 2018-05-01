package cn.vove7.qtmnotificationplugin.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import cn.vove7.easytheme.BaseThemeActivity;
import cn.vove7.qtmnotificationplugin.R;
import cn.vove7.qtmnotificationplugin.utils.SettingsHelper;

/**
 * 防止隐藏桌面图标时MainActivity被杀...?
 */
public class SplashActivity extends BaseThemeActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      if(!initNewVersion()) {
         startActivity(new Intent("cn.vove7.qtmnotificationplugin.MAIN"));
         finish();
      }
   }

   boolean initNewVersion() {
      PackageInfo info;
      try {
         info = getPackageManager().getPackageInfo(this.getPackageName(), 0);

         int currentVersion = info.versionCode;
         SettingsHelper.initPreference(this);
         int lastVersion = SettingsHelper.getInt(R.string.key_version, currentVersion);
         Log.d("SplashActivity :", "initNewVersion  ----> " + lastVersion + " --> " + currentVersion);
         if (currentVersion > lastVersion) {
            int repeateNumQQ = Integer.parseInt(SettingsHelper.getString(R.string.key_repeat_num_qq, "2"));
            int repeateNumW = Integer.parseInt(SettingsHelper.getString(R.string.key_repeat_num_wechat, "2"));
            SettingsHelper.removeKey(R.string.key_repeat_num_qq);
            SettingsHelper.removeKey(R.string.key_repeat_num_wechat);
            SettingsHelper.setValue(R.string.key_repeat_num_qq,repeateNumQQ);
            SettingsHelper.setValue(R.string.key_repeat_num_wechat,repeateNumW);

            SettingsHelper.setValue(R.string.key_version, currentVersion);
            return false;
         }
      } catch (PackageManager.NameNotFoundException e) {
         Log.d("SplashActivity :", "initNewVersion  ----> 新版本初始化出错");
         e.printStackTrace();
      }
      return false;
   }
}
