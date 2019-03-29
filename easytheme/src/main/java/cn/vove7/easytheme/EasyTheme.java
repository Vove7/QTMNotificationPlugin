package cn.vove7.easytheme;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.AnimRes;
import android.support.annotation.StyleRes;

import cn.vove7.easytheme.ThemeSet.Theme;
import cn.vove7.easytheme.ThemeSet.ThemeMode;

public class EasyTheme {
   public static Theme currentTheme;
   public static ThemeMode currentThemeMode;

   public static int currentThemeId;
   public static int defaultThemeId;

   public static long lastChange = 0;

   //切换主题过渡动画
   //Switch theme transition anim.
   public static int enterAnim = R.anim.enter;
   public static int exitAnim = R.anim.exit;

   /**
    * 初始化主题资源
    *
    * @param context   Context
    * @param themeMode see {@link ThemeSet}
    * @param theme     see {@link Theme}
    */
   public static void init(Context context, ThemeMode themeMode, Theme theme) {
      //int defaultThemeId = ThemeSet.buildStyle(themeMode, theme);
      defaultThemeId = ThemeSet.buildStyle(themeMode, theme);
      PreferenceUtils.initPreference(context);
      currentTheme = PreferenceUtils.getTheme();
      currentThemeMode = PreferenceUtils.getThemeMode();
      currentThemeId = PreferenceUtils.getThemeId();
      context.setTheme(currentThemeId);
   }

   /**
    * 使用themeId初始化
    * @param context Context
    * @param themeId @StyleRes
    */
   public static void init(Context context, @StyleRes int themeId) {
      defaultThemeId = themeId;
      PreferenceUtils.initPreference(context);
      currentThemeId = PreferenceUtils.getThemeId();
   }

   /**
    * 当修改主题,返回上个Activity, 在onResume被调用
    *
    * @param context Context
    */
   public static void applyTheme(Context context) {
      applyTheme(context, currentThemeMode, currentTheme);
   }

   /**
    * 恢复默认主题,应在Application中调用EasyTHeme.init(..) {@link EasyTheme#init(Context, ThemeMode, Theme)}
    * You should call {@link EasyTheme#init(Context, ThemeMode, Theme)} in you application class before invoke it.
    * default R.style.Light.Primary
    */
   public static void applyDefaultTheme(Context context) {
      applyTheme(context, defaultThemeId);
   }

   /**
    * 指定ThemeMode和Theme应用主题
    * Specify the ThemeMode and Theme to apply.
    *
    * @param context   activity
    * @param themeMode see {@link ThemeSet}
    * @param theme     see {@link Theme}
    */
   public static void applyTheme(Context context, ThemeMode themeMode, Theme theme) {
      PreferenceUtils.initPreference(context);
      currentTheme = theme;
      currentThemeMode = themeMode;
      currentThemeId = ThemeSet.buildStyle(currentThemeMode, currentTheme);

      PreferenceUtils.setValue(context.getString(R.string.key_easy_theme), currentTheme.toString());
      PreferenceUtils.setValue(context.getString(R.string.key_easy_theme_mode), currentThemeMode.toString());
      applyTheme(context, currentThemeId);
   }

   /**
    * 设置过渡动画(Set transition animation)
    *
    * @param enterAnim @AnimRes
    * @param exitAnim  @AnimRes
    */
   public static void setTransitionAnim(@AnimRes int enterAnim, @AnimRes int exitAnim) {
      EasyTheme.enterAnim = enterAnim;
      EasyTheme.exitAnim = exitAnim;
   }

   /**
    * 你可以自定义主题,进行应用
    * you can diy yourself theme and apply it.
    *
    * @param context activity
    * @param ThemeId resId
    */
   public static void applyTheme(Context context, @StyleRes int ThemeId) {
      //更改主题
      currentThemeId = ThemeId;
      lastChange = System.currentTimeMillis();

      PreferenceUtils.setValue(context.getString(R.string.key_easy_theme_id), currentThemeId);

      Intent intent = Intent.makeRestartActivityTask(((Activity) context).getComponentName());
      context.startActivity(intent);
      ((Activity) context).finish();
   }

   /**
    * 随机,不改变ThemeMode
    *
    * @param context Context
    */
   public static void applyRandomTheme(Context context) {
      Theme[] themes = Theme.values();
      int i;
      do {
         i = (int) (Math.random() * themes.length);
      } while (themes[i] == currentTheme);
      applyTheme(context, currentThemeMode, themes[i]);
   }

   /**
    * 切换模式 Light Dark ABlack
    *
    * @param context Context
    */
   public static void toggleThemeMode(Context context) {
      applyTheme(context, currentThemeMode.getNextMode(), currentTheme);
   }
}
