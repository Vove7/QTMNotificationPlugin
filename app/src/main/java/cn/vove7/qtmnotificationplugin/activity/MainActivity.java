package cn.vove7.qtmnotificationplugin.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import cn.vove7.easytheme.BaseThemeActivity;
import cn.vove7.easytheme.EasyTheme;
import cn.vove7.qtmnotificationplugin.R;
import cn.vove7.qtmnotificationplugin.fragments.QQSettingsFragment;
import cn.vove7.qtmnotificationplugin.fragments.WechatSettingsFragment;
import cn.vove7.qtmnotificationplugin.service.QTWNotificationListener;
import cn.vove7.qtmnotificationplugin.utils.AppUtils;
import cn.vove7.qtmnotificationplugin.utils.MyApplication;
import cn.vove7.qtmnotificationplugin.utils.PermissionUtils;
import cn.vove7.qtmnotificationplugin.utils.SettingsHelper;

import static cn.vove7.qtmnotificationplugin.utils.MyApplication.TextVolumeWith;

public class MainActivity extends BaseThemeActivity {
   QQSettingsFragment qqSettingsFragment = new QQSettingsFragment();
   WechatSettingsFragment wechatSettingsFragment = new WechatSettingsFragment();

   static boolean fromWechat = false;
   Toolbar toolbar;
   int clickNum = 0;
   //ViewPager viewPager;
   private long oldTime = 0;
   private int type = 0;
   private OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
           = item -> {
      long newTime = System.currentTimeMillis();
      boolean doubleClick = false;
      if (newTime - oldTime < 500) {
         doubleClick = true;
      } else {
         oldTime = newTime;
      }
      switch (item.getItemId()) {
         case R.id.navigation_qq://qq随机换肤
            if (doubleClick && type == 1) {
               fromWechat = false;
               EasyTheme.applyRandomTheme(this);
               return false;
            }
            type = 1;
            switchFragment(qqSettingsFragment, getString(R.string.title_qq_tim));
            return true;
         case R.id.navigation_wechat:
            if (doubleClick && type == 3) {
               //isDark = true;//微信夜间模式
               fromWechat = true;
               oldTime = 0;
               EasyTheme.toggleThemeMode(this);
               return false;
            }
            type = 3;
            switchFragment(wechatSettingsFragment, getString(R.string.title_wechat));
            return true;
      }
      return false;
   };

   FrameLayout frameLayout;
   BottomNavigationView navigation;

   private void initFragment() {
      frameLayout = findViewById(R.id.fragment);
      manager = getFragmentManager();
      if (!fromWechat)
         switchFragment(qqSettingsFragment, getString(R.string.title_qq_tim));
      else {
         navigation.setSelectedItemId(R.id.navigation_wechat);
         switchFragment(wechatSettingsFragment, getString(R.string.title_wechat));
      }

   }

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      SettingsHelper.initPreference(this);
      MyApplication.getInstance().setMainActivity(this);
      super.onCreate(savedInstanceState);

      setContentView(R.layout.activity_main);
      navigation = findViewById(R.id.navigation);
      navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

      initToolbar();
      initFragment();
   }

   MenuItem totalSwitch;
   MenuItem hideIcon;
   MenuItem setVolumeWith;

   @Override
   public boolean onMenuOpened(int featureId, Menu menu) {
      boolean s = SettingsHelper.getTotalSwitch();
      totalSwitch.setChecked(s);
      hideIcon.setChecked(SettingsHelper.getBoolean(R.string.key_hide_icon, false));
      setVolumeWith.setTitle(String.format(getString(R.string.text_set_volume_with),
              TextVolumeWith.get(SettingsHelper.getInt(R.string.key_volume_with,
                      AudioAttributes.USAGE_NOTIFICATION))));
      return super.onMenuOpened(featureId, menu);
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      Log.d("MainActivity :", "onCreateOptionsMenu  ----> ");
      getMenuInflater().inflate(R.menu.main_menu, menu);
      totalSwitch = menu.getItem(0);
      hideIcon = menu.getItem(1);
      setVolumeWith = menu.getItem(2);
      return true;
   }


   private void initToolbar() {
      toolbar = findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);
   }


   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      Log.d(this.getClass().getName(), "onMenuItemClick: " + item.getTitle());
      switch (item.getItemId()) {
         case R.id.menu_about: {
            showAboutDialog();
         }
         break;
         case R.id.menu_help: {
            startActivity(new Intent(this, HelperActivity.class));
         }
         break;
         case R.id.set_volume_with: {
            PopupMenu popupMenu = new PopupMenu(this, toolbar);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
               popupMenu.setGravity(Gravity.END);
            }
            popupMenu.inflate(R.menu.select_volume_from);
            popupMenu.setOnMenuItemClickListener(this::onOptionsItemSelected);
            popupMenu.show();
         }
         break;
         case R.id.total_switch: {
            boolean check = !item.isChecked();
            SettingsHelper.setValue(R.string.key_total_switch, check);
            item.setChecked(check);
         }
         break;
         case R.id.hide_icon: {
            boolean isHide = !item.isChecked();
            item.setChecked(isHide);
            int func = isHide ? PackageManager.COMPONENT_ENABLED_STATE_DISABLED :
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED;

            getPackageManager().setComponentEnabledSetting(
                    new ComponentName(this, SplashActivity.class),
                    func, PackageManager.DONT_KILL_APP);

            SettingsHelper.setValue(R.string.key_hide_icon, isHide);
         }
         break;
         case R.id.from_notification: {
            SettingsHelper.setValue(R.string.key_volume_with, AudioAttributes.USAGE_NOTIFICATION);
         }
         break;
         case R.id.from_media: {
            SettingsHelper.setValue(R.string.key_volume_with, AudioAttributes.USAGE_MEDIA);
         }
         break;
         case R.id.from_ringtone: {
            SettingsHelper.setValue(R.string.key_volume_with, AudioAttributes.USAGE_NOTIFICATION_RINGTONE);
         }
         break;
      }
      return false;
   }


   private void showAboutDialog() {
      AlertDialog.Builder aboutDialogBuilder = new AlertDialog.Builder(this);
      aboutDialogBuilder.setTitle(getString(R.string.text_about));

      final AppUtils appUtils = new AppUtils(this);
      aboutDialogBuilder.setNegativeButton("Donate",
              (dialogInterface, i) -> appUtils.donateWithAlipay());
      aboutDialogBuilder.setPositiveButton(R.string.text_check_for_update,
              (dia, i) -> appUtils.openMarket(getPackageName()));
      aboutDialogBuilder.setView(R.layout.dialog_help_layout);

      aboutDialogBuilder.setNeutralButton("Github",
              (dialogInterface, i) -> appUtils.openGithub(githubUrl));
      aboutDialogBuilder.show();

   }

   private static final String githubUrl = "https://github.com/Vove7/QTMNotificationPlugin";

   private void checkNotificationAccessPermission() {
      closeRequestDialog();//关闭询问窗口
      if (!PermissionUtils.notificationListenerEnabled(this)) {
         showRequestDialog();
      } else if (!QTWNotificationListener.isConnect) {//等待开启
         waitAccess();//等待开启
      } else {
         //成功开启
         showTutorials();
      }
   }

   private void showTutorials() {
      if (!SettingsHelper.getBoolean(R.string.key_main_tutorial_is_show, false)) {
         AppUtils.showTutorials(this, findViewById(R.id.navigation_qq),
                 "狂按有惊喜", "", R.drawable.ic_qq
         );
         SettingsHelper.setValue(R.string.key_main_tutorial_is_show, true);
      }

   }

   private void closeRequestDialog() {
      if (requestPermissionDialog != null) {
         requestPermissionDialog.dismiss();
      }
      if (requestRebootDialog != null) {
         requestRebootDialog.dismiss();
      }
   }

   private void waitAccess() {
      TextView textView = new TextView(this);
      textView.setText(R.string.check_access_status);
      Dialog processDialog = new Dialog(this);
      processDialog.setContentView(textView);
      textView.setWidth(800);
      textView.setPadding(50, 30, 50, 30);
      processDialog.setCancelable(false);
      processDialog.show();
      new Handler().postDelayed(() -> {
         processDialog.dismiss();
         if (!QTWNotificationListener.isConnect) {
            showRebootTips();
         }
      }, 1000);
   }


   private void showRebootTips() {
      AlertDialog.Builder rebootDialogBuilder = new AlertDialog.Builder(this);
      rebootDialogBuilder.setCancelable(false);
      rebootDialogBuilder.setTitle(R.string.text_hint);
      rebootDialogBuilder.setMessage(R.string.access_open_failed_tips);
      rebootDialogBuilder.setNeutralButton(R.string.text_recheck, (dialog, which) -> {
         dialog.dismiss();
         checkNotificationAccessPermission();
      });
      rebootDialogBuilder.setPositiveButton(R.string.text_reopen, (dialog, which) ->
              PermissionUtils.gotoNotificationAccessSetting(this));
      rebootDialogBuilder.setNegativeButton(R.string.text_button_exit, (dialog, which) ->
              System.exit(0));
      requestRebootDialog = rebootDialogBuilder.show();
   }

   private AlertDialog requestPermissionDialog;
   private AlertDialog requestRebootDialog;

   private void showRequestDialog() {
      AlertDialog.Builder requestDialogBuilder = new AlertDialog.Builder(this);
      requestDialogBuilder.setTitle(R.string.request_notification_access);
      requestDialogBuilder.setMessage(R.string.request_permission_message);
      requestDialogBuilder.setCancelable(false);
      requestDialogBuilder.setNegativeButton(R.string.text_button_exit, (dialog, which) ->
              System.exit(0));
      requestDialogBuilder.setPositiveButton(R.string.text_button_open, (dialog, which) ->
              PermissionUtils.gotoNotificationAccessSetting(this));
      requestPermissionDialog = requestDialogBuilder.show();
   }

   @Override
   protected void onResume() {
      super.onResume();
      checkNotificationAccessPermission();
      boolean s = SettingsHelper.getTotalSwitch();
      if (!s) {
         Toast.makeText(this, "当前已关闭提醒", Toast.LENGTH_SHORT).show();
      }
   }

   @Override
   public boolean onKeyDown(int keyCode, KeyEvent event) {
      if (keyCode == KeyEvent.KEYCODE_BACK) {
         Intent home = new Intent(Intent.ACTION_MAIN);
         home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         home.addCategory(Intent.CATEGORY_HOME);
         startActivity(home);
         return true;
      }
      return super.onKeyDown(keyCode, event);
   }

   private FragmentManager manager;

   private void switchFragment(Fragment targetFragment, String title) {
      toolbar.setTitle(title);
      manager.beginTransaction().replace(R.id.fragment, targetFragment).commit();
   }


}