package cn.vove7.qtmnotificationplugin.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.vove7.easytheme.BaseThemeActivity;
import cn.vove7.easytheme.EasyTheme;
import cn.vove7.qtmnotificationplugin.R;

public class HelperActivity extends BaseThemeActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_helper);
      Toolbar toolbar = findViewById(R.id.toolbar);
      hideActionBar();
      toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

      toolbar.setNavigationOnClickListener(v -> finish());

      WebView webView=findViewById(R.id.webView);
      WebSettings settings = webView.getSettings();
      // 设置javaScript可用
      settings.setJavaScriptEnabled(true);

      webView.loadUrl("file:///android_asset/help.html");
      webView.setWebViewClient(new MyClient());
   }
   class MyClient extends WebViewClient{
      @Override
      public void onPageFinished(WebView view, String url) {
         super.onPageFinished(view, url);
         switch (EasyTheme.currentThemeMode) {
            case Dark:
               view.loadUrl("javascript:load_theme_dark()");
               break;
            case Light:
               view.loadUrl("javascript:load_theme_day()");
               break;
            case ABlack:
               view.loadUrl("javascript:load_theme_ablack()");
               break;
         }
      }
   }
}
