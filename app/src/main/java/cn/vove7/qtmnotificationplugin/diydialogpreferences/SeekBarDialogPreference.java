package cn.vove7.qtmnotificationplugin.diydialogpreferences;

import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import cn.vove7.qtmnotificationplugin.R;
import cn.vove7.qtmnotificationplugin.util.SettingsHelper;
import cn.vove7.qtmnotificationplugin.util.Utils;

import static android.content.ContentValues.TAG;

public class SeekBarDialogPreference extends DialogPreference {
   public SeekBarDialogPreference(Context context) {
      super(context);
   }

   public SeekBarDialogPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
      super(context, attrs, defStyleAttr, defStyleRes);
   }

   public SeekBarDialogPreference(Context context, AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);
   }

   public SeekBarDialogPreference(Context context, AttributeSet attrs) {
      super(context, attrs);
   }

   @Override
   protected View onCreateDialogView() {
      setDialogLayoutResource(R.layout.seekbar_dialog);
      return super.onCreateDialogView();
   }

   @Override
   protected View onCreateView(ViewGroup parent) {
      oldVal = SettingsHelper.getInt(getKey(),100);
      setSummary(String.valueOf(oldVal));
      return super.onCreateView(parent);
   }

   private int oldVal;
   private int newVal;
   //private TextView textView;
   private DiscreteSeekBar seekBar;

   @Override
   protected void onBindDialogView(View view) {
      super.onBindDialogView(view);
      seekBar = view.findViewById(R.id.seekbar_vibrator_strength);
      seekBar.setProgress(oldVal);
      seekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
         @Override
         public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
            newVal = value;
            if (fromUser)
               Utils.notificationVibrator(getContext(), newVal, 1);
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
            oldVal=newVal;
            SettingsHelper.setValue(getKey(), newVal);
            setSummary(String.valueOf(newVal));
            break;
         case DialogInterface.BUTTON_NEUTRAL:
         case DialogInterface.BUTTON_NEGATIVE:
            oldVal = Integer.parseInt(getSummary().toString());
            Log.d(TAG, "onClick: oldVal" + oldVal);
            seekBar.setProgress(oldVal);
            break;
         default:
            break;
      }
      super.onClick(dialog, which);
   }
}
