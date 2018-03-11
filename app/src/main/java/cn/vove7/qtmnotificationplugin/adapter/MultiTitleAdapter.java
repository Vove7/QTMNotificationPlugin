package cn.vove7.qtmnotificationplugin.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;

import cn.vove7.qtmnotificationplugin.ManageFaActivity;
import cn.vove7.qtmnotificationplugin.R;
import cn.vove7.qtmnotificationplugin.util.SettingsHelper;

public class MultiTitleAdapter extends RecyclerView.Adapter<MultiTitleAdapter.ViewHolder> {
   private int pkgType;
   private int listType;
   private ArrayList<String>[] lists;
   private String[] titles;
   private boolean[] checks;

   static class ViewHolder extends RecyclerView.ViewHolder {
      TextView title;
      TextView nickname;
      CheckBox checkBox;
      View itemView;//

      public ViewHolder(View view) {
         super(view);
         itemView = view;//
         title = view.findViewById(R.id.item_title);
         nickname = view.findViewById(R.id.item_text);
         checkBox = view.findViewById(R.id.item_check);
      }
   }

   public MultiTitleAdapter(int pkgType, int listType, ArrayList<String>[] lists,
                            String[] titles, boolean[] checks) {
      this.listType = listType;
      this.pkgType = pkgType;
      this.lists = lists;
      this.titles = titles;
      this.checks = checks;
      if (lists.length != titles.length) {
         System.err.println("lists.length!=titles.length");
      }
   }

   @NonNull
   @Override
   public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext())
              .inflate(R.layout.search_item, parent, false);
      final ViewHolder holder = new ViewHolder(view);
      holder.itemView.setOnClickListener(view1 -> {
         String nickName = holder.nickname.getText().toString();
         if (!nickName.equals(""))
            toggleStatus(holder.checkBox, nickName);
      });
      return holder;
   }

   private void toggleStatus(CheckBox checkBox, String n) {
      checkBox.toggle();
      Object[] objects = ManageFaActivity.getSetAndKey(pkgType + listType);
      Set<String> set = (Set) objects[0];
      String key = (String) objects[1];

      if (checkBox.isChecked()) {
         set.add(n);
      } else {
         set.remove(n);
      }
      SettingsHelper.setValue(key, set);

      //
   }


   private static final String TAG = "Adapter";

   private int calNowGroupIndex(int p) {
      for (int i = 0; i < lists.length; i++) {
         if (p < getGroupNum(i + 1)) {
            return i;
         }
      }
      return lists.length - 1;
   }

   private int getGroupNum(int group) {
      if (group <= 0)
         return 0;
      int c = 0;
      for (int i = 0; i < group; i++) {
         c += lists[i].size();
      }
      return c + group;
   }

   @Override
   public void onBindViewHolder(ViewHolder holder, int position) {
      int nowGroupIndex = calNowGroupIndex(position);
      int index = position - getGroupNum(nowGroupIndex);
      Log.d(TAG, "onBindViewHolder: 当前pos->" + position +
              " nowGroup->" + nowGroupIndex + " index-> " + index);

      if (index == 0) {//标题
         Log.d(TAG, "onBindViewHolder: 标题->" + titles[nowGroupIndex]);
         holder.title.setText(titles[nowGroupIndex]);
         holder.title.setVisibility(View.VISIBLE);
         holder.nickname.setVisibility(View.GONE);
         holder.checkBox.setVisibility(View.GONE);
      } else {
         holder.title.setVisibility(View.GONE);
         holder.nickname.setVisibility(View.VISIBLE);
         holder.checkBox.setVisibility(View.VISIBLE);
         String s = lists[nowGroupIndex].get(index - 1);
         Log.d(TAG, "onBindViewHolder: 元素->" + s);
         holder.checkBox.setChecked(checks[nowGroupIndex]);
         holder.nickname.setText(s);
      }
   }

   @Override
   public int getItemCount() {
      int count = 0;
      for (ArrayList list : lists) {
         count += list.size();
      }
      return count + lists.length;
   }
}
