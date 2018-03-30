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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import cn.vove7.easytheme.BaseThemeActivity;
import cn.vove7.qtmnotificationplugin.adapter.MultiTitleAdapter;
import cn.vove7.qtmnotificationplugin.utils.AppUtils;
import cn.vove7.qtmnotificationplugin.utils.MyApplication;
import cn.vove7.qtmnotificationplugin.utils.SQLOperator;
import cn.vove7.qtmnotificationplugin.utils.SettingsHelper;

import static cn.vove7.qtmnotificationplugin.QTWNotificationListener.TYPE_QQ_TIM;
import static cn.vove7.qtmnotificationplugin.QTWNotificationListener.TYPE_WECHAT;

/**
 * 管理特别关心，黑名单
 */
public class ManageFaActivity extends BaseThemeActivity {

   private RecyclerView listView;
   private SearchView searchView;
   private SearchView.SearchAutoComplete mSearchAutoComplete;
   private int pkgType;
   private String type;
   private int listType;

   public static final int PKG_TYPE_QQ_TIM = 10;
   public static final int PKG_TYPE_WECHAT = 20;
   public static final int LIST_TYPE_FA = 1;
   public static final int LIST_TYPE_BLACK = 2;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
      setContentView(R.layout.activity_manage_fa);
      pkgType = getIntent().getIntExtra("pkgType", PKG_TYPE_QQ_TIM);
      type = pkgType == PKG_TYPE_QQ_TIM ? TYPE_QQ_TIM : TYPE_WECHAT;
      listType = getIntent().getIntExtra("listType", LIST_TYPE_FA);
      Log.d(this.getClass().getName(), "onCreate: " + pkgType);
      initView();
   }

   private <T extends View> T $(int resId) {
      return findViewById(resId);
   }

   private void initView() {
      Toolbar toolbar = $(R.id.toolbar);
      if (listType == LIST_TYPE_FA)
         toolbar.setTitle(R.string.text_manage_fa);
      else
         toolbar.setTitle(R.string.text_manage_black_list);
      setSupportActionBar(toolbar);
      setToolbarNavigationIcon(toolbar, R.drawable.ic_arrow_back_white_24dp, R.drawable.ic_arrow_back_black_24dp);

      //Toolbar返回按钮的点击事件
      toolbar.setNavigationOnClickListener(v -> {
         if (mSearchAutoComplete.isShown()) {
            try {
               //如果搜索框中有文字，则会先清空文字
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
      refreshList(getAll());
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
      searchView.setQueryHint(getString(R.string.query_hint));
      searchView.onActionViewCollapsed();
      searchView.setMaxWidth(20000);
      searchView.setSubmitButtonEnabled(true);

      searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
         @Override
         public boolean onQueryTextSubmit(String query) {
            Log.d(TAG, "onQueryTextSubmit: 提交" + query);
            if (query.trim().equals(""))
               return true;

            Object[] objects = getSetAndKey(listType + pkgType);
            Set<String> set = (Set<String>) objects[0];
            String key = (String) objects[1];
            set.add(query);
            SettingsHelper.setValue(key, set);
            searchView.setQuery("", true);
            return false;
         }

         @Override
         public boolean onQueryTextChange(String newText) {
            refreshList(queryAlike(newText));
            return false;
         }
      });
      showTutorials(searchView);
      return super.onCreateOptionsMenu(menu);
   }

   private void showTutorials(View view) {
      if (!SettingsHelper.getBoolean(R.string.key_manage_tutorial_is_show, false)) {
         AppUtils.showTutorials(this, view, "看这里,看这里", "可用于搜索/添加", null);
         SettingsHelper.setValue(R.string.key_manage_tutorial_is_show, true);
      }
   }

   private Object[] getSetAndKey() {
      return getSetAndKey(listType + pkgType);
   }

   public static Object[] getSetAndKey(int s) {
      Set<String> set;
      String key;
      switch (s) {
         case 11: {
            set = SettingsHelper.getFaSetQQ();
            key = MyApplication.getInstance().getMainActivity().getString(R.string.key_fas_qq);
         }
         break;
         case 21: {
            set = SettingsHelper.getFaSetWechat();
            key = MyApplication.getInstance().getMainActivity().getString(R.string.key_fas_wechat);
         }
         break;
         case 12: {
            set = SettingsHelper.getBlackListQQ();
            key = MyApplication.getInstance().getMainActivity().getString(R.string.key_black_list_qq);
         }
         break;
         case 22: {
            set = SettingsHelper.getBlackListWechat();
            key = MyApplication.getInstance().getMainActivity().getString(R.string.key_black_list_wechat);
         }
         break;
         default:
            set = new HashSet<>();
            key = "";
      }
      Object[] objects = new Object[2];
      objects[0] = set;
      objects[1] = key;
      return objects;
   }

   private ArrayList[] getAll() {
      ArrayList[] lists = new ArrayList[2];
      Set<String> fas = (Set<String>) getSetAndKey()[0];
      lists[0] = new ArrayList<>(fas);
      ArrayList<String> names = new SQLOperator(this).getAllNickname(
              type, (ArrayList<String>) lists[0]);
      lists[1] = names;
      return lists;
   }

   private ArrayList[] queryAlike(String query) {
      ArrayList[] lists = new ArrayList[2];
      lists[0] = new ArrayList();
      Set<String> set = (Set) getSetAndKey()[0];
      for (String name : set) {
         if (name.contains(query)) {
            lists[0].add(name);
         }
      }

      lists[1] = new SQLOperator(this).queryNickname(query, type, lists[0]);
      return lists;
   }


   private void refreshList(ArrayList[] lists) {

      LinearLayoutManager linear_lm = new LinearLayoutManager(this);
      linear_lm.setOrientation(LinearLayoutManager.VERTICAL);//
      listView.setLayoutManager(linear_lm);
      String title = listType == LIST_TYPE_FA ? "特别关心" : "黑名单";
      listView.setAdapter(new MultiTitleAdapter(pkgType, listType,
              lists, new String[]{title, "好友/群"}, new boolean[]{true, false}));
   }

   public void done(View view) {
      finish();
   }

}
