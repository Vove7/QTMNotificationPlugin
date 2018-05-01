package cn.vove7.qtmnotificationplugin.preferences.ab;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import cn.vove7.qtmnotificationplugin.R;
import cn.vove7.qtmnotificationplugin.utils.SettingsHelper;

public abstract class NumberPickerPreference extends DialogPreference {
   public NumberPickerPreference(Context context) {
      super(context);
   }

   public NumberPickerPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
      super(context, attrs, defStyleAttr, defStyleRes);
   }

   public NumberPickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);
   }

   public NumberPickerPreference(Context context, AttributeSet attrs) {
      super(context, attrs);

      TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.numberPicker);
      minValue = typedArray.getInt(R.styleable.numberPicker_minValue, minValue());
      maxValue = typedArray.getInt(R.styleable.numberPicker_maxValue, maxValue());
      typedArray.recycle();//回收
   }

   protected int minValue;
   protected int maxValue;

   protected abstract int minValue();
   protected abstract int maxValue();
   protected abstract int defaultValue();
   @Override
   protected View onCreateDialogView() {
      setDialogLayoutResource(R.layout.seekbar_dialog);
      return super.onCreateDialogView();
   }

   @Override
   protected View onCreateView(ViewGroup parent) {
      newVal = oldVal = SettingsHelper.getInt(getKey(), defaultValue());
      setSummary(buildSummary());
      return super.onCreateView(parent);
   }

   public abstract String buildSummary();


   protected int oldVal;
   protected int newVal;
   private TextView minView;
   private TextView maxView;
   private DiscreteSeekBar seekBar;

   @Override
   protected void onBindDialogView(View view) {
      super.onBindDialogView(view);
      seekBar = view.findViewById(R.id.seekbar);
      minView = view.findViewById(R.id.min_value_view);
      minView.setText(String.valueOf(minValue));
      maxView = view.findViewById(R.id.max_value_view);
      maxView.setText(String.valueOf(maxValue));
      seekBar.setMin(minValue);
      seekBar.setMax(maxValue);
      seekBar.setProgress(oldVal);
      seekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
         @Override
         public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
            newVal = value;
         }

         @Override
         public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

         }

         @Override
         public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

         }

      });
   }

   @Override
   public void onClick(DialogInterface dialog, int which) {
      switch (which) {
         case DialogInterface.BUTTON_POSITIVE:
            oldVal = newVal;
            SettingsHelper.setValue(getKey(), newVal);
            setSummary(buildSummary());
            break;
         case DialogInterface.BUTTON_NEUTRAL:
         case DialogInterface.BUTTON_NEGATIVE:
            seekBar.setProgress(oldVal);
            break;
         default:
            break;
      }
      super.onClick(dialog, which);
   }
}
