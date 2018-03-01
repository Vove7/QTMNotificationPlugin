package cn.vove7.easytheme;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;


public class BaseThemeActivity extends AppCompatActivity {
   long lastChange = 0;

   public BaseThemeActivity() {
   }

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      lastChange = EasyTheme.lastChange;
      initTheme();
   }

   //初始化主题
   private void initTheme() {
      setTheme(EasyTheme.currentThemeId);
   }

   public void hideActionBar() {
      ActionBar actionBar = getSupportActionBar();
      if(actionBar!=null){
         actionBar.hide();
      }
   }
   @Override
   protected void onResume() {
      super.onResume();
      if (lastChange != EasyTheme.lastChange) {
         lastChange = EasyTheme.lastChange;
         EasyTheme.applyTheme(this);
      }
   }

   @Override
   public void finish() {
      super.finish();
      if (lastChange != EasyTheme.lastChange) {
         //切换动画
         overridePendingTransition(EasyTheme.enterAnim, EasyTheme.exitAnim);
      }
   }

}
