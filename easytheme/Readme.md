# EasyTheme
> a simple theme engine

![example](http://www.vove7.cn:800/typecho/usr/uploads/2018/03/994988033.gif)
## how to use?
- add this library
   1.gradle
   `compile 'cn.vove7:EasyTheme:1.0'`
   2.maven
   ```
   <dependency>
      <groupId>cn.vove7</groupId>
      <artifactId>EasyTheme</artifactId>
      <version>1.0</version>
      <type>pom</type>
   </dependency>
```
- in your applicatoin  ```onCreate()``` insert ```EasyTheme.init(this, ThemeSet.ThemeMode.Light, ThemeSet.Theme.Primary);```
```
   @Override
   public void onCreate() {
      super.onCreate();
      //set default theme
      EasyTheme.init(this, ThemeSet.ThemeMode.Light, ThemeSet.Theme.Primary);
   }
```
* set your `activity extends BaseThemeActivity`
```
public class MainActivity extends BaseThemeActivity{
    ...
}
```
## the ways to change theme in your activity
- Specify the ThemeMode and Theme to apply.
call `EasyTheme.applyTheme(Context context, ThemeMode themeMode, Theme theme)` in activity
- you can diy yourself theme and apply it.
call `EasyTheme.applyTheme(Context context, @StyleRes int ThemeId)` in activity
## other function explain
- EasyTheme.init(Context context, @StyleRes int themeId)
    > init default theme use your diy style in application
- EasyTheme.applyDefaultTheme(Context context)
    > recover the defaultTheme
- EasyTheme.setTransitionAnim(@AnimRes int enterAnim, @AnimRes int exitAnim)
    > set activity anim of enter and exit when switch theme.
- EasyTheme.applyRandomTheme(Context context)
    > set random theme.
- EasyTheme.toggleThemeMode(Context context)
    > switch themeMode Light->Dark->ABlack
- BaseThemeActivity.hideActionBar()
    > hide this activity's actionbar
