package cn.vove7.qtmnotificationplugin.utils;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.accessibility.AccessibilityManager;

import java.util.List;

public class PermissionUtils {

   /**
    * 显示应用详情页
    * @param context Context
    * @param packageName 包名
    */
   public static void showPackageDetail(Context context, String packageName) {
      Intent intent = new Intent();
      intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
      Uri uri = Uri.fromParts("package", packageName, null);
      intent.setData(uri);
      context.startActivity(intent);
   }

   /**
    * @param context Context
    * @return 是否开启本app无障碍服务
    */
   public static boolean accessibilityServiceEnabled(Context context) {
      String pkg = context.getPackageName();
      AccessibilityManager am = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
      List<AccessibilityServiceInfo> enabledAccessibilityServiceList;
      if (am != null) {
         enabledAccessibilityServiceList = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
         for (AccessibilityServiceInfo info : enabledAccessibilityServiceList) {
            //Log.d("#########", "all -->" + info.getId());
            if (info.getId().contains(pkg)) {
               return true;
            }
         }
      }
      return false;
   }

   private static final String TAG = "PermissionUtils";

   /**
    * @param context Context
    * @return 是否开启本app的通知访问权限
    */
   public static boolean notificationListenerEnabled(Context context) {
      boolean enable = false;
      String packageName = context.getPackageName();
      String flat = Settings.Secure.getString(context.getContentResolver(), "enabled_notification_listeners");
      //Log.d(TAG, "flat-" + flat);
      if (flat != null) {
         enable = flat.contains(packageName);
      }
      return enable;
   }

   /**
    * 开启通知使用权限Activity
    * @param context (Context)
    * @return 开启结果
    */
   public static boolean gotoNotificationAccessSetting(Context context) {
      try {
         Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
         intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         context.startActivity(intent);
         return true;
      } catch(ActivityNotFoundException e) {
         try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName cn = new ComponentName("com.android.settings","com.android.settings.Settings$NotificationAccessSettingsActivity");
            intent.setComponent(cn);
            intent.putExtra(":settings:show_fragment", "NotificationAccessSettings");
            context.startActivity(intent);
            return true;
         } catch(Exception ex) {
            ex.printStackTrace();
         }
         return false;
      }
   }
}
