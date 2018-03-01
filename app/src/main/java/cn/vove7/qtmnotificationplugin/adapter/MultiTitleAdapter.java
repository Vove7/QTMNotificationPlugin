package cn.vove7.qtmnotificationplugin.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import cn.vove7.qtmnotificationplugin.R;

public class MultiTitleAdapter extends RecyclerView.Adapter<MultiTitleAdapter.ViewHolder> {
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

   public MultiTitleAdapter(ArrayList<String>[] lists, String[] titles, boolean[] checks) {
      this.lists = lists;
      this.titles = titles;
      this.checks = checks;
      if (lists.length != titles.length) {
         System.err.println("lists.length!=titles.length");
      }
   }

   @Override
   public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
      final ViewHolder holder = new ViewHolder(view);
      holder.nickname.setOnClickListener(view1 -> {
         int pos = holder.getAdapterPosition();
         toggleStatus();

      });
      return holder;
   }

   private void toggleStatus() {

   }

   int nowGroupIndex = 0;

   private static final String TAG = "Adapter";

   int p = 0;

   @Override
   public void onBindViewHolder(ViewHolder holder, int position) {
      Log.d(TAG, "onBindViewHolder: 当前pos->" + position + " nowGroup->" + nowGroupIndex);
      int num = getGroupNum(nowGroupIndex);//前面的数量
      Log.d(TAG, "onBindViewHolder: num->" + num);
      if (num == 0 || position % num == 0) {//标题
         Log.d(TAG, "onBindViewHolder: 标题");
         holder.title.setText(titles[nowGroupIndex]);
         holder.title.setVisibility(View.VISIBLE);
         holder.nickname.setVisibility(View.GONE);
         holder.checkBox.setVisibility(View.GONE);
         nowGroupIndex++;
         p = 0;
      } else {
         Log.d(TAG, "onBindViewHolder: 元素");
         String s = lists[nowGroupIndex - 1].get(p++);
         holder.checkBox.setChecked(checks[nowGroupIndex-1]);
         holder.nickname.setText(s);
      }


   }

   private int getGroupNum(int group) {
      int c = 0;
      for (int i = 0; i < group; i++) {
         c += lists[i].size();
      }
      return c + group;
   }

   @Override
   public int getItemCount() {
      int count = 0;
      for (ArrayList list : lists) {
         count += list.size();
      }
      Log.d(TAG, "getItemCount: count->" + (count + lists.length));
      return count + lists.length;
   }
}
