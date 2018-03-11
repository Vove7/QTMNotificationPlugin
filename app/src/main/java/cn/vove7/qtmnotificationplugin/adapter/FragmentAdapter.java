package cn.vove7.qtmnotificationplugin.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class FragmentAdapter extends FragmentPagerAdapter {

   private List<Fragment> list;

   public FragmentAdapter(FragmentManager fm) {
      super(fm);
   }

   public FragmentAdapter(FragmentManager fm, List<Fragment> list) {
      super(fm);
      this.list = list;
   }//写构造方法，方便赋值调用

   @Override
   public Fragment getItem(int arg0) {
      return list.get(arg0);
   }

   @Override
   public int getCount() {
      return list.size();
   }//设置Item的数量

}