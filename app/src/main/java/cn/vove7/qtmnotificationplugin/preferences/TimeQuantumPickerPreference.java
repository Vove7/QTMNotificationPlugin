package cn.vove7.qtmnotificationplugin.preferences;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.Preference;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import cn.vove7.qtmnotificationplugin.R;
import cn.vove7.qtmnotificationplugin.utils.AppUtils;
import cn.vove7.qtmnotificationplugin.utils.SettingsHelper;


public class TimeQuantumPickerPreference extends Preference {
   public TimeQuantumPickerPreference(Context context) {
      super(context);
   }

   public TimeQuantumPickerPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
      super(context, attrs, defStyleAttr, defStyleRes);
   }

   public TimeQuantumPickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);
   }

   public TimeQuantumPickerPreference(Context context, AttributeSet attrs) {
      super(context, attrs);
      //自定义属性values/attrs.xml
      TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.timePicker);
      typedArray.recycle();//回收
   }

   private int[] initHours = new int[2];
   private int[] initMinutes = new int[2];
   private int[] newHours = new int[2];
   private int[] newMinutes = new int[2];

   private void initSummary() {
      String timeQuantum = SettingsHelper.getString(getKey(), "23:00-06:00");
      String[] times = timeQuantum.split("-");
      String[] timeBegin = times[0].split(":");
      String[] timeEnd = times[1].split(":");
      initHours[0] = Integer.parseInt(timeBegin[0]);
      initHours[1] = Integer.parseInt(timeEnd[0]);

      initMinutes[0] = Integer.parseInt(timeBegin[1]);
      initMinutes[1] = Integer.parseInt(timeEnd[1]);
      newHours = initHours;
      newMinutes = initMinutes;
      setSummary(timeQuantum);
   }

   @Override
   protected View onCreateView(ViewGroup parent) {
      initSummary();
      return super.onCreateView(parent);
   }

   private TimeQuantumPickerDialog pickerDialog;

   @Override
   protected void onClick() {
      if (pickerDialog == null)
         pickerDialog = new TimeQuantumPickerDialog(getContext());

      pickerDialog.show();
   }

   class PagerAdapterWithTimePicker extends PagerAdapter {
      @SuppressLint("DefaultLocale")
      String buildQuantum() {
         return String.format("%02d:%02d-%02d:%02d", newHours[0], newMinutes[0], newHours[1], newMinutes[1]);
      }

      public PagerAdapterWithTimePicker() {
         super();
      }

      @Override
      public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
         return view == object;
      }

      @Override
      public int getCount() {
         return 2;
      }

      @NonNull
      @Override
      public Object instantiateItem(@NonNull ViewGroup container, int position) {
         TimePicker timePicker = new TimePicker(container.getContext());
         timePicker.setIs24HourView(true);
         timePicker.setCurrentHour(initHours[position]);
         timePicker.setCurrentMinute(initMinutes[position]);
         timePicker.setOnTimeChangedListener((timePicker1, i, i1) -> {
            newHours[position] = i;
            newMinutes[position] = i1;
         });
         container.addView(timePicker);
         return timePicker;
      }

      private final String[] TITLES = new String[]{
              "开始时间", "结束时间"
      };

      @Nullable
      @Override
      public CharSequence getPageTitle(int position) {
         return TITLES[position];
      }
   }

   class TimeQuantumPickerDialog extends Dialog implements View.OnClickListener {
      View contentView;
      Context context;
      Button positiveButton, negativeButton, neutralButton;

      private String timeQuantum;
      private ViewPager viewPager;
      private TabLayout tabLayout;
      private PagerAdapterWithTimePicker adapterWithTimePicker;

      private int wh[];

      public TimeQuantumPickerDialog(@NonNull Context context) {
         super(context);
         wh = AppUtils.getScreenHW(context);
         this.context = context;
      }

      @Override
      protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         contentView = LayoutInflater.from(context).inflate(R.layout.dialog_time_quantum_picker, null);
         setContentView(contentView);
         positiveButton = findViewById(R.id.button_positive);
         negativeButton = findViewById(R.id.button_negative);
         neutralButton = findViewById(R.id.button_neutral);

         neutralButton.setOnClickListener(this);
         positiveButton.setOnClickListener(this);
         negativeButton.setOnClickListener(this);
         initView();
      }

      private void initData() {
         newHours = initHours;
         newMinutes = initMinutes;
         viewPager.setCurrentItem(0);
         positiveButton.setText(R.string.text_next_step);
      }

      @Override
      public void show() {
         super.show();
         initData();
      }

      @SuppressLint("ClickableViewAccessibility")
      private void initView() {
         tabLayout = findViewById(R.id.tab_view);
         viewPager = findViewById(R.id.view_page);
         viewPager.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (wh[1] * 0.9)));
         adapterWithTimePicker = new PagerAdapterWithTimePicker();
         viewPager.setAdapter(adapterWithTimePicker);
         viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
               Log.d("TimeQuantumPicker :", "onPageSelected  ----> " + position);
               if (position == 1) {
                  positiveButton.setText(R.string.text_button_positive);
               } else if (position == 0) {
                  positiveButton.setText(R.string.text_next_step);
               }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
         });

         tabLayout.setupWithViewPager(viewPager);
      }

      @Override
      public void onClick(View view) {
         switch (view.getId()) {
            case R.id.button_positive:
               if (viewPager.getCurrentItem() == 0) {//下一步
                  viewPager.setCurrentItem(1);
                  positiveButton.setText(R.string.text_button_positive);
                  return;
               }
               initHours = newHours;
               initMinutes = newMinutes;
               timeQuantum = adapterWithTimePicker.buildQuantum();
               SettingsHelper.setValue(getKey(), timeQuantum);
               setSummary(timeQuantum);
               dismiss();
               break;
            case R.id.button_negative:
               cancel();
               break;
            case R.id.button_neutral:
               dismiss();
               break;
         }
      }
   }
}
