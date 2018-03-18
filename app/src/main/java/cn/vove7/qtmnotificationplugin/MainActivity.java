package cn.vove7.qtmnotificationplugin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.vove7.easytheme.BaseThemeActivity;
import cn.vove7.easytheme.EasyTheme;
import cn.vove7.easytheme.ThemeSet;
import cn.vove7.qtmnotificationplugin.fragments.QQSettingsFragment;
import cn.vove7.qtmnotificationplugin.fragments.WechatSettingsFragment;
import cn.vove7.qtmnotificationplugin.utils.AppUtils;
import cn.vove7.qtmnotificationplugin.utils.MyApplication;
import cn.vove7.qtmnotificationplugin.utils.PermissionUtils;
import cn.vove7.qtmnotificationplugin.utils.SettingsHelper;

public class MainActivity extends BaseThemeActivity {
   QQSettingsFragment qqSettingsFragment = new QQSettingsFragment();
   WechatSettingsFragment wechatSettingsFragment = new WechatSettingsFragment();

   Toolbar toolbar;
   int clickNum = 0;
   //ViewPager viewPager;
   static int index = 0;
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
               index++;
               index %= ThemeSet.Theme.values().length;
               EasyTheme.applyRandomTheme(this);
               if (clickNum < 5) {
                  clickNum++;
               } else {
                  Toast.makeText(this, "啊啊啊啊啊啊", Toast.LENGTH_SHORT).show();
                  clickNum = 0;
               }
               return false;
            }
            type = 1;
            //viewPager.setCurrentItem(0);
            switchFragment(qqSettingsFragment, getString(R.string.title_qq_tim));
            return true;
         case R.id.navigation_wechat:
            if (doubleClick && type == 3) {
               //isDark = true;//微信夜间模式
               EasyTheme.toggleThemeMode(this);
               return false;
            } else {
               type = 3;
               //viewPager.setCurrentItem(1);
               switchFragment(wechatSettingsFragment, getString(R.string.title_wechat));
               return true;
            }
      }
      return false;
   };

   FrameLayout frameLayout;

   private void initFragment() {
      frameLayout = findViewById(R.id.fragment);
      manager = getFragmentManager();
      switchFragment(qqSettingsFragment, getString(R.string.title_qq_tim));
      //viewPager=findViewById(R.id.fragment);
      //List<Fragment> list=new ArrayList<>();
      //list.add(qqSettingsFragment);
      //list.add(wechatSettingsFragment);
      //FragmentAdapter adapter=new FragmentAdapter(getSupportFragmentManager(),list);
      //viewPager.setAdapter(adapter);
      //viewPager.setCurrentItem(0);
   }

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      SettingsHelper.initPreference(this);
      MyApplication.getInstance().setMainActivity(this);
      super.onCreate(savedInstanceState);

      setContentView(R.layout.activity_main);
      hideActionBar();
      BottomNavigationView navigation = findViewById(R.id.navigation);
      navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

      initToolbar();
      initFragment();
   }

   private void initToolbar() {
      toolbar = findViewById(R.id.toolbar);
      toolbar.inflateMenu(R.menu.main_menu);
      MenuItem hideIcon = toolbar.getMenu().getItem(0);
      hideIcon.setChecked(SettingsHelper.getBoolean(R.string.key_hide_icon, false));
      hideIcon.setOnMenuItemClickListener(menuItem -> {
         boolean isHide = !menuItem.isChecked();
         menuItem.setChecked(isHide);
         Log.d(this.getClass().getName(), "onMenuItemClick: " + menuItem.getTitle() + isHide);
         int func = isHide ? PackageManager.COMPONENT_ENABLED_STATE_DISABLED :
                 PackageManager.COMPONENT_ENABLED_STATE_ENABLED;

         getPackageManager().setComponentEnabledSetting(
                 new ComponentName(this, SplashActivity.class),
                 func, PackageManager.DONT_KILL_APP);

         SettingsHelper.setValue(R.string.key_hide_icon, isHide);
         return false;
      });
      toolbar.setOnMenuItemClickListener(item -> {
                 switch (item.getItemId()) {
                    case R.id.menu_about: {
                       showAboutDialog();
                    }
                    break;
                    case R.id.menu_help: {
                       startActivity(new Intent(this, HelperActivity.class));
                    }
                    break;
                 }
                 return false;
              }
      );
   }

   private void showAboutDialog() {
      AlertDialog.Builder aboutDialogBuilder = new AlertDialog.Builder(this);
      aboutDialogBuilder.setTitle(getString(R.string.text_about));

      final AppUtils appUtils = new AppUtils(this);
      aboutDialogBuilder.setNegativeButton("Donate",
              (dialogInterface, i) -> appUtils.donateWithAlipay());
      aboutDialogBuilder.setPositiveButton("检查更新",
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
      } else if (!QTMNotificationListener.isConnect) {//等待开启
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
         if (!QTMNotificationListener.isConnect) {
            showRebootTips();
         }
      }, 1000);
   }


   private void showRebootTips() {
      AlertDialog.Builder rebootDialogBuilder = new AlertDialog.Builder(this);
      rebootDialogBuilder.setCancelable(false);
      rebootDialogBuilder.setTitle("提示");
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
