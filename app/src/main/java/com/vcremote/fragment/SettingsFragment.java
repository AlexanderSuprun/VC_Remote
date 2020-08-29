package com.vcremote.fragment;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;

import com.vcremote.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference, rootKey);
    }
}
