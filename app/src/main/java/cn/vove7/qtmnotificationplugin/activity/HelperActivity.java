package cn.vove7.qtmnotificationplugin.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import cn.vove7.easytheme.BaseThemeActivity;
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
      webView.loadUrl("file:///android_asset/help.html");
   }

}
