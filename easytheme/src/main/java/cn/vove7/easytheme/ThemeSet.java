package cn.vove7.easytheme;

import java.util.HashMap;
import java.util.Map;

/**
 * 预设主题
 * The default theme
 */
public class ThemeSet {
   private static final Map<String, Integer> themeMap = new HashMap<>();

   public static int buildStyle(ThemeMode themeMode, Theme theme) {
      Integer id = themeMap.get(themeMode + "." + theme);
      return id == null ?
              themeMap.get(themeMode + "." + Theme.Primary) : id;
   }

   static {
      //Light
      themeMap.put("Light.Primary", R.style.Light_Primary);
      themeMap.put("Light.Amber", R.style.Light_Amber);
      themeMap.put("Light.Blue", R.style.Light_Blue);
      themeMap.put("Light.Brown", R.style.Light_Brown);
      themeMap.put("Light.Cyan", R.style.Light_Cyan);
      themeMap.put("Light.DeepOrange", R.style.Light_DeepOrange);
      themeMap.put("Light.Green", R.style.Light_Green);
      themeMap.put("Light.Grey", R.style.Light_Grey);
      themeMap.put("Light.Indigo", R.style.Light_Indigo);
      themeMap.put("Light.Orange", R.style.Light_Orange);
      themeMap.put("Light.Pink", R.style.Light_Pink);
      themeMap.put("Light.Purple", R.style.Light_Purple);
      themeMap.put("Light.Red", R.style.Light_Red);
      themeMap.put("Light.Teal", R.style.Light_Teal);
      //Dark
      themeMap.put("Dark.Primary", R.style.Dark_Primary);
      themeMap.put("Dark.Amber", R.style.Dark_Amber);
      themeMap.put("Dark.Cyan", R.style.Dark_Cyan);
      themeMap.put("Dark.DeepOrange", R.style.Dark_DeepOrange);
      themeMap.put("Dark.Green", R.style.Dark_Green);
      themeMap.put("Dark.Indigo", R.style.Dark_Indigo);
      themeMap.put("Dark.LightBlue", R.style.Dark_LightBlue);
      themeMap.put("Dark.Orange", R.style.Dark_Orange);
      themeMap.put("Dark.Pink", R.style.Dark_Pink);
      themeMap.put("Dark.Purple", R.style.Dark_Purple);
      themeMap.put("Dark.Red", R.style.Dark_Red);
      themeMap.put("Dark.Teal", R.style.Dark_Teal);
      //ABlack
      themeMap.put("ABlack.Primary", R.style.ABlack_Primary);
      themeMap.put("ABlack.Amber", R.style.ABlack_Amber);
      themeMap.put("ABlack.Cyan", R.style.ABlack_Cyan);
      themeMap.put("ABlack.DeepOrange", R.style.ABlack_DeepOrange);
      themeMap.put("ABlack.Green", R.style.ABlack_Green);
      themeMap.put("ABlack.LightBlue", R.style.ABlack_LightBlue);
      themeMap.put("ABlack.Lime", R.style.ABlack_Lime);
      themeMap.put("ABlack.Orange", R.style.ABlack_Orange);
      themeMap.put("ABlack.Purple", R.style.ABlack_Purple);
      themeMap.put("ABlack.Red", R.style.ABlack_Red);
      themeMap.put("ABlack.Teal", R.style.ABlack_Teal);
   }

   /**
    * Light,Dark,A屏黑模式
    */
   public enum ThemeMode {
      Light("Light"), Dark("Dark"), ABlack("ABlack");
      private String mode;

      @Override
      public String toString() {
         return mode;
      }

      public ThemeMode getNextMode() {
         switch (this) {
            case Light:
               return Dark;
            case Dark:
               return ABlack;
            case ABlack:
               return Light;
         }
         return Light;
      }

      ThemeMode(String mode) {
         this.mode = mode;
      }
   }

   /**
    * The default theme
    * just contain colorPrimary,colorPrimaryDark,colorAccent,navigationBarColor when ThemeMode is Light or Dark.
    * ABlack has not contain the colorPrimaryDark(it should be black)
    */
   public enum Theme {
      Primary("Primary"),
      Amber("Amber"),
      Blue("Blue"),
      Brown("Brown"),
      Cyan("Cyan"),
      DeepOrange("DeepOrange"),
      Green("Green"),
      Grey("Grey"),
      Indigo("Indigo"),
      LightBlue("LightBlue"),
      Lime("Lime"),
      Orange("Orange"),
      Pink("Pink"),
      Purple("Purple"),
      Red("Red"),
      Teal("Teal");

      private String mTheme;

      @Override
      public String toString() {
         return mTheme;
      }

      Theme(String mTheme) {
         this.mTheme = mTheme;
      }
   }
}
