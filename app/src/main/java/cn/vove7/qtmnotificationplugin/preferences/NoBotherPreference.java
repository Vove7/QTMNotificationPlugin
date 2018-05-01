package cn.vove7.qtmnotificationplugin.preferences;

import android.content.Context;
import android.util.AttributeSet;

import cn.vove7.qtmnotificationplugin.R;
import cn.vove7.qtmnotificationplugin.preferences.ab.NumberPickerPreference;

public class NoBotherPreference extends NumberPickerPreference {
   public NoBotherPreference(Context context) {
      super(context);
   }

   public NoBotherPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
      super(context, attrs, defStyleAttr, defStyleRes);
   }

   public NoBotherPreference(Context context, AttributeSet attrs) {
      super(context, attrs);
   }

   @Override
   public String buildSummary() {
      return (String.format(getContext().getString(R.string.summary_no_bother), newVal));

   }

   @Override
   protected int defaultValue() {
      return maxValue();
   }

   @Override
   public int minValue() {
      return 3;
   }

   @Override
   public int maxValue() {
      return 99;
   }
}
