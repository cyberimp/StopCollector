package com.kalinasoft.stopcollector;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingsActivityFragment extends PreferenceFragmentCompat {

    public SettingsActivityFragment() {
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
    }

}
