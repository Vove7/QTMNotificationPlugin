package cn.vove7.qtmnotificationplugin.diydialogpreferences;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import cn.vove7.qtmnotificationplugin.R;
import cn.vove7.qtmnotificationplugin.utils.SettingsHelper;


public class TimePickerPreference extends DialogPreference {
   public TimePickerPreference(Context context) {
      super(context);
   }

   public TimePickerPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
      super(context, attrs, defStyleAttr, defStyleRes);
   }

   public TimePickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);
   }

   public TimePickerPreference(Context context, AttributeSet attrs) {
      super(context, attrs);
      //自定义属性values/attrs.xml
      TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.timePicker);
      defaultHour = typedArray.getInt(R.styleable.timePicker_defaultHour, 12);
      defaultMinute = typedArray.getInt(R.styleable.timePicker_defaultMinute, 0);
      typedArray.recycle();//回收
   }

   private int defaultHour;
   private int defaultMinute;

   @SuppressLint("DefaultLocale")
   @Override
   protected View onCreateView(ViewGroup parent) {

      currentTime = SettingsHelper.getPreference(getKey());
      if (currentTime == null) {
         currentTime = String.format("%02d:%02d", defaultHour, defaultMinute);
      }
      Log.d(this.getClass().getName(), "onCreateView: " + currentTime);
      setSummary(currentTime);
      return super.onCreateView(parent);
   }

   @Override
   protected View onCreateDialogView() {
      setDialogLayoutResource(R.layout.dialog_time_picker);
      return super.onCreateDialogView();
   }
   private String currentTime;

   @Override
   protected void onBindDialogView(View view) {
      super.onBindDialogView(view);
      timePicker = (view.findViewById(R.id.time_picker));
      timePicker.setIs24HourView(true);
      timePicker.setDrawingCacheEnabled(true);

      String[] s = currentTime.split(":");//old
      timePicker.setCurrentHour(Integer.parseInt(s[0]));
      timePicker.setCurrentMinute(Integer.parseInt(s[1]));
   }

   private TimePicker timePicker;

   public TimePicker getTimePicker() {
      return timePicker;
   }

   @SuppressLint("DefaultLocale")
   @Override
   public void onClick(DialogInterface dialogInterface, int which) {
      switch (which) {
         case Dialog.BUTTON_POSITIVE:
            //OK
            currentTime = String.format("%02d:%02d", timePicker.getCurrentHour(),
                    timePicker.getCurrentMinute());
            SettingsHelper.setValue(getKey(), currentTime);
            setSummary(currentTime);
            break;
         case Dialog.BUTTON_NEGATIVE:
         case Dialog.BUTTON_NEUTRAL:
            break;
      }
      super.onClick(dialogInterface, which);
   }
}
