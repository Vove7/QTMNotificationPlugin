package cn.vove7.qtmnotificationplugin.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import cn.vove7.qtmnotificationplugin.ManageFaActivity;
import cn.vove7.qtmnotificationplugin.R;
import cn.vove7.qtmnotificationplugin.util.SettingsHelper;
import cn.vove7.qtmnotificationplugin.util.Utils;

public class QQSettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

   public QQSettingsFragment() {
   }/*
   public static final Integer[] vibratorOptionsQQ = new Integer[]{
           R.string.key_vibrator_strength_qq,
           R.string.key_repeat_num_qq
   };
   public static final Integer[] noDistrubingOptionsQQ = new Integer[]{
           R.string.key_no_disturbing_begin_qq,
           R.string.key_no_disturbing_end_qq,
           R.string.key_is_fa_no_distrubing_qq
   };
   public static final Integer[] alarmOptionsQQ = new Integer[]{R.string.key_ringtone_qq};
   public static final Integer[][] allOptionsQQ = new Integer[][]{{
           0, R.string.key_mode_qq}, {
           1, R.string.key_is_vibrator_qq,
           R.string.key_vibrator_strength_qq,
           R.string.key_repeat_num_qq}, {
           1, R.string.key_is_alarm_qq,
           R.string.key_ringtone_qq}, {
           1, R.string.key_is_no_distrubing_qq,
           R.string.key_no_disturbing_begin_qq,
           R.string.key_no_disturbing_end_qq,
           R.string.key_is_fa_no_distrubing_qq}*//*, {
           0, R.string.key_fas_qq,
           R.string.key_fa_ringtone_qq}*//*, {
           0, R.string.key_black_list_qq}
   };
*/

   private static final int[] listenerOptionsQQ = new int[]{
           R.string.key_is_vibrator_qq,
           R.string.key_is_alarm_qq,
           R.string.key_ringtone_qq,
           R.string.key_total_switch_qq,
           R.string.key_is_no_distrubing_qq,
           R.string.key_repeat_num_qq,
           R.string.key_mode_qq,
           R.string.key_fa_ringtone_qq};
   private Map<String, String> mode;

   @Override
   public void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.preferences_qq);
      //for (int id : listenerOptionsQQ) {
      //   findPreference($(id)).setOnPreferenceChangeListener(this);
      //}
      mode = new HashMap<>();
      mode.put($(R.string.mode_default), $(R.string.text_default));
      mode.put($(R.string.mode_only_screen_off), $(R.string.text_only_screen_off));
      mode.put($(R.string.mode_only_fa), $(R.string.text_only_fa));

      //SettingsHelper.setOptionEnabled(this, allOptionsQQ, SettingsHelper.getTotalSwitchQQ());
      initPreferenceView();
      findPreference(getString(R.string.key_fas_qq)).setOnPreferenceClickListener(preference -> {
         startActivity(new Intent(QQSettingsFragment.this.getActivity(), ManageFaActivity.class));
         return false;
      });
   }

   private String $i(int v) {
      return String.valueOf(v);
   }

   private void initPreferenceView() {
      findPreference($(R.string.key_mode_qq)).setSummary(mode.get(SettingsHelper.getModeQQ()));
      findPreference($(R.string.key_vibrator_strength_qq)).setSummary($i(SettingsHelper.getVibratorStrengthQQ()));
      findPreference($(R.string.key_repeat_num_qq)).setSummary($i(SettingsHelper.getRepeatNumQQ()));
      findPreference($(R.string.key_ringtone_qq))
              .setSummary(Utils.getRingtoneTitleFromUri(this.getActivity(), SettingsHelper.getRingtoneQQ()));
      findPreference($(R.string.key_no_disturbing_begin_qq))
              .setSummary(SettingsHelper.getNoDistrubingBeginTimeQQ());
      findPreference($(R.string.key_no_disturbing_end_qq))
              .setSummary(SettingsHelper.getNoDistrubingEndTimeQQ());
      findPreference($(R.string.key_fa_ringtone_qq))
              .setSummary(Utils.getRingtoneTitleFromUri(this.getActivity(), SettingsHelper.getFaRingtoneQQ()));


   }

   private String $(int resId) {
      return getString(resId);
   }

   @Override
   public boolean onPreferenceChange(Preference preference, Object newValue) {
      String key = preference.getKey();
      if (key.equals($(listenerOptionsQQ[0]))) {
         //SettingsHelper.setOptionEnabled(this, vibratorOptionsQQ, (boolean) newValue);
      } else if (key.equals($(listenerOptionsQQ[1]))) {
         //SettingsHelper.setOptionEnabled(this, alarmOptionsQQ, (boolean) newValue);
      } else if (key.equals($(listenerOptionsQQ[2]))) {
         preference.setSummary(Utils.getRingtoneTitleFromUri(this.getActivity(), (String) newValue));
      } else if (key.equals($(listenerOptionsQQ[3]))) {
         //SettingsHelper.setOptionEnabled(this, allOptionsQQ, (boolean) newValue);
      } else if (key.equals($(listenerOptionsQQ[4]))) {
         //SettingsHelper.setOptionEnabled(this, noDistrubingOptionsQQ, (boolean) newValue);
      } else if (key.equals($(listenerOptionsQQ[5]))) {
         preference.setSummary(String.valueOf((newValue)));
      } else if (key.equals($(listenerOptionsQQ[6]))) {
         preference.setSummary(mode.get(newValue));
      } else if (key.equals($(listenerOptionsQQ[7]))) {
         preference.setSummary(Utils.getRingtoneTitleFromUri(this.getActivity(), (String) newValue));
      }
      return true;
   }
}
