package com.dudutech.biu.ui.setting;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;

import com.dudutech.biu.R;
import com.dudutech.biu.model.UserListModel;
import com.dudutech.biu.ui.common.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class SettingsActivity2 extends BaseActivity {

    public static final int SETTING_LOG_OUT = -1;
    private SettingsFragment mSettingsFragment;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.inject(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            mSettingsFragment = new SettingsFragment();
            replaceFragment(R.id.settings_container, mSettingsFragment);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void replaceFragment(int viewId, android.app.Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(viewId, fragment).commit();
    }

    /**
     * A placeholder fragment containing a settings view.
     */
    public static class SettingsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.

            Preference avatarPreference=findPreference("avatar_hd");
            bindPreferenceSummaryToValue(avatarPreference);

            boolean value= PreferenceManager
                    .getDefaultSharedPreferences(avatarPreference.getContext())
                    .getBoolean("avatar_hd", false);
            sBindPreferenceSummaryToValueListener.onPreferenceChange(avatarPreference, value
            );
            Preference picPreference=findPreference("pic_quantity");
            bindPreferenceSummaryToValue(picPreference);
            String picQuantity= PreferenceManager
                    .getDefaultSharedPreferences(picPreference.getContext())
                    .getString("pic_quantity", "");
            sBindPreferenceSummaryToValueListener.onPreferenceChange(picPreference, picQuantity);

            findPreference("about").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {


                    return true;
                }
            });
            findPreference("logout").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    Intent intent = new Intent();
                    intent.putExtra("opt", SETTING_LOG_OUT);
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();

                    return true;
                }
            });


        }
    }

    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.


    }

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {

             String key =preference.getKey();

            if(key.equals("avatar_hd")){

                preference.setDefaultValue(value);

            }
            else if (key.equals("pic_quantity")){
                String stringValue = value.toString();
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(listPreference.getEntries()[index]);
            }
            else {
                if(value instanceof  String){
                    String stringValue = value.toString();
                    preference.setSummary(stringValue);
                }


            }
            return true;
        }
    };

}
