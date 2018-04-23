package cn.vove7.qtmnotificationplugin.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.didikee.donate.AlipayDonate;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;

import cn.vove7.qtmnotificationplugin.activity.MainActivity;
import cn.vove7.qtmnotificationplugin.R;

public class AppUtils {
   private Context context;

   public AppUtils(Context context) {
      this.context = context;
   }

   private static final String payCode = "FKX07237LYKEFIVIY8MSE9";

   public void donateWithAlipay() {
      boolean hasInstalledAlipayClient = AlipayDonate.hasInstalledAlipayClient(context);
      if (hasInstalledAlipayClient) {
         AlipayDonate.startAlipayClient((MainActivity) context, payCode);
      }
   }

   public void openGithub(String githubUrl) {
      Intent intent = new Intent();
      intent.setAction(Intent.ACTION_VIEW);
      intent.setData(Uri.parse(githubUrl));
      context.startActivity(intent);
   }

   public void openMarket(String pkgName) {
      String str = "market://details?id=" + pkgName;
      Intent localIntent = new Intent("android.intent.action.VIEW");
      localIntent.setData(Uri.parse(str));
      context.startActivity(localIntent);
   }

   public static void showTutorials(Context context, View view, String title, String description, @DrawableRes @Nullable Integer iconId) {
      TapTarget tapTarget = TapTarget.forView(view, title, description)
              // All options below are optional
              .outerCircleColor(R.color.accent_red)      // Specify a color for the outer circle
              .outerCircleAlpha(0.99f)            // Specify the alpha amount for the outer circle
              .targetCircleColor(R.color.accent_white)   // Specify a color for the target circle
              .titleTextSize(20)                  // Specify the size (in sp) of the title text
              .titleTextColor(R.color.accent_white)      // Specify the color of the title text
              .descriptionTextSize(10)            // Specify the size (in sp) of the description text
              .descriptionTextColor(R.color.md_red_700)  // Specify the color of the description text
              .textColor(R.color.accent_white)            // Specify a color for both the title and description text
              .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
              .dimColor(R.color.accent_black)            // If set, will dim behind the view with 30% opacity of the given color
              .drawShadow(true)                   // Whether to draw a drop shadow or not
              .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
              .tintTarget(true)                   // Whether to tint the target view's color
              .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
              .targetRadius(60);                    // Specify the target radius (in dp)
      if (iconId != null) {
         tapTarget.icon(context.getDrawable(iconId));// Specify a custom drawable to draw as the target
      }
      TapTargetView.showFor((Activity) context, tapTarget,
              new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                 @Override
                 public void onTargetClick(TapTargetView view) {
                    super.onTargetClick(view);      // This call is optional
                 }
              });
   }

}