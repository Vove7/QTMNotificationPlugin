package cn.vove7.qtmnotificationplugin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;

import cn.vove7.easytheme.BaseThemeActivity;
import cn.vove7.easytheme.EasyTheme;
import cn.vove7.easytheme.ThemeSet;
import cn.vove7.qtmnotificationplugin.adapter.MultiTitleAdapter;
import cn.vove7.qtmnotificationplugin.util.SQLOperator;

import static cn.vove7.qtmnotificationplugin.QTMNotificationListener.TYPE_QQ_TIM;

public class ManageFaActivity extends BaseThemeActivity {

   private RecyclerView listView;
   private SearchView searchView;
   private SearchView.SearchAutoComplete mSearchAutoComplete;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
      setContentView(R.layout.activity_manage_fa);
      getIntent().getBundleExtra("data");
      initView();
   }

   private <T extends View> T $(int resId) {
      return findViewById(resId);
   }

   private void initView() {
      Toolbar toolbar = $(R.id.toolbar);
      setSupportActionBar(toolbar);
      if (EasyTheme.currentThemeMode == ThemeSet.ThemeMode.Light) {
         toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
      } else {
         toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
      }
      //Toolbar返回按钮的点击事件
      toolbar.setNavigationOnClickListener(v -> {
         if (mSearchAutoComplete.isShown()) {
            try {
               //如果搜索框中有文字，则会先清空文字，但网易云音乐是在点击返回键时直接关闭搜索框
               mSearchAutoComplete.setText("");
               Method method = searchView.getClass().getDeclaredMethod("onCloseClicked");
               method.setAccessible(true);
               method.invoke(searchView);
            } catch (Exception e) {
               e.printStackTrace();
            }
         } else {
            finish();
         }
      });
      listView = $(R.id.search_list);
      ArrayList<String> names = new SQLOperator(this).getNickname(TYPE_QQ_TIM);
      ArrayList[] lists = new ArrayList[2];
      lists[0] = names;
      lists[1] = names;

      LinearLayoutManager linear_lm=new LinearLayoutManager(this);
      linear_lm.setOrientation(LinearLayoutManager.VERTICAL);//横滚
      listView.setLayoutManager(linear_lm);
      listView.setAdapter(new MultiTitleAdapter(lists, new String[]{"特别关心", "未选"}, new boolean[]{true, false}));
   }

   private static final String TAG = "ManageFaActivity";

   @SuppressLint("RestrictedApi")
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.search_menu, menu);

      //找到searchView
      MenuItem searchItem = menu.findItem(R.id.action_search);
      searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
      mSearchAutoComplete = searchView.findViewById(R.id.search_src_text);
      //设置触发查询的最少字符数（默认2个字符才会触发查询）
      mSearchAutoComplete.setThreshold(1);
      searchView.setQueryHint("搜索昵称记录/自定义提交");
      searchView.onActionViewExpanded();
      searchView.setMaxWidth(20000);
      searchView.setSubmitButtonEnabled(true);

      //Cursor cursor=new SQLOperator(this).getNickname(TYPE_QQ_TIM);
      //searchView.setSuggestionsAdapter(new SimpleCursorAdapter(ManageFaActivity.this,
      //        R.layout.search_suggestion_item, cursor, new String[]{"nickname"}, new int[]{R.id.text}));

      searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
         @Override
         public boolean onQueryTextSubmit(String query) {
            Log.d(TAG, "onQueryTextSubmit: 提交" + query);
            return false;
         }

         @Override
         public boolean onQueryTextChange(String newText) {
            //Cursor cursor1=new SQLOperator(ManageFaActivity.this).searchNickname(newText,TYPE_QQ_TIM);
            //searchView.getSuggestionsAdapter().changeCursor(cursor1);
            Log.d(TAG, "onQueryTextChange: ->" + newText + "  suggestion count：");
            return false;
         }
      });

      return super.onCreateOptionsMenu(menu);
   }

   public void done(View view) {
      Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
   }
}
