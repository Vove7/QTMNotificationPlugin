package cn.vove7.qtmnotificationplugin.preferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import cn.vove7.qtmnotificationplugin.R;
import cn.vove7.qtmnotificationplugin.utils.SettingsHelper;
import cn.vove7.qtmnotificationplugin.utils.Utils;

public class VibratorOptionPreference extends DialogPreference {
   public VibratorOptionPreference(Context context) {
      super(context);
   }

   public VibratorOptionPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
      super(context, attrs, defStyleAttr, defStyleRes);
   }

   public VibratorOptionPreference(Context context, AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);
   }

   public VibratorOptionPreference(Context context, AttributeSet attrs) {
      super(context, attrs);

      TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.vibratorOptionsKey);
      strengthKey = typedArray.getString(R.styleable.vibratorOptionsKey_keyStrength);
      numKey = typedArray.getString(R.styleable.vibratorOptionsKey_keyNum);
      typedArray.recycle();//回收
   }

   private String strengthKey;
   private String numKey;

   @Override
   protected View onCreateDialogView() {
      setDialogLayoutResource(R.layout.vibrator_options);
      return super.onCreateDialogView();
   }

   @SuppressLint("DefaultLocale")
   @Override
   protected View onCreateView(ViewGroup parent) {
      newStrengthVal = oldStrengthVal = SettingsHelper.getInt(strengthKey, 100);
      newNumVal = oldNumVal = SettingsHelper.getInt(numKey, 2);

      setSummary(buildSummary());
      return super.onCreateView(parent);
   }

   private String buildSummary() {
      return String.format(getContext().getString(R.string.summary_vibrator_options), newStrengthVal, newNumVal);
   }

   private int oldStrengthVal;
   private int newStrengthVal;
   private int oldNumVal;
   private int newNumVal;

   private DiscreteSeekBar seekBarStrength;
   private DiscreteSeekBar seekBarNum;

   @Override
   protected void onBindDialogView(View view) {
      super.onBindDialogView(view);
      seekBarStrength = view.findViewById(R.id.vibrator_strength);
      seekBarStrength.setProgress(oldStrengthVal);
      seekBarStrength.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
         @Override
         public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
            newStrengthVal = value;
            if (fromUser)
               Utils.notificationVibrator(getContext(), newStrengthVal, 1);
         }

         @Override
         public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

         }

         @Override
         public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

         }

      });
      seekBarNum = view.findViewById(R.id.vibrator_num);
      seekBarNum.setProgress(oldNumVal);
      seekBarNum.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
         @Override
         public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
            newNumVal = value;
         }

         @Override
         public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

         }

         @Override
         public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

         }
      });
   }

   @Override
   public void onClick(DialogInterface dialog, int which) {
      switch (which) {
         case DialogInterface.BUTTON_POSITIVE:
            oldStrengthVal = newStrengthVal;
            oldNumVal = newNumVal;
            SettingsHelper.setValue(strengthKey, newStrengthVal);
            SettingsHelper.setValue(numKey, newNumVal);
            setSummary(buildSummary());
            break;
         case DialogInterface.BUTTON_NEUTRAL:
         case DialogInterface.BUTTON_NEGATIVE:
            seekBarStrength.setProgress(oldStrengthVal);
            seekBarNum.setProgress(oldNumVal);
            break;
         default:
            break;
      }
      super.onClick(dialog, which);
   }
}
