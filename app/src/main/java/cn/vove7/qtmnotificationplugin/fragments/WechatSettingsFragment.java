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
import cn.vove7.qtmnotificationplugin.utils.SettingsHelper;
import cn.vove7.qtmnotificationplugin.utils.Utils;

import static cn.vove7.qtmnotificationplugin.ManageFaActivity.LIST_TYPE_BLACK;
import static cn.vove7.qtmnotificationplugin.ManageFaActivity.LIST_TYPE_FA;
import static cn.vove7.qtmnotificationplugin.ManageFaActivity.PKG_TYPE_WECHAT;

public class WechatSettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
   public WechatSettingsFragment() {
      // Required empty public constructor
   }


   private static final int[] listenerOptionsWechat = new int[]{
           R.string.key_is_vibrator_wechat,
           R.string.key_is_alarm_wechat,
           R.string.key_ringtone_wechat,
           R.string.key_total_switch_wechat,
           R.string.key_is_no_distrubing_wechat,
           R.string.key_repeat_num_wechat,
           R.string.key_mode_wechat,
           R.string.key_fa_ringtone_wechat};

   private Map<String, String> mode;


   @Override
   public void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.preferences_wechat);
      mode = new HashMap<>();
      mode.put($(R.string.mode_default), $(R.string.text_default));
      mode.put($(R.string.mode_only_screen_off), $(R.string.text_only_screen_off));
      mode.put($(R.string.mode_only_fa), $(R.string.text_only_fa));

      //SettingsHelper.setOptionEnabled(this, allOptionsWechat, SettingsHelper.getTotalSwitchWechat());
      initPreferenceView();
      setListenerOptionsWechat();
   }

   private void setListenerOptionsWechat() {
      findPreference(getString(R.string.key_fas_wechat)).setOnPreferenceClickListener(preference -> {
         Intent intent = new Intent(WechatSettingsFragment.this.getActivity(), ManageFaActivity.class);
         intent.putExtra("pkgType", PKG_TYPE_WECHAT);
         intent.putExtra("listType", LIST_TYPE_FA);
         startActivity(intent);
         return false;
      });

      findPreference(getString(R.string.key_black_list_wechat)).setOnPreferenceClickListener(preference -> {
         Intent intent = new Intent(WechatSettingsFragment.this.getActivity(), ManageFaActivity.class);
         intent.putExtra("pkgType", PKG_TYPE_WECHAT);
         intent.putExtra("listType", LIST_TYPE_BLACK);
         startActivity(intent);
         return false;
      });


      for (int key : listenerOptionsWechat) {
         findPreference($(key)).setOnPreferenceChangeListener(this);
      }
   }

   private String $i(int v) {
      return String.valueOf(v);
   }

   private void initPreferenceView() {
      findPreference($(R.string.key_mode_wechat)).setSummary(mode.get(SettingsHelper.getModeWechat()));
      findPreference($(R.string.key_vibrator_strength_wechat)).setSummary($i(SettingsHelper.getVibratorStrengthWechat()));
      findPreference($(R.string.key_repeat_num_wechat)).setSummary($i(SettingsHelper.getRepeatNumWechat()));
      findPreference($(R.string.key_ringtone_wechat))
              .setSummary(Utils.getRingtoneTitleFromUri(this.getActivity(), SettingsHelper.getRingtoneWechat()));
      findPreference($(R.string.key_no_disturbing_begin_wechat))
              .setSummary(SettingsHelper.getNoDistrubingBeginTimeWechat());
      findPreference($(R.string.key_no_disturbing_end_wechat))
              .setSummary(SettingsHelper.getNoDistrubingEndTimeWechat());
      findPreference($(R.string.key_fa_ringtone_wechat))
              .setSummary(Utils.getRingtoneTitleFromUri(this.getActivity(), SettingsHelper.getFaRingtoneWechat()));


   }

   private String $(int resId) {
      return getString(resId);
   }

   @Override
   public boolean onPreferenceChange(Preference preference, Object newValue) {
      String key = preference.getKey();
      if (key.equals($(listenerOptionsWechat[0]))) {
         //SettingsHelper.setOptionEnabled(this, vibratorOptionsWechat, (boolean) newValue);
      } else if (key.equals($(listenerOptionsWechat[1]))) {
         //SettingsHelper.setOptionEnabled(this, alarmOptionsWechat, (boolean) newValue);
      } else if (key.equals($(listenerOptionsWechat[2]))) {
         preference.setSummary(Utils.getRingtoneTitleFromUri(this.getActivity(), (String) newValue));
      } else if (key.equals($(listenerOptionsWechat[3]))) {
         //SettingsHelper.setOptionEnabled(this, allOptionsWechat, (boolean) newValue);
      } else if (key.equals($(listenerOptionsWechat[4]))) {
         //SettingsHelper.setOptionEnabled(this, noDistrubingOptionsWechat, (boolean) newValue);
      } else if (key.equals($(listenerOptionsWechat[5]))) {
         preference.setSummary(String.valueOf((newValue)));
      } else if (key.equals($(listenerOptionsWechat[6]))) {
         preference.setSummary(mode.get(newValue));
      } else if (key.equals($(listenerOptionsWechat[7]))) {
         preference.setSummary(Utils.getRingtoneTitleFromUri(this.getActivity(), (String) newValue));
      }
      return true;
   }
}
