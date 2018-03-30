package cn.vove7.qtmnotificationplugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 拨号*#*#789#*#*唤醒
 */
public class DialReceiver extends BroadcastReceiver {

   @Override
   public void onReceive(Context context, Intent intent) {
      Log.d(this.getClass().getName(), "onReceive: get!!!!!!!!!!!!!!!!!" );
      String host;
      if (intent.getData() != null) {
         host = intent.getData().getHost();
      } else return;
      if (host.equals("789")) {
         Intent it = new Intent(context, MainActivity.class);
         context.startActivity(it);
      }
   }
}
