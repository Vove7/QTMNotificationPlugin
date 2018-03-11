package cn.vove7.qtmnotificationplugin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.vove7.easytheme.BaseThemeActivity;
import cn.vove7.easytheme.EasyTheme;
import cn.vove7.easytheme.ThemeSet;
import cn.vove7.qtmnotificationplugin.fragments.QQSettingsFragment;
import cn.vove7.qtmnotificationplugin.fragments.WechatSettingsFragment;
import cn.vove7.qtmnotificationplugin.util.MyApplication;
import cn.vove7.qtmnotificationplugin.util.PermissionUtils;
import cn.vove7.qtmnotificationplugin.util.SettingsHelper;

public class MainActivity extends BaseThemeActivity {
   QQSettingsFragment qqSettingsFragment = new QQSettingsFragment();
   //TimSettingsFragment timSettingsFragment = new TimSettingsFragment();
   WechatSettingsFragment wechatSettingsFragment = new WechatSettingsFragment();

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
               return false;
            }
            type = 1;
            //viewPager.setCurrentItem(0);
            switchFragment(qqSettingsFragment);
            return true;
         //case R.id.navigation_tim:
         //   switchFragment(timSettingsFragment);
         //   if (doubleClick && type == 2) {//关于
         //      Toast.makeText(this, "关于", Toast.LENGTH_SHORT).show();
         //   }
         //   type = 2;
         //   return true;
         case R.id.navigation_wechat:
            if (doubleClick && type == 3) {
               //isDark = true;//微信夜间模式
               EasyTheme.toggleThemeMode(this);
               return false;
            } else {
               type = 3;
               //viewPager.setCurrentItem(1);
               switchFragment(wechatSettingsFragment);
               return true;
            }
      }
      return false;
   };

   FrameLayout frameLayout;

   private void initFragment() {
      frameLayout = findViewById(R.id.fragment);
      manager=getFragmentManager();
      switchFragment(qqSettingsFragment);
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

      initFragment();


      Toolbar toolbar = findViewById(R.id.toolbar);
      toolbar.inflateMenu(R.menu.main_menu);
      toolbar.setOnMenuItemClickListener(item -> {
                 Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                 switch (item.getItemId()) {
                    case R.id.menu_about: {

                    }
                    break;
                    case R.id.menu_donate: {

                    }
                    break;
                    case R.id.menu_help: {

                    }
                    break;
                    case R.id.menu_change_skin: {
                       //applyTheme(EasyTheme.isDark);
                    }
                    break;
                 }
                 return false;
              }
      );
      //setSupportActionBar(toolbar);
   }

   private void checkNotificationAccessPermission() {
      closeRequestDialog();//关闭询问窗口
      if (!PermissionUtils.notificationListenerEnabled(this)) {
         showRequestDialog();
      } else if (!QTMNotificationListener.isConnect) {//等待开启
         waitAccess();//等待开启
      }
      //成功开启
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

   private Fragment currentFragment;
   private FragmentManager manager;
   private void switchFragment(Fragment targetFragment) {
      currentFragment = targetFragment;
      manager.beginTransaction().replace(R.id.fragment, targetFragment).commit();
   }

}
