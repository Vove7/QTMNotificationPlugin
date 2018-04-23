package cn.vove7.qtmnotificationplugin.service;

import android.annotation.TargetApi;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;
import android.widget.Toast;

import cn.vove7.qtmnotificationplugin.R;
import cn.vove7.qtmnotificationplugin.utils.SettingsHelper;

@TargetApi(Build.VERSION_CODES.N)
public class AppTileService extends TileService {
   private static final int STATE_OFF = 0;
   private static final int STATE_ON = 1;
   private int toggleState = STATE_ON;

   public AppTileService() {
   }

   @Override
   public void onTileAdded() {
      Log.d("AppTileService :", "onTileAdded  ----> ");
      super.onTileAdded();
   }

   @Override
   public void onTileRemoved() {
      Log.d("AppTileService :", "onTileRemoved  ----> ");
      super.onTileRemoved();
   }

   @Override
   public void onStartListening() {
      SettingsHelper.initPreference(this);
      if (SettingsHelper.getTotalSwitch()) {
         open();
      } else {
         close();
      }

      Log.d("AppTileService :", "onStartListening  ----> ");
      super.onStartListening();
   }

   @Override
   public void onClick() {
      Log.d("AppTileService :", "onClick  ----> ");
      if (toggleState == STATE_ON) {
         close();
         Toast.makeText(this, R.string.qtm_offline, Toast.LENGTH_SHORT).show();
      } else {
         open();
         Toast.makeText(this, R.string.qtm_online, Toast.LENGTH_SHORT).show();
      }
   }

   private void close() {
      Icon icon = Icon.createWithResource(getApplicationContext(), R.drawable.ic_qq);
      getQsTile().setState(Tile.STATE_INACTIVE);// 更改成非活跃状态

      SettingsHelper.setValue(R.string.key_total_switch, false);
      toggleState = STATE_OFF;
      getQsTile().setIcon(icon);//设置图标
      getQsTile().updateTile();//更新Tile
   }

   private void open() {
      Icon icon = Icon.createWithResource(getApplicationContext(), R.drawable.ic_qq);
      getQsTile().setState(Tile.STATE_ACTIVE);// 更改成非活跃状态

      SettingsHelper.setValue(R.string.key_total_switch, true);
      toggleState = STATE_ON;
      getQsTile().setIcon(icon);//设置图标
      getQsTile().updateTile();//更新Tile
   }

   @Override
   public void onStopListening() {
      Log.d("AppTileService :", "onStopListening  ----> ");
      super.onStopListening();
   }
}
