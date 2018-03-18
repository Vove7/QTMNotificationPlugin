package cn.vove7.qtmnotificationplugin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 *防止隐藏桌面图标时MainActivity被杀...?
 */
public class SplashActivity extends AppCompatActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      startActivity(new Intent("cn.vove7.qtmnotificationplugin.MAIN")
              .addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS));
      finish();
   }
}
