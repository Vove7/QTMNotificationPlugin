package cn.vove7.qtmnotificationplugin.activity;

import android.content.Intent;
import android.os.Bundle;

import cn.vove7.easytheme.BaseThemeActivity;

/**
 * 防止隐藏桌面图标时MainActivity被杀...?
 */
public class SplashActivity extends BaseThemeActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
         startActivity(new Intent("cn.vove7.qtmnotificationplugin.MAIN"));
         finish();

   }

}
